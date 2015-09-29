package py.com.global.educador.engine.managers;

import java.util.Calendar;
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
import py.com.global.educador.engine.enums.EstadoSuscriptorModulo;
import py.com.global.educador.engine.enums.FlowProccessType;
import py.com.global.educador.engine.interfaces.ClientFlowManager;
import py.com.global.educador.engine.interfaces.SubscriberStateUpdater;
import py.com.global.educador.engine.utils.QueueManager;
import py.com.global.educador.jpa.entity.EjecucionSuscriptor;
import py.com.global.educador.jpa.entity.EjecucionSuscriptorDetalle;
import py.com.global.educador.jpa.entity.Evaluacion;
import py.com.global.educador.jpa.entity.EvaluacionSuscriptor;
import py.com.global.educador.jpa.entity.Modulo;
import py.com.global.educador.jpa.entity.PlanificacionEnvio;
import py.com.global.educador.jpa.entity.Pregunta;
import py.com.global.educador.jpa.entity.Respuesta;
import py.com.global.educador.jpa.entity.Suscriptor;

@Stateless
public class ClientFlowManagerImpl implements ClientFlowManager{

	@PersistenceContext(unitName="EducadorJpa")
	EntityManager entityManager;
	@EJB SessionManager sessionManager;
	@EJB SubscriberStateUpdater subscriberStateUpdater;
	@EJB SystemParameterCache systemParameterCache;

	String defaultFinalText=".Envie la respuesta correcta";

	Logger log=Logger.getLogger(ClientFlowManagerImpl.class);

	Long MAX_INTENTOS=2L;

	/**
	 * Description Algoritmo
	 * 
	 * se obtiene la ejecucion del suscriptor para el modulo (ACTIVO), en caso que no exista, se crea.
	 * luego se obtiene el detalle actual de ejecucion para ese modulo y se compara contra el tipo que 
	 * que viene en la solicitud, si no es igual no se hace nada
	 * si es igual, se verifica que sea 
	 * 
	 * */
	@Asynchronous
	public void process(QueueMessage message) {

		if (!checkParamsOk(message)) {
			log.error("Uno o mas parametros no fueron establecidos correctamente, por favor, verfique mas arriba");
			return;
		}

		String tipo=(String) message.getParam(EducadorConstants.QueueMessageParamKey.TYPE);
		Long subsId=(Long) message.getParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_ID);
		Long projectId= (Long) message.getParam(EducadorConstants.QueueMessageParamKey.PROJECT_ID);
		Long moduloId=(Long) message.getParam(EducadorConstants.QueueMessageParamKey.MODULE_ID);

		EjecucionSuscriptor ejecucionSuscriptor=getEjecucionSuscriptor(moduloId,subsId);
		if (ejecucionSuscriptor==null) {
			log.error("No se pudo crear o recuperar la ejecucion para el [suscriptor="+subsId+"] en el [modulo="+moduloId+"]");
			return;
		}
		if (EstadoEjecucionSuscriptor.CANCELADO.name().equalsIgnoreCase(ejecucionSuscriptor.getEstadoEjecucion())) {
			log.error("Se paso el registro de un suscritor que ya tiene cancelado la evaluacion al [modulo="+moduloId+"]");

			subscriberStateUpdater.updateSuscriptorModulo(projectId, moduloId, subsId, EstadoSuscriptorModulo.CANCELADO);

			return;
		}
		if (EstadoEjecucionSuscriptor.FINALIZADO.name().equalsIgnoreCase(ejecucionSuscriptor.getEstadoEjecucion())) {
			//log.info("El suscriptor ya finalizo sus ejecuciones.");
			return;
		}
		
		EjecucionSuscriptorDetalle detalleEjecucion= getDetalleEjecucion(ejecucionSuscriptor);
		if (detalleEjecucion==null) {
			log.error("No se pudo recuperar el detalle de ejecucion actual para el [suscriptor="+subsId+"] para el [modulo="+moduloId+"]");
			return;
		}
		String flowMethod= systemParameterCache.getValue(SystemParameterKey.SYSTEM_ENGINE_PROCESS_FLOW_METHOD);

		if (EstadoEnvioSmsc.PENDIENTE_RESPUESTA.name().equalsIgnoreCase(detalleEjecucion.getEstadoEnvioSmsc())) {
			log.warn("Hay una ejecucion actual pendiente de respuesta del sms para el suscriptor--->"+subsId);
			Long intento=detalleEjecucion.getIntentoEnvioSinRespuesta();
			if (intento!=null && intento>=getMaxIntentosSinRespuesta()) {
				log.warn("Ya se ha llegado a la maxima cantidad de espera para una respuesta del SMSC para el suscriptor---> "+subsId);
				detalleEjecucion.setEstadoEjecucion(EstadoEjecucionSuscriptorDetalle.FALLIDO.name());
				detalleEjecucion.setEstadoEnvioSmsc(EstadoEnvioSmsc.NO_RESPONDIDO_SMSC.name());
				detalleEjecucion.setRespuestaSmsc(EstadoEnvioSmsc.NO_RESPONDIDO_SMSC.name());
				entityManager.merge(detalleEjecucion);
				if ("EVALUACION".equalsIgnoreCase(detalleEjecucion.getEnviar())) {
					EvaluacionSuscriptor es=getCurrentEvaluacionSuscriptor(detalleEjecucion);
					if (es!=null) {
						es.setEstadoEnvio(EstadoEnvio.FALLIDO.name());
						es.setEstadoEvaluacion(EstadoEvaluacionSuscriptor.FALLIDO.name());
						es.setRespuestaSenderSmsc(EstadoEnvioSmsc.NO_RESPONDIDO_SMSC.name());
						entityManager.merge(es);
					}
				}
				
				process(message);
				return;
			}
			else{
				detalleEjecucion.setIntentoEnvioSinRespuesta(intento==null?1:++intento);
				entityManager.merge(detalleEjecucion);
				return;
			}
			
			
		}
		if (EstadoEnvioSmsc.RESPONDIDO_SMSC.name().equalsIgnoreCase(detalleEjecucion.getEstadoEnvioSmsc())) {
			if (detalleEjecucion.getFechaEnvio()==null || !"Command_Status[int=0; hex=0x0]".contains(detalleEjecucion.getRespuestaSmsc().trim())) {//quiere decir que fallo el envio
				Long intentoFallido=detalleEjecucion.getIntentoEnvioConRespuesta();
				if (intentoFallido!=null && intentoFallido>=getMaxIntentosConRespuesta()) {
					detalleEjecucion.setEstadoEjecucion(EstadoEjecucionSuscriptorDetalle.FALLIDO.name());
					entityManager.merge(detalleEjecucion);
					if ("EVALUACION".equalsIgnoreCase(detalleEjecucion.getEnviar())) {
						EvaluacionSuscriptor es=getCurrentEvaluacionSuscriptor(detalleEjecucion);
						if (es!=null) {
							es.setEstadoEnvio(EstadoEnvio.FALLIDO.name());
							es.setEstadoEvaluacion(EstadoEvaluacionSuscriptor.FALLIDO.name());
							es.setRespuestaSenderSmsc(detalleEjecucion.getEstadoEnvioSmsc());
							entityManager.merge(es);
						}
					}
					
					process(message);
					return;
				}
				else{
					detalleEjecucion.setIntentoEnvioConRespuesta(intentoFallido==null?1:++intentoFallido);
					entityManager.merge(detalleEjecucion);
					
				}
//				
				
			}
		}
		
		if (flowMethod==null || flowMethod.trim().isEmpty() || flowMethod.trim().equalsIgnoreCase(FlowProccessType.SIMPLE.name())) {
			message.addParam(QueueMessageParamKey.TYPE, detalleEjecucion.getEnviar());
			sendIgnoreParams(detalleEjecucion, message);
			return;
		}

		if (tipo.equalsIgnoreCase(detalleEjecucion.getEnviar())) {
			send(detalleEjecucion, message);
		}

	}

	private Long getMaxIntentosSinRespuesta() {
		try {
			return Long.parseLong(systemParameterCache.getValue(SystemParameterKey.SYSTEM_ENGINE_PROCESS_FLOW_ATTEMPS_SMSC_NOREPLY));
		} catch (Exception e) {
			log.error(e);
		}
		return 2l;//default 2
	}
	
	private Long getMaxIntentosConRespuesta() {
		try {
			return Long.parseLong(systemParameterCache.getValue(SystemParameterKey.SYSTEM_ENGINE_PROCESS_FLOW_ATTEMPS_SMSC_REPLYERROR));
		} catch (Exception e) {
			log.error(e);
		}
		return 2l;//default 2
	}

	private void sendIgnoreParams(EjecucionSuscriptorDetalle detalleEjecucion,
			QueueMessage message) {
		if ("EVALUACION".equalsIgnoreCase(detalleEjecucion.getEnviar())) {
			message.addParam(QueueMessageParamKey.EVALUATION_ID, detalleEjecucion.getEvaluacion().getIdEvaluacion());
			sendEvaluacion(detalleEjecucion,message);

		}
		else if("TIP".equalsIgnoreCase(detalleEjecucion.getEnviar())){
			sendTip(detalleEjecucion,message);
		}

	}

	private void send(EjecucionSuscriptorDetalle detalleEjecucion, QueueMessage message){
		Long subsId=(Long) message.getParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_ID);
		Long moduloId=(Long) message.getParam(EducadorConstants.QueueMessageParamKey.MODULE_ID);
		if ("EVALUACION".equalsIgnoreCase(detalleEjecucion.getEnviar())) {
			Long evaluacionId=(Long) message.getParam(EducadorConstants.QueueMessageParamKey.EVALUATION_ID);
			if (evaluacionId==null) {
				log.error("No se recibio el id de evaluacion para la ejecucion para el [suscriptor="+subsId+"] para el [modulo="+moduloId+"]");
				return;
			}
			if (evaluacionId.longValue()==detalleEjecucion.getEvaluacion().getIdEvaluacion()) {
				sendEvaluacion(detalleEjecucion,message);
			}
		}
		else if("TIP".equalsIgnoreCase(detalleEjecucion.getEnviar())){
			sendTip(detalleEjecucion,message);
		}
	}

	

	private boolean checkParamsOk(QueueMessage message) {

		String tipo=(String) message.getParam( EducadorConstants.QueueMessageParamKey.TYPE);
		Long subsId=(Long) message.getParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_ID);
		String susNro=(String) message.getParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER);
		Long moduloId=(Long) message.getParam(EducadorConstants.QueueMessageParamKey.MODULE_ID);

		if (tipo==null || tipo.trim().isEmpty()) {
			log.error("No se encontro el parametro [TIPO], se descarta el pedido");
			return false;
		}

		if (subsId==null) {
			log.error("No se encontro el parametro [SUSCRIPTOR_ID], se descarta el pedido");
			return false;
		}
		if (susNro==null) {
			log.error("No se encontro el parametro [SUSCRIPTOR_NRO], se descarta el pedido");
			return false;
		}
		if (moduloId==null) {
			log.error("No se encontro el parametro [MODULO_ID], se descarta el pedido");
			return false;
		}



		return true;
	}

	private void sendTip(EjecucionSuscriptorDetalle detalleEjecucion, QueueMessage message) {
		log.debug("Enviando [TIP: "+detalleEjecucion.getTip().getContenido()+"] al [suscriptor_id: "+
				detalleEjecucion.getEjecucionSuscriptor().getSuscriptor().getIdSuscriptor()+
				"; suscriptor_nro"+detalleEjecucion.getEjecucionSuscriptor().getSuscriptor().getNumero() +"]");
		if (tiempoEsperaExpirado(detalleEjecucion)) {
			log.debug("Enviando TIP..");
			String msg=detalleEjecucion.getTip().getContenido();
			detalleEjecucion.setEstadoEnvioSmsc(EstadoEnvioSmsc.PENDIENTE_RESPUESTA.name());
			detalleEjecucion.setFechaEjecucion(new Date());
			Long intento=detalleEjecucion.getIntentoEnvioSinRespuesta();
			intento=intento==null?1:++intento;
			detalleEjecucion.setIntentoEnvioSinRespuesta(intento);
			entityManager.merge(detalleEjecucion);
			log.debug(msg);
			message.addParam("SESSION_REQUIRED", Boolean.FALSE); //No requiere este parametro, por ende tampoco los otros para la sesion
			//message.addParam(EducadorConstants.QueueMessageParamKey.EVALUATION_ID, es.getEvaluacion().getIdEvaluacion());
			message.addParam(QueueMessageParamKey.SUBSCRIBER_EXECUTION_DETAIL_ID, detalleEjecucion.getIdEjecucionDetalle());
			//message.addParam(QueueMessageParamKey.SUBSCRIBER_EVALUATION_ID, es.getIdEvaluacionSuscriptor());
			message.addParam("MESSAGE", msg);
			QueueManager.sendObject(message, EducadorConstants.Queues.NOTIFICATION_REQUEST);
			QueueManager.closeQueueConn(EducadorConstants.Queues.NOTIFICATION_REQUEST);


		}
		else{
			log.debug("Aun se debe esperar tiempo para el envio del TIP ");
		}

	}

	private void sendEvaluacion(EjecucionSuscriptorDetalle detalleEjecucion, QueueMessage message) {
		log.debug("Enviando [Evaluacion: "+detalleEjecucion.getEvaluacion().getIdEvaluacion()+"] al [suscriptor_id: "+
				detalleEjecucion.getEjecucionSuscriptor().getSuscriptor().getIdSuscriptor()+
				"; suscriptor_nro"+detalleEjecucion.getEjecucionSuscriptor().getSuscriptor().getNumero() +"]");

		if (tiempoEsperaExpirado(detalleEjecucion)) {
			sendEvaluacion0(detalleEjecucion,message);
		}
		else{
			log.debug("Aun se debe esperar tiempo para la ejecucion de la evaluacion ");
		}




	}

	private void sendEvaluacion0(EjecucionSuscriptorDetalle detalleEjecucion,
			QueueMessage message) {
		EvaluacionSuscriptor es=getCurrentEvaluacionSuscriptor(detalleEjecucion);
		if (es==null) {
			log.error("No se pudo encontrar la evaluacion del [suscriptor_id: "+detalleEjecucion.getEjecucionSuscriptor().getSuscriptor().getIdSuscriptor()+
					"; suscriptor_nro: "+detalleEjecucion.getEjecucionSuscriptor().getSuscriptor().getNumero()+"]");
			return;
		}
		if (EstadoEvaluacionSuscriptor.CANCELADO.name().equalsIgnoreCase(es.getEstadoEvaluacion())) {
			log.error("La evaluacion actual del [suscriptor_id: "+detalleEjecucion.getEjecucionSuscriptor().getSuscriptor().getIdSuscriptor()+
					"; suscriptor_nro: "+detalleEjecucion.getEjecucionSuscriptor().getSuscriptor().getNumero()+"] esta en estado CANCELADO");
			return;
		}

		/*
		 * EN vez de cancelar la ejecucion, ponemos a no respondido la evaluacion y seguimos con el siguiente detalle de ejecucion
		 * */
		if (es.getIntento()!=null && es.getIntento()>=MAX_INTENTOS) {
			es.setEstadoEvaluacion(EstadoEvaluacionSuscriptor.NO_RESPONDIDO.name());
			detalleEjecucion.setEstadoEjecucion(EstadoEjecucionSuscriptorDetalle.ENVIADO.name());
			//detalleEjecucion.setFechaEnvio(new Date());
			entityManager.merge(es);
			entityManager.merge(detalleEjecucion);
			log.info("Reprocesando el pedido de envio para--> "+message);
			process(message);//Reprocesar pedido

			return;

		}

		String msg=getMessageToSend(es.getPregunta());
		if (msg==null || msg.trim().isEmpty()) {
			log.error("No se pudo enviar el mensaje al [suscriptor_id: "+detalleEjecucion.getEjecucionSuscriptor().getSuscriptor().getIdSuscriptor()+
					"; suscriptor_nro: "+detalleEjecucion.getEjecucionSuscriptor().getSuscriptor().getNumero()+"] , el mensaje esperado para enviar en nulo o vacio" );
			return;
		}
		detalleEjecucion.setEstadoEnvioSmsc(EstadoEnvioSmsc.PENDIENTE_RESPUESTA.name());
		Long intento=detalleEjecucion.getIntentoEnvioSinRespuesta();
		intento=intento==null?1:++intento;
		detalleEjecucion.setIntentoEnvioSinRespuesta(intento);
		detalleEjecucion.setFechaEjecucion(new Date());
		entityManager.merge(detalleEjecucion);
		
		log.debug("Enviando pedido de envio de Mensaje al Notification Manager...");
		log.debug(msg);
		
		message.addParam(QueueMessageParamKey.SESSION_REQUIRED, Boolean.TRUE);
		message.addParam(EducadorConstants.QueueMessageParamKey.EVALUATION_ID, es.getEvaluacion().getIdEvaluacion());
		message.addParam(QueueMessageParamKey.SUBSCRIBER_EXECUTION_DETAIL_ID, detalleEjecucion.getIdEjecucionDetalle());
		message.addParam(QueueMessageParamKey.SUBSCRIBER_EVALUATION_ID, es.getIdEvaluacionSuscriptor());
		//		message.addParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER, getShortNumber());
//		String shortNumber=(String) message.getParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER);
//		message.addParam(QueueMessageParamKey.MESSAGE, msg+getSuffix(shortNumber));//FIXME: agregar
		message.addParam(QueueMessageParamKey.MESSAGE, msg);//FIXME: agregar
		QueueManager.sendObject(message, EducadorConstants.Queues.NOTIFICATION_REQUEST);
		QueueManager.closeQueueConn(EducadorConstants.Queues.NOTIFICATION_REQUEST);
		

	}

//	private String getSuffix(String shortNumber) {
//		try {
//			String value=systemParameterCache.getValue(SystemParameterKey.SYSTEM_ENGINE_PROCESS_FLOW_EVALUATION_MESSAGES_SUFFIX,shortNumber );
//			if (value==null) {
//				return defaultFinalText;
//			}
//			return value;
//		} catch (Exception e) {
//			log.error(e);
//		}
//		return defaultFinalText;
//	}

	private boolean tiempoEsperaExpirado(
			EjecucionSuscriptorDetalle detalleEjecucion) {
		if (detalleEjecucion.getEsperarHorasAnterior()==null && detalleEjecucion.getEsperarMinutosAnterior()==null) {//solo si ambos son nulos
			return true;
		}
		EjecucionSuscriptorDetalle detAnterior=getDetallerAnterior(detalleEjecucion);
		if (detAnterior==null || detAnterior.getFechaEjecucion()==null) {
			return true;
		}
		Calendar c= Calendar.getInstance();
		c.setTime(detAnterior.getFechaEjecucion());
		if (detalleEjecucion.getEsperarHorasAnterior()!=null) {
			c.add(Calendar.HOUR_OF_DAY, detalleEjecucion.getEsperarHorasAnterior().intValue());
		}
		if (detalleEjecucion.getEsperarMinutosAnterior()!=null) {
			c.add(Calendar.MINUTE, detalleEjecucion.getEsperarMinutosAnterior().intValue());
		}
		return  (new Date()).after(c.getTime());
	}

	@SuppressWarnings("unchecked")
	private EjecucionSuscriptorDetalle getDetallerAnterior(
			EjecucionSuscriptorDetalle detalleEjecucion) {
		String hql="SELECT _ed FROM EjecucionSuscriptorDetalle _ed WHERE _ed.ejecucionSuscriptor =:ejeSuc " +
				" AND _ed.orden= :orden ";
		Query q= entityManager.createQuery(hql);
		q.setParameter("ejeSuc", detalleEjecucion.getEjecucionSuscriptor());
		q.setParameter("orden", detalleEjecucion.getOrden()-1);
		List<EjecucionSuscriptorDetalle> l=q.getResultList();
		if (l.isEmpty()) {
			return null;
		}
		return l.get(0);
	}

	@SuppressWarnings("unchecked")
	private String getMessageToSend(Pregunta pregunta) {
		StringBuilder st= new StringBuilder(pregunta.getContenidoPregunta()+"\n");
		if (pregunta.getPreguntaAbierta()!=null && pregunta.getPreguntaAbierta()) {
			return st.toString();
		}
		String hql="SELECT _r FROM Respuesta _r WHERE _r.pregunta= :pregunta ORDER BY _r.ordenRespuesta ";
		List<Respuesta>l;
		Query q= entityManager.createQuery(hql);
		q.setParameter("pregunta", pregunta);
		l=q.getResultList();
		if (l.isEmpty()) {
			return null;
		}

		
		for (Respuesta respuesta : l) {
			st.append(respuesta.getOrdenRespuesta()+")"+ respuesta.getContenidoRespuesta()+"\n");
		}
		return st.toString();
	}



	@SuppressWarnings("unchecked")
	private EvaluacionSuscriptor getCurrentEvaluacionSuscriptor(EjecucionSuscriptorDetalle detalleEjecucion) {
		String hql="SELECT _es FROM EvaluacionSuscriptor _es WHERE _es.ejecucionSuscriptorDetalle= :detalleEjecucion  ";
		Query q=  entityManager.createQuery(hql);
		q.setParameter("detalleEjecucion", detalleEjecucion);
		q.setMaxResults(1);
		List<EvaluacionSuscriptor> l=q.getResultList();
		
		if (l.isEmpty()) {
			return crearEvaluacionSuscriptor(detalleEjecucion,1);
		}
		long orden=0;
		Boolean esFinal=false;
		EvaluacionSuscriptor _evs = l.get(0);
		
		
			orden=_evs.getPregunta().getOrdenPregunta();
			esFinal=_evs.getPregunta().getPreguntaFinal();
			if (EstadoEvaluacionSuscriptor.CANCELADO.name().equalsIgnoreCase(_evs.getEstadoEvaluacion())) {
				return null;
			}

			if (EstadoEvaluacionSuscriptor.ACTIVO.name().equalsIgnoreCase(_evs.getEstadoEvaluacion())) {
				return _evs;
			}
		
		//Si llegamos aca es porque no hay registros activos
		if (esFinal!=null && esFinal) {
			if (EstadoEvaluacionSuscriptor.RESPONDIDO.name().equalsIgnoreCase(_evs.getEstadoEvaluacion())) {
				detalleEjecucion.setEstadoEjecucion(EstadoEjecucionSuscriptorDetalle.ENVIADO.name());
			}
			else if(EstadoEvaluacionSuscriptor.CANCELADO.name().equalsIgnoreCase(_evs.getEstadoEvaluacion())){
				detalleEjecucion.setEstadoEjecucion(EstadoEjecucionSuscriptorDetalle.CANCELADO.name());
			}
			entityManager.merge(detalleEjecucion);

		}
		else{
			return crearEvaluacionSuscriptor(detalleEjecucion,orden+1);
		}

		return null;
	}

	private EvaluacionSuscriptor crearEvaluacionSuscriptor(
			EjecucionSuscriptorDetalle detalleEjecucion, long orden) {
		Pregunta _p=getPregunta(detalleEjecucion.getEvaluacion(),orden);
		if (_p==null) {
			log.error("No hay preguntas asociadas para la [evaluacion_id: "+detalleEjecucion.getEvaluacion().getIdEvaluacion()+"] y el [orden:"+orden+" ]");
			return null;
		}
		EvaluacionSuscriptor _evs= new EvaluacionSuscriptor();
		_evs.setEjecucionSuscriptorDetalle(detalleEjecucion);
		_evs.setEvaluacion(detalleEjecucion.getEvaluacion());
		_evs.setPregunta(_p);
		_evs.setSuscriptor(detalleEjecucion.getEjecucionSuscriptor().getSuscriptor());
		_evs.setEstadoEvaluacion(EstadoEvaluacionSuscriptor.ACTIVO.name());
		_evs.setFechaAlta(new Date());
		entityManager.persist(_evs);
		return _evs;
	}

	private Pregunta getPregunta(Evaluacion evaluacion, long orden) {
		String hql="SELECT _p FROM Pregunta _p WHERE _p.evaluacion= :evaluacion AND _p.ordenPregunta= :orden ";
		Query q= entityManager.createQuery(hql);
		q.setParameter("evaluacion", evaluacion);
		q.setParameter("orden", orden);
		return (Pregunta) q.getSingleResult();
	}

	private EjecucionSuscriptorDetalle getDetalleEjecucion(EjecucionSuscriptor es) {
		EjecucionSuscriptorDetalle det=getCurrentEjecucionDetalle(es);
		if (det==null) {
			log.error("No se pudo obtener el detalle de ejecucion del [suscriptor_id:"+es.getSuscriptor().getIdSuscriptor()+
					"; suscriptor_nro: "+es.getSuscriptor().getNumero()+"] " +
					"para el [modulo:"+es.getModulo().getIdModulo()+" ]");
			return null;
		}
		return det;
	}

	@SuppressWarnings("unchecked")
	private EjecucionSuscriptorDetalle getCurrentEjecucionDetalle(EjecucionSuscriptor es) {
		String hql="SELECT _ed FROM EjecucionSuscriptorDetalle _ed WHERE _ed.ejecucionSuscriptor=:es ORDER BY _ed.orden DESC";
		Query q=entityManager.createQuery(hql);
		q.setParameter("es", es);
		q.setMaxResults(1);
		List<EjecucionSuscriptorDetalle> l=q.getResultList();
		if (l.isEmpty()) {
			return crearEjecucionSuscriptorDetalle(es,1);
		}

		long orden=0;
		EjecucionSuscriptorDetalle _ed = l.get(0);
		Boolean isFinal=false;

		orden=_ed.getOrden();
		isFinal=_ed.getEnvioFinal();
		if (EstadoEjecucionSuscriptorDetalle.CANCELADO.name().equalsIgnoreCase(_ed.getEstadoEjecucion())) {
			log.error("La ejecucion del plan para el cliente fue cancelada");
			es.setEstadoEjecucion(EstadoEjecucionSuscriptor.CANCELADO.name());
			entityManager.merge(es);
			subscriberStateUpdater.updateSuscriptorModulo(es.getModulo().getProyecto().getIdProyecto(),es.getModulo().getIdModulo(),  es.getSuscriptor().getIdSuscriptor(), EstadoSuscriptorModulo.CANCELADO);

			return null;
		}
		if (EstadoEjecucionSuscriptor.ACTIVO.name().equalsIgnoreCase(_ed.getEstadoEjecucion())) {
			return _ed;
		}


		//Si llegamos aca es porque no se encontro activo

		if (isFinal!=null && isFinal) {
			//ya estamos en la ultima secuencia de ejecucion y no estamos en un estado valido.
			if (EstadoEjecucionSuscriptorDetalle.ENVIADO.name().equalsIgnoreCase(_ed.getEstadoEjecucion())) {
				es.setEstadoEjecucion(EstadoEjecucionSuscriptor.FINALIZADO.name());
				subscriberStateUpdater.updateSuscriptorModulo(es.getModulo().getProyecto().getIdProyecto(),es.getModulo().getIdModulo(),  es.getSuscriptor().getIdSuscriptor(), EstadoSuscriptorModulo.FINALIZADO);

				sendFinalMsg(es.getSuscriptor().getNumero(),es.getModulo().getProyecto().getNumeroCorto());
			}
			else{
				es.setEstadoEjecucion(EstadoEjecucionSuscriptor.CANCELADO.name());
				subscriberStateUpdater.updateSuscriptorModulo(es.getModulo().getProyecto().getIdProyecto(),es.getModulo().getIdModulo(),  es.getSuscriptor().getIdSuscriptor(), EstadoSuscriptorModulo.CANCELADO);

			}
			entityManager.merge(es);
		}
		else{//si estamos aca es porque no estamos en el final pero hay que crear el detalle 
			//de ejecucion para el siguiente orden planificado para el envio 
			return crearEjecucionSuscriptorDetalle(es,(orden+1));
		}


		return null;
	}

	private void sendFinalMsg(String susNro, String shortNumber) {
		QueueMessage notificationMessage=new QueueMessage();
		notificationMessage.addParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER, shortNumber);
		notificationMessage.addParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER, susNro);
		notificationMessage.addParam("SESSION_REQUIRED", false);
		notificationMessage.addParam("FORCE_SEND", true);
		notificationMessage.addParam("MESSAGE", systemParameterCache.getValue(SystemParameterKey.SYSTEM_ENGINE_PROCESS_RESPONSE_SUCCESS_FINAL_MESSAGE, shortNumber));
		QueueManager.sendObject(notificationMessage, Educador_Constants.Queues.NOTIFICATION_REQUEST);
		QueueManager.closeQueueConn(Educador_Constants.Queues.NOTIFICATION_REQUEST);

	}




	private EjecucionSuscriptorDetalle crearEjecucionSuscriptorDetalle(
			EjecucionSuscriptor es, long orden) {
		PlanificacionEnvio _pe=getPlanificacionEnvio(es.getModulo().getIdModulo(), orden);
		if (_pe==null) {
			log.error("No hay plan de envio para el [modulo="+es.getModulo().getIdModulo()+"] y [orden="+orden+"]");
			return null;
		}
		EjecucionSuscriptorDetalle _ed= new EjecucionSuscriptorDetalle();
		_ed.setEstadoEjecucion(EstadoEjecucionSuscriptorDetalle.ACTIVO.name());
		if (_pe.getEnviar().equalsIgnoreCase("TIP")) {
			_ed.setTip(_pe.getTip());
		}
		else if(_pe.getEnviar().equalsIgnoreCase("EVALUACION")){
			_ed.setEvaluacion(_pe.getEvaluacion());
		}
		_ed.setOrden(_pe.getOrden());
		_ed.setEjecucionSuscriptor(es);
		_ed.setEnvioFinal(_pe.getEnvioFinal());
		_ed.setEnviar(_pe.getEnviar());
		_ed.setEsperarHorasAnterior(_pe.getEsperarHorasAnterior());
		_ed.setEsperarMinutosAnterior(_pe.getEsperarMinutosAnterior());
		entityManager.persist(_ed);
		return _ed;
	}



	@SuppressWarnings("unchecked")
	private EjecucionSuscriptor getEjecucionSuscriptor(Long moduloId,
			Long subsId) {
		String hql= "SELECT _es FROM EjecucionSuscriptor _es " +
				"					WHERE _es.modulo.idModulo= :idModulo " +
				"						AND _es.suscriptor.idSuscriptor= :idSuscriptor " +
				" 	ORDER BY _es.fechaAlta DESC";
		//String[] a={EstadoEjecucionSuscriptor.ACTIVO.name()};
		Query q= entityManager.createQuery(hql);
		q.setMaxResults(1);
		q.setParameter("idModulo", moduloId);
		q.setParameter("idSuscriptor", subsId);
		//q.setParameter("estadosActivos", Arrays.asList(a));
		List<EjecucionSuscriptor> l=q.getResultList();
		if (l.isEmpty()) {
			return createEjecucionSubcriber(moduloId,subsId,EstadoEjecucionSuscriptor.ACTIVO);
		}

		return l.get(0);

	}

	private EjecucionSuscriptor createEjecucionSubcriber(Long moduloId,
			Long subsId, EstadoEjecucionSuscriptor estado) {
		EjecucionSuscriptor es= new EjecucionSuscriptor();
		es.setModulo(entityManager.find(Modulo.class, moduloId));
		es.setSuscriptor(entityManager.find(Suscriptor.class, subsId));
		es.setFechaAlta(new Date());
		es.setEstadoEjecucion(estado.name());
		entityManager.persist(es);
		return es;
	}

	private PlanificacionEnvio getPlanificacionEnvio(Long moduloId, Long orden) {
		try {
			String hql="SELECT _pe FROM  PlanificacionEnvio _pe WHERE _pe.modulo.idModulo= :idModulo AND _pe.orden= :orden ";
			Query q=entityManager.createQuery(hql);
			q.setParameter("idModulo", moduloId);
			q.setParameter("orden", orden);
			return (PlanificacionEnvio) q.getSingleResult();
		} catch (Exception e) {
			log.error(e);
		}
		return null;

	}
	
	



}
