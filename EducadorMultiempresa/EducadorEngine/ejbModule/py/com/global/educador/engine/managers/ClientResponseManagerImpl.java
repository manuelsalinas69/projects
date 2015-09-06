package py.com.global.educador.engine.managers;

import java.util.Date;
import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.cache.SystemParameterCache;
import py.com.global.educador.engine.configuration.EducadorConstants;
import py.com.global.educador.engine.configuration.EducadorConstants.QueueMessageParamKey;
import py.com.global.educador.engine.configuration.EducadorConstants.SystemParameterKey;
import py.com.global.educador.engine.configuration.Educador_Constants;
import py.com.global.educador.engine.dto.QueueMessage;
import py.com.global.educador.engine.enums.EstadoEjecucionSuscriptor;
import py.com.global.educador.engine.enums.EstadoEjecucionSuscriptorDetalle;
import py.com.global.educador.engine.enums.EstadoEnvio;
import py.com.global.educador.engine.enums.EstadoEnvioSmsc;
import py.com.global.educador.engine.enums.EstadoEvaluacionSuscriptor;
import py.com.global.educador.engine.enums.EstadoRegistro;
import py.com.global.educador.engine.enums.EstadoSuscriptorModulo;
import py.com.global.educador.engine.enums.EstadoSuscriptorProyecto;
import py.com.global.educador.engine.interfaces.ClientResponseManager;
import py.com.global.educador.engine.interfaces.SubscriberStateUpdater;
import py.com.global.educador.engine.utils.QueueManager;
import py.com.global.educador.jpa.entity.EjecucionSuscriptor;
import py.com.global.educador.jpa.entity.EjecucionSuscriptorDetalle;
import py.com.global.educador.jpa.entity.EvaluacionSuscriptor;
import py.com.global.educador.jpa.entity.LogClientResponse;
import py.com.global.educador.jpa.entity.Respuesta;
import py.com.global.educador.jpa.entity.SuscriptorProyecto;
import py.com.global.educador.jpa.entity.SuscriptorProyectoPK;

@Stateless
public class ClientResponseManagerImpl implements ClientResponseManager{

	Logger log=Logger.getLogger(ClientResponseManagerImpl.class);
	
	@EJB SessionManager sessionManager;
	@EJB SubscriberStateUpdater subscriberStateUpdater;
	@EJB SystemParameterCache systemParameterCache;
	
	@PersistenceContext(unitName="EducadorJpa")
	EntityManager entityManager;
	
	@Asynchronous
	public void process(QueueMessage message) {
		String susNro=(String) message.getParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER);
		String msg=(String) message.getParam(QueueMessageParamKey.MESSAGE);
		String shortNumber=(String) message.getParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER);
		
		LogClientResponse responseLog= new LogClientResponse();
		responseLog.setFechaRecepcion(new Date());
		responseLog.setMensaje(msg);
		responseLog.setNumeroCorto(shortNumber);
		responseLog.setNumero(susNro);
		
		entityManager.persist(responseLog);
		
		if (sessionManager.isSessionActive(susNro, shortNumber)) {
			QueueMessage sessionParams=sessionManager.getSession(susNro, shortNumber);
			Long susId=(Long) sessionParams.getParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_ID);
			responseLog.setIdSuscriptor(susId);
			entityManager.merge(responseLog);
			Long evalSusId=(Long) sessionParams.getParam(QueueMessageParamKey.SUBSCRIBER_EVALUATION_ID);
			Long ejeDetId=(Long) sessionParams.getParam(QueueMessageParamKey.SUBSCRIBER_EXECUTION_DETAIL_ID);
			if (evalSusId==null) {
				log.error("NO se encontro el parametro [EVALUACION_SUSCRIPTOR_ID] para el [suscriptorId: "+susId+"- suscriptorNro: "+susNro+" ]");
				return;
			}
			if (ejeDetId==null) {
				log.error("NO se encontro el parametro [EJECUCION_SUSCRIPTOR_DETALLE_ID] para el [suscriptorId: "+susId+"- suscriptorNro: "+susNro+" ]");
				return;
			}
			
			processClientResponse(susId,susNro,evalSusId,ejeDetId,message);
			
		}
		else{
			log.warn("No se encontro la sesion para el [numero="+susNro+" ]");
		}
		
	}

	private void processClientResponse(Long susId, String susNro,Long evalSusId,Long ejeDetId, QueueMessage message) {
		EvaluacionSuscriptor evaluacionSuscriptor= entityManager.find(EvaluacionSuscriptor.class, evalSusId);
		Long idProyectoActivo=getProyecto((String) message.getParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER));
		Long idModulo=(Long) message.getParam(QueueMessageParamKey.MODULE_ID);
		String shortNumber=(String) message.getParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER);
		if (idProyectoActivo==null) {
			log.error("No se encontro el proyecto activo");
			return;
		}
		SuscriptorProyectoPK id=new SuscriptorProyectoPK();
		id.setIdProyecto(idProyectoActivo);
		id.setIdSuscriptor(susId);
		SuscriptorProyecto susPro= entityManager.find(SuscriptorProyecto.class, id);
		if (susPro==null) {
			log.error("No se encontro la suscripcion al proyecto activo suscriptor para el [suscriptorId="+susId+"] y el [proyectoId="+idProyectoActivo+"]");
			return;
		}
		
		if (!EstadoSuscriptorProyecto.ACTIVO.name().equalsIgnoreCase(susPro.getEstadoSuscriptorProyecto())) {
			log.error("No se encontro la suscripcion al proyecto activo suscriptor para el [suscriptorId="+susId+"] y el [proyectoId="+idProyectoActivo+"]");
			return;
		}
		if (evaluacionSuscriptor==null) {
			log.error("NO se ha encontrado la evaluacion correspondiente para el ID---> "+evalSusId+" para el [suscriptorId:"+susId+"- suscriptorNro: "+susNro+" ]");
			return;
		}
		evaluacionSuscriptor.setEstadoEnvio(EstadoEnvio.ENVIADO.name());
		entityManager.merge(evaluacionSuscriptor);
		
		EjecucionSuscriptorDetalle ejecucionSuscriptorDetalle=entityManager.find(EjecucionSuscriptorDetalle.class, ejeDetId);
		if (ejecucionSuscriptorDetalle==null) {
			log.error("NO se ha encontrado el detalle de ejecucion para el ID---> "+ejeDetId+" para el [suscriptorId:"+susId+"- suscriptorNro: "+susNro+" ]");
			return;
		}
		ejecucionSuscriptorDetalle.setEstadoEnvioSmsc(EstadoEnvioSmsc.RESPONDIDO_SMSC.name());
		entityManager.merge(ejecucionSuscriptorDetalle);
		String msg=(String) message.getParam(QueueMessageParamKey.MESSAGE);
		if (msg==null) {
			log.error("NO se recibio la respuesta. Recibido nulo en el parametro [MESSAGE] para el [suscriptor="+susNro+"][shortNumber="+message.getParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER)+"]  ");
			//sessionManager.updateSessionIniDate(susNro, shortNumber, new Date());
			QueueMessage notificationMessage=new QueueMessage();
			notificationMessage.addParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER, shortNumber);
			notificationMessage.addParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER, susNro);
			notificationMessage.addParam(QueueMessageParamKey.SESSION_REQUIRED, false);
			notificationMessage.addParam(QueueMessageParamKey.FORCE_SEND, true);
			notificationMessage.addParam(QueueMessageParamKey.MESSAGE, systemParameterCache.getValue(SystemParameterKey.SYSTEM_ENGINE_PROCESS_RESPONSE_ERRORS_UNKNOW,shortNumber));
			QueueManager.sendObject(notificationMessage, Educador_Constants.Queues.NOTIFICATION_REQUEST);
			QueueManager.closeQueueConn(Educador_Constants.Queues.NOTIFICATION_REQUEST);
			return;
		}
		if (msg.trim().isEmpty()) {
			//sessionManager.updateSessionIniDate(susNro, shortNumber, new Date());
			QueueMessage notificationMessage=new QueueMessage();
			notificationMessage.addParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER, shortNumber);
			notificationMessage.addParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER, susNro);
			notificationMessage.addParam(QueueMessageParamKey.SESSION_REQUIRED, false);
			notificationMessage.addParam(QueueMessageParamKey.FORCE_SEND, true);
			notificationMessage.addParam(QueueMessageParamKey.MESSAGE, systemParameterCache.getValue(SystemParameterKey.SYSTEM_ENGINE_PROCESS_RESPONSE_ERRORS_EMPTY,shortNumber));
			QueueManager.sendObject(notificationMessage, Educador_Constants.Queues.NOTIFICATION_REQUEST);
			QueueManager.closeQueueConn(Educador_Constants.Queues.NOTIFICATION_REQUEST);
			return;
			
		}
		if (evaluacionSuscriptor.getPregunta().getPreguntaAbierta()!=null && evaluacionSuscriptor.getPregunta().getPreguntaAbierta()) {
			evaluacionSuscriptor.setRespuestaAbierta(msg);
		}
		else{
			Respuesta respuesta= getRespuesta(evaluacionSuscriptor,msg);
			if (respuesta==null) {
				//sessionManager.updateSessionIniDate(susNro, shortNumber, new Date());
				QueueMessage notificationMessage=new QueueMessage();
				notificationMessage.addParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER, shortNumber);
				notificationMessage.addParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER, susNro);
				notificationMessage.addParam(QueueMessageParamKey.SESSION_REQUIRED, false);
				notificationMessage.addParam(QueueMessageParamKey.FORCE_SEND, true);
				notificationMessage.addParam(QueueMessageParamKey.MESSAGE,systemParameterCache.getValue(SystemParameterKey.SYSTEM_ENGINE_PROCESS_RESPONSE_ERRORS_MISMATCH,shortNumber));
				QueueManager.sendObject(notificationMessage, Educador_Constants.Queues.NOTIFICATION_REQUEST);
				QueueManager.closeQueueConn(Educador_Constants.Queues.NOTIFICATION_REQUEST);
				return;
			}
			
			evaluacionSuscriptor.setRespuesta(respuesta);
			evaluacionSuscriptor.setRespuestaCorrecta(respuesta.getEsRespuestaCorrecta());
		}
		
		evaluacionSuscriptor.setEstadoEvaluacion(EstadoEvaluacionSuscriptor.RESPONDIDO.name());
		evaluacionSuscriptor.setFechaRespuesta(new Date());
		entityManager.merge(evaluacionSuscriptor);
		sessionManager.removeSession(susNro, shortNumber);
		boolean isEnvioInmediato=EducadorConstants.Constants.ENVIO_INMEDIATO.equalsIgnoreCase(evaluacionSuscriptor.getEvaluacion().getConfiguracionEnvioModulo());
		
		if (evaluacionSuscriptor.getPregunta().getPreguntaFinal()!=null && evaluacionSuscriptor.getPregunta().getPreguntaFinal()) {
			ejecucionSuscriptorDetalle.setEstadoEjecucion(EstadoEjecucionSuscriptorDetalle.ENVIADO.name());
			
			if (ejecucionSuscriptorDetalle.getEnvioFinal()!=null && ejecucionSuscriptorDetalle.getEnvioFinal()) {
				EjecucionSuscriptor ejecucionSuscriptor=ejecucionSuscriptorDetalle.getEjecucionSuscriptor();
				ejecucionSuscriptor.setEstadoEjecucion(EstadoEjecucionSuscriptor.FINALIZADO.name());
				entityManager.merge(ejecucionSuscriptor);
				subscriberStateUpdater.updateSuscriptorModulo(idProyectoActivo, idModulo, susId,  EstadoSuscriptorModulo.FINALIZADO);
				sendFinalMsg(message);
			}
			entityManager.merge(ejecucionSuscriptorDetalle);
		}
		else{
			if (isEnvioInmediato) {
				sendNextPregunta(evaluacionSuscriptor);
			}
		}
		
		
		
		
	}

	private void sendFinalMsg(QueueMessage message) {
		String susNro=(String) message.getParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER);
		String shortNumber=(String) message.getParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER);
		QueueMessage notificationMessage=new QueueMessage();
		notificationMessage.addParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER, shortNumber);
		notificationMessage.addParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER, susNro);
		notificationMessage.addParam(QueueMessageParamKey.SESSION_REQUIRED, false);
		notificationMessage.addParam(QueueMessageParamKey.FORCE_SEND, true);
		notificationMessage.addParam(QueueMessageParamKey.MESSAGE, systemParameterCache.getValue(SystemParameterKey.SYSTEM_ENGINE_PROCESS_RESPONSE_SUCCESS_FINAL_MESSAGE, shortNumber));
		QueueManager.sendObject(notificationMessage, Educador_Constants.Queues.NOTIFICATION_REQUEST);
		QueueManager.closeQueueConn(Educador_Constants.Queues.NOTIFICATION_REQUEST);
		
	}

	private void sendNextPregunta(EvaluacionSuscriptor evaluacionSuscriptor) {
		QueueMessage queueMessage= new QueueMessage();
		queueMessage.addParam( EducadorConstants.QueueMessageParamKey.TYPE, "EVALUACION");
		queueMessage.addParam(EducadorConstants.QueueMessageParamKey.EVALUATION_ID, evaluacionSuscriptor.getEvaluacion().getIdEvaluacion());
		queueMessage.addParam(QueueMessageParamKey.PROJECT_ID,evaluacionSuscriptor.getEvaluacion().getModulo().getProyecto().getIdProyecto());
		queueMessage.addParam(EducadorConstants.QueueMessageParamKey.MODULE_ID, evaluacionSuscriptor.getEvaluacion().getModulo().getIdModulo());
		queueMessage.addParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER, evaluacionSuscriptor.getEvaluacion().getModulo().getProyecto().getNumeroCorto());
		queueMessage.addParam(QueueMessageParamKey.CONFIGURATION, evaluacionSuscriptor.getEvaluacion().getConfiguracionEnvioModulo());
		queueMessage.addParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_ID, evaluacionSuscriptor.getSuscriptor().getIdSuscriptor());
		queueMessage.addParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER, evaluacionSuscriptor.getSuscriptor().getNumero());
		QueueManager.sendObject(queueMessage, Educador_Constants.Queues.CLIENT_TASK_EVENT);
		QueueManager.closeQueueConn(Educador_Constants.Queues.CLIENT_TASK_EVENT);
		
	}

	@SuppressWarnings("unchecked")
	private Respuesta getRespuesta(EvaluacionSuscriptor evaluacionSuscriptor,String msg) {
		String hql="SELECT _r FROM Respuesta _r WHERE _r.pregunta= :pregunta AND lower(_r.valorEsperado)=lower(:valorEnviado)";
		Query q= entityManager.createQuery(hql);
		q.setParameter("pregunta", evaluacionSuscriptor.getPregunta());
		q.setParameter("valorEnviado", msg.trim());
		List<Respuesta> l=q.getResultList();
		if (l.isEmpty()) {
			return null;
		}
		
		return l.get(0);
	}

	@SuppressWarnings("unchecked")
	private Long getProyecto(String shortNumber) {
		String hql="SELECT _p.idProyecto FROM Proyecto _p WHERE _p.numeroCorto= :shortNumber AND _p.estadoRegistro= :estadoRegistro";
		
		Query q=entityManager.createQuery(hql);
		q.setParameter("shortNumber", shortNumber);
		q.setParameter("estadoRegistro", EstadoRegistro.ACTIVO.name());
		List<Long> l=q.getResultList();
		if (l==null || l.isEmpty()) {
			return null;
		}
		return l.get(0);
	}

}
