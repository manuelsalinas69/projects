package py.com.global.educador.engine.managers;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.cache.SystemParameterCache;
import py.com.global.educador.engine.configuration.EducadorConstants.ErrorCode;
import py.com.global.educador.engine.configuration.EducadorConstants.QueueMessageParamKey;
import py.com.global.educador.engine.configuration.EducadorConstants.Queues;
import py.com.global.educador.engine.configuration.EducadorConstants.SystemParameterKey;
import py.com.global.educador.engine.dto.EducadorError;
import py.com.global.educador.engine.dto.QueueMessage;
import py.com.global.educador.engine.enums.TipoSuscripcion;
import py.com.global.educador.engine.interfaces.AltaServiceManager;
import py.com.global.educador.engine.interfaces.SubscriptionProcessManager;
import py.com.global.educador.engine.utils.QueueManager;

@Stateless
public class AltaServiceManagerImpl implements AltaServiceManager {

	Logger log = Logger.getLogger(AltaServiceManagerImpl.class);

	@EJB
	SubscriptionProcessManager subscriptionProcessManager;
	
	@EJB
	SystemParameterCache systemParameterCache;
	

	

	@Asynchronous
	 
	public void process(QueueMessage message) {
		EducadorError error;
		try {
			String typeSubscription=(String) message.getParam(QueueMessageParamKey.SUBSCRIPTION_TYPE);
			String shortNumber = (String) message
					.getParam(QueueMessageParamKey.SHORT_NUMBER); 
			if (typeSubscription==null) {
				typeSubscription=TipoSuscripcion.MANUAL.name();
			}
			if (message != null) {
				error = subscriptionProcessManager.addSubscriber(message);
				if (error != null) {
					switch (error.getCode()) {
					case ErrorCode.SUCCESS:
						QueueMessage m0= new QueueMessage(message.getAllParams());
						if (typeSubscription.equals(TipoSuscripcion.AUTOMATICA.name())){
							m0.addParam(
									QueueMessageParamKey.MESSAGE,
									systemParameterCache.getValue(SystemParameterKey.MESSAGE_FOR_SUCCESS_SUBSCRIPTION_AUTO, shortNumber));
						}else if (typeSubscription.equals(TipoSuscripcion.MANUAL.name())){
							m0.addParam(
									QueueMessageParamKey.MESSAGE,
									systemParameterCache.getValue(SystemParameterKey.MESSAGE_FOR_SUCCESS_SUBSCRIPTION_MANUAL, shortNumber));
						}
						
						m0.addParam(
								QueueMessageParamKey.SESSION_REQUIRED,
								false);
						QueueManager.sendObject(m0,
								Queues.NOTIFICATION_REQUEST);
					
						
						//Thread.sleep(2000);
						QueueMessage m1= new QueueMessage(message.getAllParams());
						m1.addParam(
								QueueMessageParamKey.SESSION_REQUIRED,
								false);
						String msg1=systemParameterCache.getValue(SystemParameterKey.MESSAGE_FOR_WELCOME_1, shortNumber);
						log.debug("Welcome 1 Message---> "+msg1);
						m1.addParam(QueueMessageParamKey.MESSAGE,msg1);
						log.debug("Sending Message---> "+m1);
						QueueManager.sendObject(m1,Queues.NOTIFICATION_REQUEST);
						
					
						

						//Thread.sleep(3000);
						QueueMessage m2= new QueueMessage(message.getAllParams());
						m2.addParam(
								QueueMessageParamKey.SESSION_REQUIRED,
								false);
						String msg2=systemParameterCache.getValue(SystemParameterKey.MESSAGE_FOR_WELCOME_2, shortNumber);
						log.debug("Welcome 2 Message---> "+msg2);
						m2.addParam(
								QueueMessageParamKey.MESSAGE,
								msg2);
						log.debug("Sending QueueMessage---> "+m2);
						QueueManager.sendObject(m2,
								Queues.NOTIFICATION_REQUEST);
					
						
						break;
					case ErrorCode.SUBSCRIBER_EXIST_ERROR:
						if (TipoSuscripcion.AUTOMATICA.name().equalsIgnoreCase(typeSubscription)) {
							return;
						}
						message.addParam(
								QueueMessageParamKey.MESSAGE,
								systemParameterCache.getValue(SystemParameterKey.MESSAGE_FOR_SUBSCRIBER_EXIST_ERROR, shortNumber));
						message.addParam(
								QueueMessageParamKey.SESSION_REQUIRED,
								false);
						QueueManager.sendObject(message,
								Queues.NOTIFICATION_REQUEST);
					
						break;
					default:
						break;
					}
					
				}

			}
		} catch (Exception e) {
			log.error(e);
		}
	

	}


	
		
	

}
