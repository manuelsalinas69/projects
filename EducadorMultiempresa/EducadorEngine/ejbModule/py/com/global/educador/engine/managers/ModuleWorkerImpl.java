package py.com.global.educador.engine.managers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.TransactionTimeout;

import py.com.global.educador.engine.cache.SystemParameterCache;
import py.com.global.educador.engine.configuration.EducadorConstants;
import py.com.global.educador.engine.configuration.EducadorConstants.QueueMessageParamKey;
import py.com.global.educador.engine.configuration.EducadorConstants.SystemParameterKey;
import py.com.global.educador.engine.dto.QueueMessage;
import py.com.global.educador.engine.enums.EstadoSuscriptorModulo;
import py.com.global.educador.engine.enums.EstadoSuscriptorProyecto;
import py.com.global.educador.engine.enums.FiltroSuscriptor;
import py.com.global.educador.engine.enums.FlowProccessType;
import py.com.global.educador.engine.interfaces.ModuleWorker;
import py.com.global.educador.engine.utils.QueueManager;
import py.com.global.educador.jpa.entity.Modulo;
import py.com.global.educador.jpa.entity.Proyecto;
import py.com.global.educador.jpa.entity.Suscriptor;
@Stateless
public class ModuleWorkerImpl implements ModuleWorker{
	
	@PersistenceContext(unitName="EducadorJpa")
	EntityManager entityManager;
	
	@EJB SessionManager sessionManager;
	@EJB SystemParameterCache systemParameterCache;
	Logger log=Logger.getLogger(ModuleWorkerImpl.class);
	
	

	@Override
	@Asynchronous
	@TransactionTimeout(value=10,unit=TimeUnit.HOURS)
	public void process(Map<String, Object> params) {
		try {
			String flowType=systemParameterCache.getValue(SystemParameterKey.SYSTEM_ENGINE_PROCESS_FLOW_METHOD);

			long t1=System.currentTimeMillis();
			Long proyectoId=(Long) params.get(QueueMessageParamKey.PROJECT_ID);
			String shortNumber=(String) params.get(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER);
			Long moduloId=(Long)params.get(EducadorConstants.QueueMessageParamKey.MODULE_ID);
			//Long evaluacionId=(Long)params.get(EducadorConstants.QueueMessageParamKey.EVALUATION_ID);
			String type=(String) params.get(QueueMessageParamKey.TYPE);
			Modulo modulo=entityManager.find(Modulo.class, moduloId);
			//Proyecto p=entityManager.find(Proyecto.class, proyectoId);
			if (type!=null && type.equalsIgnoreCase("TIP")) {
				int result=setearModuloActualASuscriptores(proyectoId,moduloId,modulo.getFiltroSuscriptor());
				log.info("Cantidad de registros actualizados para el modulo ["+moduloId+"]---> "+result);
			}



			if (type!=null && type.equalsIgnoreCase("EVALUACION")) {
				if (flowType==null || flowType.trim().isEmpty() || flowType.equalsIgnoreCase(FlowProccessType.SIMPLE.name())) {
					log.info("El timer de evaluacion sera controlado por el mismo servicio del modulo. [params] -> "+params);
					return;
				}
			}


			log.debug("Executing ModuleWorker---> "+params);
			List<Suscriptor> l= getSubscribers(proyectoId,moduloId, shortNumber);
			if (l==null || l.isEmpty()) {
				log.warn("No hay suscriptores para el [moduloId="+moduloId+"]");
				return;
			}
			Map<String, Object> subsParam;
			for (Suscriptor suscriptor : l) {
				subsParam=new HashMap<String, Object>();
				subsParam.putAll(params);
				subsParam.put(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_ID, suscriptor.getIdSuscriptor());
				subsParam.put(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER, suscriptor.getNumero());
//				if (sessionManager.isSessionActive(suscriptor.getNumero(), shortNumber)) {
//					log.debug("Hay session activa para el numero----> "+suscriptor.getNumero());
//					continue;
//				}
				log.debug("Sending request---> "+subsParam);
				QueueMessage queueMessage= new QueueMessage(subsParam);
				QueueManager.sendObject(queueMessage, EducadorConstants.Queues.CLIENT_TASK_EVENT);
				QueueManager.closeQueueConn(EducadorConstants.Queues.CLIENT_TASK_EVENT);
				//enginePause();
			}
			long t2=System.currentTimeMillis();
			log.debug("t2-t1---> "+(t2-t1)+"ms.");
		} catch (Exception e) {
			log.error("loadAndSendModuleSubscribersTaskRequest", e);
		}

		
	}
	

//	private void enginePause() {
//		try {
//			long value= Long.parseLong(systemParameterCache.getValue("system.engine.process.module.delay"));
//			if(value>0)Thread.sleep(value);
//		} catch (Exception e) {
//			log.error(e);
//		}
//		
//		
//	}


	private int setearModuloActualASuscriptores(Long proyectoId, Long moduloId, String filter) {
		//FiltroSuscriptor filtroSuscriptor;
		if (filter==null) {
			//filtroSuscriptor=FiltroSuscriptor.ANY;
			return updateAnySubcribers(proyectoId, moduloId);

		}
		if (filter.startsWith(FiltroSuscriptor.ANY.name())) {
			return updateAnySubcribers(proyectoId, moduloId);
		}

		if (filter.startsWith(FiltroSuscriptor.NEWS.name())) {
			return updateNewSubcribers(proyectoId, moduloId);
		}

		if (filter.startsWith(FiltroSuscriptor.IN_STATE.name())) {
			String states=filter.substring(filter.indexOf(":")+1);
			String[] stat= states.split(",");
			List<String> l= Arrays.asList(stat);

			return updateSubscribersInStates(proyectoId, moduloId, l);
		}


		return 0;
	}

	private int updateNewSubcribers(Long proyectoId, Long moduloId){
		String hql="UPDATE SuscriptorProyecto _sp SET _sp.modulo= :modulo,_sp.estadoSuscriptorModulo=:estadoSuscriptorModulo " +
				" WHERE (_sp.modulo is null) AND _sp.proyecto= :proyecto " +
				" AND _sp.estadoSuscriptorProyecto= :estadoSuscriptorProyecto ";


		Query q=entityManager.createQuery(hql);
		q.setParameter("modulo", entityManager.find(Modulo.class, moduloId));
		q.setParameter("proyecto", entityManager.find(Proyecto.class, proyectoId));
		q.setParameter("estadoSuscriptorProyecto",EstadoSuscriptorProyecto.ACTIVO.name());
		q.setParameter("estadoSuscriptorModulo",EstadoSuscriptorModulo.ACTIVO.name());
		


		return q.executeUpdate();
	}

	private int updateAnySubcribers(Long proyectoId, Long moduloId){
		String hql="UPDATE SuscriptorProyecto _sp SET _sp.modulo= :modulo,_sp.estadoSuscriptorModulo=:estadoSuscriptorModulo " +
				" WHERE (_sp.modulo is null OR _sp.modulo!= :modulo) AND _sp.proyecto= :proyecto " +
				" AND _sp.estadoSuscriptorProyecto= :estadoSuscriptorProyecto ";


		Query q=entityManager.createQuery(hql);
		q.setParameter("modulo", entityManager.find(Modulo.class, moduloId));
		q.setParameter("proyecto", entityManager.find(Proyecto.class, proyectoId));
		q.setParameter("estadoSuscriptorProyecto",EstadoSuscriptorProyecto.ACTIVO.name());
		q.setParameter("estadoSuscriptorModulo",EstadoSuscriptorModulo.ACTIVO.name());
		


		return q.executeUpdate();
		
	}

	private int updateSubscribersInStates(Long proyectoId, Long moduloId, List<String> statesList){
		String hql="UPDATE SuscriptorProyecto _sp SET _sp.modulo= :modulo,_sp.estadoSuscriptorModulo=:estadoSuscriptorModulo " +
				" WHERE (_sp.modulo is null OR _sp.modulo!= :modulo) AND _sp.proyecto= :proyecto " +
				" AND _sp.estadoSuscriptorProyecto= :estadoSuscriptorProyecto AND (_sp.estadoSuscriptorModulo is null OR _sp.estadoSuscriptorModulo in (:l))";


		Query q=entityManager.createQuery(hql);
		q.setParameter("modulo", entityManager.find(Modulo.class, moduloId));
		q.setParameter("proyecto", entityManager.find(Proyecto.class, proyectoId));
		q.setParameter("estadoSuscriptorProyecto",EstadoSuscriptorProyecto.ACTIVO.name());
		q.setParameter("estadoSuscriptorModulo",EstadoSuscriptorModulo.ACTIVO.name());
		q.setParameter("l",statesList);


		return q.executeUpdate();

	}

//	private void setEjecucionesModulo(Modulo modulo,
//			EstadoEjecucionSuscriptor estado) {
//		String sql="UPDATE EJECUCION_SUSCRIPTOR SET ESTADO_EJECUCION= :estado,MOTIVO_CANCELACION= :motivo  WHERE ID_MODULO=:idModulo AND ESTADO_EJECUCION= :activo ";
//		Query q=entityManager.createNativeQuery(sql);
//		q.setParameter("estado", estado.name());
//		q.setParameter("idModulo", modulo.getIdModulo());
//		q.setParameter("motivo", MotivosCancelacion.MODULO_FINALIZADO.name());
//		q.setParameter("activo", EstadoEjecucionSuscriptor.ACTIVO.name());
//		int r=q.executeUpdate();
//		log.debug("Cantidad de ejecuciones canceladas para el [modulo="+modulo.getIdModulo()+"]---> "+r);
//
//
//
//	}

	@SuppressWarnings("unchecked")
	private List<Suscriptor> getSubscribers(Long proyectoId, Long moduloId, String numeroCorto) {
		String hql="SELECT _s.suscriptor FROM SuscriptorProyecto _s WHERE _s.id.idProyecto=:idProyecto " +
				" AND _s.estadoSuscriptorProyecto= :estadoSuscriptorProyecto " +
				" AND _s.estadoSuscriptorModulo= :estadoSuscriptorModulo " +
				" AND _s.modulo.idModulo= :idModulo AND _s.suscriptor.numero not in (SELECT _strx.numero FROM SessionTrx _strx WHERE _strx.numeroCorto= :numeroCorto)";
		Query q= entityManager.createQuery(hql);
		q.setParameter("idProyecto", proyectoId);
		q.setParameter("estadoSuscriptorProyecto", EstadoSuscriptorProyecto.ACTIVO.name());
		q.setParameter("estadoSuscriptorModulo", EstadoSuscriptorModulo.ACTIVO.name());
		q.setParameter("idModulo", moduloId);
		q.setParameter("numeroCorto", numeroCorto);
		return q.getResultList();
	}

}
