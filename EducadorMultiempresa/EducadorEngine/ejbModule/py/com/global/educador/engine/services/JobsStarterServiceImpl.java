package py.com.global.educador.engine.services;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.cache.SystemParameterCache;
import py.com.global.educador.engine.configuration.EducadorConstants;
import py.com.global.educador.engine.configuration.EducadorConstants.QueueMessageParamKey;
import py.com.global.educador.engine.enums.EstadoEvaluacion;
import py.com.global.educador.engine.enums.EstadoModulo;
import py.com.global.educador.engine.enums.EstadoRegistro;
import py.com.global.educador.engine.managers.ModuleJobManager;
import py.com.global.educador.jpa.entity.Evaluacion;
import py.com.global.educador.jpa.entity.Modulo;

@Singleton
@Startup
public class JobsStarterServiceImpl {

	@PersistenceContext(unitName = "EducadorJpa")
	EntityManager entityManager;
	@EJB
	ModuleJobManager moduleJobManager;
	@Resource
	TimerService timerService;

	@EJB SystemParameterCache systemParameterCache;
	
	private Logger log = Logger.getLogger(JobsStarterServiceImpl.class);

	@PostConstruct
	public void init(){
		log.debug("Starting stored Modules and Evaluations..");
		startStoredModules();
		//startStoredEvaluations();
	}


	private void startStoredModules() {
		String[] estadosMoudlos={EstadoModulo.INICIADO.name()};
		startModules0(estadosMoudlos, EstadoRegistro.ACTIVO);


	}


	public void startStoredEvaluations() {
		String [] estadosEvaluacion= {EstadoEvaluacion.INICIADO.name()};
		startEvaluaciones0(estadosEvaluacion, EstadoRegistro.ACTIVO);


	}


	@Schedule(dayOfMonth = "*", hour = "*", minute = "*/5",persistent=false)
	public void startJobs(Timer timer) {
		log.debug("Starting new Modules and new Evaluations..");
		try {
			startNewModules();
			//startNewEvaluaciones();
		} catch (Exception e) {
			log.error("startJobs-Exception",e);
		}

	}



	private void startNewModules() {
		String[] estadosMoudlos={EstadoModulo.CREADO.name()};
		startModules0(estadosMoudlos, EstadoRegistro.ACTIVO);


	}
	
	private void startModules0(String[] estadosMoudlos, EstadoRegistro estadoRegistro) {

		List<Modulo> l = getModulos(Arrays.asList(estadosMoudlos), estadoRegistro);
		log.debug("Cantidad de modulos en estado ["+Arrays.toString(estadosMoudlos)+"] encontrados--> "+l.size());
		for (Modulo modulo : l) {

			HashMap<String, Object> params= new HashMap<String, Object>();
			params.put( EducadorConstants.QueueMessageParamKey.TYPE, "TIP");
			params.put(QueueMessageParamKey.PROJECT_ID, modulo.getProyecto().getIdProyecto());
			params.put(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER, modulo.getProyecto().getNumeroCorto());
			params.put(EducadorConstants.QueueMessageParamKey.MODULE_ID, modulo.getIdModulo());
			params.put(QueueMessageParamKey.CONFIGURATION, modulo.getConfiguracionEnvioTip());
			params.put(QueueMessageParamKey.START_DATE, modulo.getFechaInicio());
			params.put(QueueMessageParamKey.END_DATE, modulo.getFechaFin());
			if(moduleJobManager.createTimer(modulo.getConfiguracionEnvioTip(), params)){
				log.debug("Timer Creado para [modulo="+modulo.getIdModulo()+"]");
				modulo.setEstadoModulo(EstadoModulo.INICIADO.name());
				entityManager.merge(modulo);
			}
		}

	}

	public void startNewEvaluaciones(){
		String [] estadosEvaluacion= {EstadoEvaluacion.CREADO.name()};
		startEvaluaciones0(estadosEvaluacion, EstadoRegistro.ACTIVO);

	}

	private void startEvaluaciones0(String [] estadosEvaluacion, EstadoRegistro estadoRegistro){
		List<Evaluacion> l = getEvaluaciones(Arrays.asList(estadosEvaluacion), estadoRegistro);
		log.debug("Cantidad de evaluaciones en estado ["+Arrays.toString(estadosEvaluacion)+"] encontradas--> "+l.size());
		for (Evaluacion evaluacion : l) {
			HashMap<String, Object> params= new HashMap<String, Object>();
			params.put( EducadorConstants.QueueMessageParamKey.TYPE, "EVALUACION");
			params.put(EducadorConstants.QueueMessageParamKey.EVALUATION_ID, evaluacion.getIdEvaluacion());
			params.put(QueueMessageParamKey.PROJECT_ID,evaluacion.getModulo().getProyecto().getIdProyecto());
			params.put(EducadorConstants.QueueMessageParamKey.MODULE_ID, evaluacion.getModulo().getIdModulo());
			params.put(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER, evaluacion.getModulo().getProyecto().getNumeroCorto());
			params.put(QueueMessageParamKey.CONFIGURATION, evaluacion.getConfiguracionEnvioModulo());
			if(moduleJobManager.createTimer(evaluacion.getConfiguracionEnvioModulo(), params)){
				log.debug("Timer Creado para [evaluacion="+evaluacion.getIdEvaluacion()+"]");
				evaluacion.setEstadoEvaluacion(EstadoEvaluacion.INICIADO.name());
				entityManager.merge(evaluacion);
			}
			else{
				log.error("No se pudo crear la tarea programada para la [evaluacion="+evaluacion.getIdEvaluacion()+", verifique los errores previos]");
			}
		}
	}


	
	@SuppressWarnings("unchecked")
	private List<Evaluacion> getEvaluaciones(List<String> estadosEvaluacion, EstadoRegistro estadoRegistro){
		String hql = "SELECT _e FROM Evaluacion _e WHERE _e.estadoEvaluacion in (:estadosEvaluacion) AND _e.modulo.fechaInicio<= :lowIni AND _e.modulo.fechaFin>=:lowIni" +
				" AND _e.estadoRegistro= :estadoRegistro";
		Query q= entityManager.createQuery(hql);
		q.setParameter("estadosEvaluacion",estadosEvaluacion);
		q.setParameter("estadoRegistro", estadoRegistro.name());
		q.setParameter("lowIni", new Date());
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	private List<Modulo> getModulos(List<String> estadosModulo, EstadoRegistro estadoRegistro) {
		String hql = "SELECT _m FROM Modulo _m WHERE _m.estadoModulo in (:estadosModulo) AND _m.estadoRegistro= :estadoRegistro AND _m.fechaInicio<= :lowIni AND _m.fechaFin>=:lowIni";
		Query q = entityManager.createQuery(hql);
		q.setParameter("estadosModulo", estadosModulo);
		q.setParameter("estadoRegistro", estadoRegistro.name());
		q.setParameter("lowIni", new Date());
		return q.getResultList();
	}

}
