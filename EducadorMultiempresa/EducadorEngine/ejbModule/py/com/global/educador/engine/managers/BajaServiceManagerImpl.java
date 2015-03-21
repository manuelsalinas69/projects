package py.com.global.educador.engine.managers;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.cache.SystemParameterCache;
import py.com.global.educador.engine.configuration.EducadorConstants;
import py.com.global.educador.engine.configuration.EducadorConstants.QueueMessageParamKey;
import py.com.global.educador.engine.configuration.EducadorConstants.Queues;
import py.com.global.educador.engine.dto.EducadorError;
import py.com.global.educador.engine.dto.QueueMessage;
import py.com.global.educador.engine.enums.TipoSuscripcion;
import py.com.global.educador.engine.interfaces.BajaServiceManager;
import py.com.global.educador.engine.interfaces.SubscriptionProcessManager;
import py.com.global.educador.engine.utils.QueueManager;


@Stateless
public class BajaServiceManagerImpl implements BajaServiceManager {

	Logger log=Logger.getLogger(BajaServiceManagerImpl.class);
	
	@EJB
	SubscriptionProcessManager subscriptionProcessManager;
	
	@EJB
	SystemParameterCache systemParameterCache;
	
	@Asynchronous
	 
	public void process(QueueMessage message) {
		
		EducadorError error;
		String typeUnsubscription=(String) message.getParam(EducadorConstants.QueueMessageParamKey.SUBSCRIPTION_TYPE);
		String shortNumber = (String) message
				.getParam(QueueMessageParamKey.SHORT_NUMBER); 
		if (typeUnsubscription==null) {
			typeUnsubscription=TipoSuscripcion.MANUAL.name();
		}
		boolean isErrorAndAuto=false;
		if (message != null) {
			error = subscriptionProcessManager.deleteSubscriber(message);
			if (error != null) {
				switch (error.getCode()) {
				case EducadorConstants.ErrorCode.SUCCESS:
					if (typeUnsubscription.equals(TipoSuscripcion.AUTOMATICA.name())){
						message.addParam(
								EducadorConstants.QueueMessageParamKey.MESSAGE,
								systemParameterCache.getValue(EducadorConstants.SystemParameterKey.MESSAGE_FOR_SUCCESS_UNSUBSCRIPTION_AUTO,shortNumber));
						
					}else if (typeUnsubscription.equals(TipoSuscripcion.MANUAL.name())){
						message.addParam(
								EducadorConstants.QueueMessageParamKey.MESSAGE,
								systemParameterCache.getValue(EducadorConstants.SystemParameterKey.MESSAGE_FOR_SUCCESS_UNSUBSCRIPTION_MANUAL,shortNumber));
					}
					QueueManager.sendObject(message, Queues.BAJA_EVENT);
					QueueManager.closeQueueConn(Queues.BAJA_EVENT);
					break;
				case EducadorConstants.ErrorCode.SUBSCRIBER_DOES_NOT_EXIST_ERROR:
					isErrorAndAuto=TipoSuscripcion.AUTOMATICA.name().equalsIgnoreCase(typeUnsubscription);
					message.addParam(
							EducadorConstants.QueueMessageParamKey.MESSAGE,
							systemParameterCache.getValue(EducadorConstants.SystemParameterKey.MESSAGE_FOR_SUBSCRIBER_DOES_NOT_EXIST_ERROR,shortNumber));
					break;
				default:
					break;
				}
				if (isErrorAndAuto) {
					return;
				}
				message.addParam(
						EducadorConstants.QueueMessageParamKey.SESSION_REQUIRED,
						false);
				QueueManager.sendObject(message,
						EducadorConstants.Queues.NOTIFICATION_REQUEST);
				QueueManager
						.closeQueueConn(EducadorConstants.Queues.NOTIFICATION_REQUEST);
			}

		}
		
		
		
	}

	

}
