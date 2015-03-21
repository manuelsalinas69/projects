package py.com.global.educador.engine.dispatchers;

import java.util.Date;
import java.util.regex.Matcher;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.jms.BytesMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.configuration.EducadorConstants;
import py.com.global.educador.engine.configuration.EducadorConstants.QueueMessageParamKey;
import py.com.global.educador.engine.configuration.Educador_Constants;
import py.com.global.educador.engine.dto.QueueMessage;
import py.com.global.educador.engine.enums.TipoSuscripcion;
import py.com.global.educador.engine.interfaces.SmsInDispatcher;
import py.com.global.educador.engine.utils.QueueManager;
import py.com.global.educador.jpa.entity.LogSmsIn;

@Stateless
public class SmsInDispatcherImpl implements SmsInDispatcher{

	Logger log=Logger.getLogger(SmsInDispatcherImpl.class);

	@PersistenceContext(unitName="EducadorJpa")
	EntityManager entityManager;
	
	
	String [] paterns={}; 
	
	@Asynchronous
	public void dispatch(QueueMessage message) {
		try {
			log.debug("Dispatch msg-->"+message);
//		String msg=(String) message.getParam(Educador_Constants.QueueMessageParamKey.MESSAGE);

			String msg=(String) message.getParam(QueueMessageParamKey.MESSAGE);
			if (msg==null) {
				msg="";
			}
			msg=msg.trim();
			LogSmsIn logSmsIn= new LogSmsIn();
			logSmsIn.setFechaRecepcion(new Date());
			logSmsIn.setMensaje(msg.trim());
			logSmsIn.setNumeroDestino((String) message.getParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER));
			logSmsIn.setNumeroOrigen((String) message.getParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER));
			//logSmsIn.setJmsMessageId((String) message.getParam(QueueMessageParamKey.JMS_MESSAGE_ID));
			entityManager.persist(logSmsIn);
			
			Matcher subscritionMatcher=Educador_Constants.Patterns.SUSCRIPTION_REQUEST_PATTERN.matcher(msg.trim());
			if (subscritionMatcher.matches()) {
				log.info("Sending message to SubscriberIn Queue--> "+message);
				message.addParam(QueueMessageParamKey.SUBSCRIPTION_TYPE,TipoSuscripcion.MANUAL.name());
				QueueManager.sendObject(message, Educador_Constants.Queues.SUBSCRIPTION_IN);
				QueueManager.closeQueueConn(Educador_Constants.Queues.SUBSCRIPTION_IN);
			}
			else{
				log.info("Sending message to ClientResponseIn Queue--> "+message);
				QueueManager.sendObject(message, Educador_Constants.Queues.CLIENT_RESPONSE_IN);
				QueueManager.closeQueueConn(Educador_Constants.Queues.CLIENT_RESPONSE_IN);
			}
		} catch (Exception e) {
			log.error("dispatch----> "+e,e);

		}
	}
	@Override
	public void dispatch(BytesMessage message) {
		try {
			log.debug("Dispatch msg-->"+message);
//		String msg=(String) message.getParam(Educador_Constants.QueueMessageParamKey.MESSAGE);

			String msg=(String) message.getObjectProperty(QueueMessageParamKey.MESSAGE);
			
			LogSmsIn logSmsIn= new LogSmsIn();
			logSmsIn.setFechaRecepcion(new Date());
			logSmsIn.setMensaje(msg);
			logSmsIn.setNumeroDestino((String) message.getObjectProperty(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER));
			logSmsIn.setNumeroOrigen((String) message.getObjectProperty(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER));
			//logSmsIn.setJmsMessageId((String) message.getParam(QueueMessageParamKey.JMS_MESSAGE_ID));
			entityManager.persist(logSmsIn);
			
			Matcher subscritionMatcher=Educador_Constants.Patterns.SUSCRIPTION_REQUEST_PATTERN.matcher(msg);
			if (subscritionMatcher.matches()) {
				log.info("Sending message to SubscriberIn Queue--> "+message);
				message.setObjectProperty(QueueMessageParamKey.SUBSCRIPTION_TYPE,TipoSuscripcion.MANUAL.name());
				QueueManager.sendObject(message, Educador_Constants.Queues.SUBSCRIPTION_IN);
				QueueManager.closeQueueConn(Educador_Constants.Queues.SUBSCRIPTION_IN);
			}
			else{
				log.info("Sending message to ClientResponseIn Queue--> "+message);
				QueueManager.sendObject(message, Educador_Constants.Queues.CLIENT_RESPONSE_IN);
				QueueManager.closeQueueConn(Educador_Constants.Queues.CLIENT_RESPONSE_IN);
			}
		} catch (Exception e) {
			log.error("dispatch----> "+e,e);

		}
		
	}

}
