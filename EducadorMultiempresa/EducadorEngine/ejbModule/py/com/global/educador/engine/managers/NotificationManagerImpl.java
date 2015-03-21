package py.com.global.educador.engine.managers;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.BytesMessage;
import javax.jms.JMSException;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.configuration.EducadorConstants;
import py.com.global.educador.engine.configuration.EducadorConstants.QueueMessageParamKey;
import py.com.global.educador.engine.dto.QueueMessage;
import py.com.global.educador.engine.utils.QueueManager;

@Stateless
public class NotificationManagerImpl implements py.com.global.educador.engine.interfaces.NotificationManager{

	
	Logger log=Logger.getLogger(NotificationManagerImpl.class);
	
	@EJB SessionManager sessionManager;
	
	@Asynchronous
	public void process(QueueMessage message){
		String susNro=(String)message.getParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER);
		//String susNro=(String)message.getParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER);
		String shortNro=(String) message.getParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER);
		Boolean sesionRequired=(Boolean) message.getParam(EducadorConstants.QueueMessageParamKey.SESSION_REQUIRED);//si es false o nulo no se crea una sesion, por ejemplo para los tips
		Boolean forceSend=(Boolean) message.getParam(EducadorConstants.QueueMessageParamKey.FORCE_SEND);
		if (forceSend==null) {
			forceSend=false;
		}
		String msg=(String) message.getParam(QueueMessageParamKey.MESSAGE);
		
		if (sesionRequired==null) {
			log.error("No se encontro el parametro [SESSION_REQUIRED], sera descartada la notificacion");
			return;
		}
		
		if (susNro==null || susNro.trim().isEmpty()) {
			log.error("No se encontro el parametro [SUSCRIPTOR_NRO], sera descartada la notificacion");
			return;
		}
		
		if (shortNro==null) {
			log.error("No se encontro el parametro [SHORT_NUMBER], sera descartada la notificacion");
			return;
		}
		if (msg==null || msg.trim().isEmpty()) {
			log.error("No se encontro el parametro [MESSAGE], sera descartada la notificacion");
			return;
		}
		
//		if (sessionManager.isSessionActive(susNro, shortNro) && !forceSend) {
//			log.warn("No se puede enviar notificacion al [suscriptor:"+susNro+"], existe una sesion activa para el mismo");
//			return;
//		}
		
		if (sesionRequired) {
			
			if (sessionManager.createSession(susNro,shortNro, message)) {
				sendNotification(susNro,message);
			}
			else{
				log.error("No se pudo crear la sesion para el nro del [suscriptor: "+susNro+"] [params: "+message+"], el mensaje NO fue enviado, verifique los detalles " +
						"del error mas arriba");
			}
		}
		else{
			sendNotification(susNro,message);
		}
		
		
		
	}

	private void sendNotification(String susNro, QueueMessage message) {
		
		log.info("Sendind notification request to SMS OUT QUEUE");
		QueueManager.sendObject(message, EducadorConstants.Queues.SMS_OUT);
		QueueManager.closeQueueConn( EducadorConstants.Queues.SMS_OUT);
	}
	
	private void sendNotification(String susNro, BytesMessage message) {
		
		log.info("Sendind notification request to SMS OUT QUEUE");
		QueueManager.sendObject(message, EducadorConstants.Queues.SMS_OUT);
		QueueManager.closeQueueConn( EducadorConstants.Queues.SMS_OUT);
	}

	@Override
	public void process(BytesMessage message) {
		try {
			String susNro=(String)message.getObjectProperty(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER);
			//String susNro=(String)message.getParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER);
			String shortNro=(String) message.getObjectProperty(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER);
			Boolean sesionRequired=(Boolean) message.getObjectProperty(EducadorConstants.QueueMessageParamKey.SESSION_REQUIRED);//si es false o nulo no se crea una sesion, por ejemplo para los tips
			Boolean forceSend=(Boolean) message.getObjectProperty(EducadorConstants.QueueMessageParamKey.FORCE_SEND);
			if (forceSend==null) {
				forceSend=false;
			}
			String msg=(String) message.getObjectProperty("MESSAGE");
			
			if (sesionRequired==null) {
				log.error("No se encontro el parametro [SESSION_REQUIRED], sera descartada la notificacion");
				return;
			}
			
			if (susNro==null || susNro.trim().isEmpty()) {
				log.error("No se encontro el parametro [SUSCRIPTOR_NRO], sera descartada la notificacion");
				return;
			}
			
			if (shortNro==null) {
				log.error("No se encontro el parametro [SHORT_NUMBER], sera descartada la notificacion");
				return;
			}
			if (msg==null || msg.trim().isEmpty()) {
				log.error("No se encontro el parametro [MESSAGE], sera descartada la notificacion");
				return;
			}
			
//			if (sessionManager.isSessionActive(susNro, shortNro) && !forceSend) {
//				log.warn("No se puede enviar notificacion al [suscriptor:"+susNro+"], existe una sesion activa para el mismo");
//				return;
//			}
//			
			if (sesionRequired) {
				
				if (sessionManager.createSession(susNro,shortNro, message)) {
					sendNotification(susNro,message);
				}
				else{
					log.error("No se pudo crear la sesion para el nro del [suscriptor: "+susNro+"] [params: "+message+"], el mensaje NO fue enviado, verifique los detalles " +
							"del error mas arriba");
				}
			}
			else{
				sendNotification(susNro,message);
			}
		} catch (JMSException e) {
			log.error("process",e);
		}
		
		
	}

}
