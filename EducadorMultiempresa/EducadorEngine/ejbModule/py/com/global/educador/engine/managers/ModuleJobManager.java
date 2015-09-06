package py.com.global.educador.engine.managers;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.ejb.AccessTimeout;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.TransactionTimeout;

import py.com.global.educador.engine.cache.SystemParameterCache;
import py.com.global.educador.engine.configuration.EducadorConstants;
import py.com.global.educador.engine.configuration.EducadorConstants.QueueMessageParamKey;
import py.com.global.educador.engine.configuration.EducadorConstants.SystemParameterKey;
import py.com.global.educador.engine.enums.EstadoEjecucionSuscriptor;
import py.com.global.educador.engine.enums.EstadoEvaluacion;
import py.com.global.educador.engine.enums.EstadoModulo;
import py.com.global.educador.engine.enums.EstadoRegistro;
import py.com.global.educador.engine.enums.FlowProccessType;
import py.com.global.educador.engine.enums.MotivosCancelacion;
import py.com.global.educador.engine.enums.ScheduleParameters;
import py.com.global.educador.engine.interfaces.ModuleWorker;
import py.com.global.educador.jpa.entity.Evaluacion;
import py.com.global.educador.jpa.entity.Modulo;

@Singleton
public class ModuleJobManager {

	@Resource
	TimerService timerService;
	@PersistenceContext(unitName="EducadorJpa")
	EntityManager entityManager;
	@EJB SessionManager sessionManager;
	@EJB SystemParameterCache systemParameterCache;
	@EJB ModuleWorker moduleWorker;

	Logger log=Logger.getLogger(ModuleJobManager.class);


	@Lock(LockType.READ)
	public boolean createTimer(String conf, Map<String, Object> params){
		if (conf==null || conf.trim().isEmpty()) {
			log.warn("No se pudo crear una tarea programada para la conf nula o vacia");
			return false;
		}
		if (params==null || params.isEmpty()) {
			log.warn("No se pudo crear una tarea programada para parametros nulos o vacios [conf="+conf+"]");
			return false;
		}
		ScheduleExpression scheduleExpression= createScheduleFor(conf,null,null);
		if (scheduleExpression==null) {
			log.warn("No se pudo crear una tarea programada para la conf---> "+conf);
			return false;
		}


		log.trace("Creando timer para conf---> "+conf);
		params.put(EducadorConstants.QueueMessageParamKey.CONFIGURATION, conf);
		timerService.createCalendarTimer(scheduleExpression, new TimerConfig((Serializable) params, false));

		return true;
	}

	private ScheduleExpression createScheduleFor(String conf, Date startDate, Date endDate) {
		ScheduleExpression se= new ScheduleExpression();


		if (startDate!=null) {
			se.start(startDate);
		}
		if (endDate!=null) {
			se.end(endDate);
		}

		if (EducadorConstants.Constants.ENVIO_INMEDIATO.equalsIgnoreCase(conf.trim())) {
			se.hour("*");
			se.minute("*/5");
			return se;
		}

		Map<String, String> confMap=getMapOfScheduleStringConf(conf);
		if (confMap==null || confMap.isEmpty()) {
			return null;
		}
		for (Entry<String, String> entry : confMap.entrySet()) {
			ScheduleParameters sp= ScheduleParameters.valueOf(entry.getKey());
			//log.debug("Current Entry--> "+entry.getKey());
			switch (sp) {
			case year:
				log.trace("year="+entry.getValue());
				se.year(entry.getValue());
				break;
			case month:
				log.trace("month="+entry.getValue());
				se.month(entry.getValue());
				break;
			case dayOfMonth:
				log.trace("dayOfMonth="+entry.getValue());
				se.dayOfMonth(entry.getValue());
				break;
			case dayOfWeek:
				log.trace("dayOfWeek="+entry.getValue());
				se.dayOfWeek(entry.getValue());
				break;
			case hour:
				log.trace("hour="+entry.getValue());
				se.hour(entry.getValue());
				break;
			case minute:
				log.trace("minute="+entry.getValue());
				se.minute(entry.getValue());
				break;
			case second:
				log.trace("second="+entry.getValue());
				se.second(entry.getValue());
				break;
			default:
				break;
			}
		}

		return se;
	}

	private Map<String, String> getMapOfScheduleStringConf(String conf) {
		HashMap<String, String> m= new HashMap<String, String>();
		if (conf==null || conf.isEmpty()) {
			return null;
		}
		String [] values=conf.split(";");
		for (String v : values) {
			String[] keyValue=v.split("=");
			m.put(keyValue[0].trim(), keyValue[1].trim());
		}
		log.trace("MAP FOR CONF---> "+m);
		return m;
	}

	@SuppressWarnings("unchecked")
	@Timeout
	@TransactionTimeout(value=60,unit=TimeUnit.MINUTES)
	@AccessTimeout(value = 20, unit = TimeUnit.MINUTES)
	/*
	 * Ejecuta la tarea del modulo para el canal de sms
	 * */
	public void runJob(Timer timer){
		Map<String, Object> params=(Map<String, Object>) timer.getInfo();
		loadAndSendModuleSubscribersTaskRequest(params, timer);


	}

	private void loadAndSendModuleSubscribersTaskRequest(Map<String, Object> params, Timer timer) {

		try {
			String flowType=systemParameterCache.getValue(SystemParameterKey.SYSTEM_ENGINE_PROCESS_FLOW_METHOD);

			long t1=System.currentTimeMillis();

			Long moduloId=(Long)params.get(EducadorConstants.QueueMessageParamKey.MODULE_ID);
			Long evaluacionId=(Long)params.get(EducadorConstants.QueueMessageParamKey.EVALUATION_ID);
			String type=(String) params.get(QueueMessageParamKey.TYPE);
			Modulo modulo=entityManager.find(Modulo.class, moduloId);

			Evaluacion ev=null;
			if (modulo!=null) {
				if (modulo.getFechaFin().before(new Date()) || 
						EstadoModulo.CANCELADO.name().equalsIgnoreCase(modulo.getEstadoModulo()) ||
						EstadoModulo.TERMINADO.name().equalsIgnoreCase(modulo.getEstadoModulo())
						|| EstadoRegistro.INACTIVO.name().equalsIgnoreCase(modulo.getEstadoRegistro())) {


					switch (type) {
					case "TIP":
						log.info("Cancelando Modulo ["+moduloId+"] Finalizado...[params---> "+params+"]");
						modulo.setEstadoModulo(EstadoModulo.TERMINADO.name());
						modulo.setEstadoRegistro(EstadoRegistro.INACTIVO.name());
						entityManager.merge(modulo);
						log.debug("Updating ejecuciones del modulo...");
						setEjecucionesModulo(modulo, EstadoEjecucionSuscriptor.CANCELADO);

						break;
					case "EVALUACION":
						ev= entityManager.find(Evaluacion.class, evaluacionId);
						ev.setEstadoEvaluacion(EstadoEvaluacion.TERMINADO.name());
						ev.setEstadoRegistro(EstadoRegistro.INACTIVO.name());
						entityManager.merge(ev);

						break;
					default:
						break;
					}




					timer.cancel();
					return;
				}
			}
			else{
				log.error("Modulo es NULO!, se cancelara el timer [params]--> "+params);
				timer.cancel();
			}

			switch (type) {
			case "TIP":
				if (EstadoModulo.MODIFICADO.name().equalsIgnoreCase(modulo.getEstadoModulo())) {
					log.debug("Configuracion de envio modificada. Creando timer nuevo. Cancelado actual--> "+params);
					params.put(QueueMessageParamKey.CONFIGURATION, modulo.getConfiguracionEnvioTip());
					createTimer(modulo.getConfiguracionEnvioTip(), params);
					modulo.setEstadoModulo(EstadoModulo.INICIADO.name());
					entityManager.merge(modulo);
					timer.cancel();

				}
				break;
			case "EVALUACION":
				ev= entityManager.find(Evaluacion.class, evaluacionId);
				if (EstadoEvaluacion.MODIFICADO.name().equalsIgnoreCase(ev.getEstadoEvaluacion())) {
					log.debug("Configuracion de envio modificada. Creando timer nuevo. Cancelado actual--> "+params);
					params.put(QueueMessageParamKey.CONFIGURATION, ev.getConfiguracionEnvioModulo());
					createTimer(ev.getConfiguracionEnvioModulo(), params);
					ev.setEstadoEvaluacion(EstadoEvaluacion.INICIADO.name());
					entityManager.merge(ev);
					timer.cancel();

				}
				break;
			default:
				break;
			}

			if (type!=null && type.equalsIgnoreCase("EVALUACION")) {
				if (flowType==null || flowType.trim().isEmpty() || flowType.equalsIgnoreCase(FlowProccessType.SIMPLE.name())) {
					log.info("El timer de evaluacion sera controlado por el mismo servicio del modulo. [params] -> "+params);
					return;
				}
			}
			
			
			moduleWorker.process(params);


			long t2=System.currentTimeMillis();
			log.debug("t2-t1---> "+(t2-t1)+"ms.");
		} catch (Exception e) {
			log.error("loadAndSendModuleSubscribersTaskRequest", e);
		}

	}





	private void setEjecucionesModulo(Modulo modulo,
			EstadoEjecucionSuscriptor estado) {
		String sql="UPDATE EJECUCION_SUSCRIPTOR SET ESTADO_EJECUCION= :estado,MOTIVO_CANCELACION= :motivo  WHERE ID_MODULO=:idModulo AND ESTADO_EJECUCION= :activo ";
		Query q=entityManager.createNativeQuery(sql);
		q.setParameter("estado", estado.name());
		q.setParameter("idModulo", modulo.getIdModulo());
		q.setParameter("motivo", MotivosCancelacion.MODULO_FINALIZADO.name());
		q.setParameter("activo", EstadoEjecucionSuscriptor.ACTIVO.name());
		int r=q.executeUpdate();
		log.debug("Cantidad de ejecuciones canceladas para el [modulo="+modulo.getIdModulo()+"]---> "+r);



	}


}
