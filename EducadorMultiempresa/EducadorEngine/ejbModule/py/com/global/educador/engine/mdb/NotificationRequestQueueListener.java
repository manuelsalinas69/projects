package py.com.global.educador.engine.mdb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.BytesMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.configuration.EducadorConstants;
import py.com.global.educador.engine.configuration.EducadorConstants.QueueMessageParamKey;
import py.com.global.educador.engine.dto.QueueMessage;
import py.com.global.educador.engine.interfaces.NotificationManager;

/**
 * Message-Driven Bean implementation class for: NotificationRequestQueueListener
 */
@MessageDriven(
		activationConfig = { @ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Queue"), @ActivationConfigProperty(
				propertyName = "destination", propertyValue = EducadorConstants.Queues.NOTIFICATION_REQUEST),
				@ActivationConfigProperty(propertyName="maxSession",propertyValue="150")
		})
 
public class NotificationRequestQueueListener implements MessageListener {

	@EJB NotificationManager notificationManager;
	
	Logger log=Logger.getLogger(NotificationRequestQueueListener.class);
	
    /**
     * Default constructor. 
     */
    public NotificationRequestQueueListener() {
        
    }
	
	/**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message message) {
    	try {
		
			if (message != null && !message.getJMSRedelivered()
					&& message instanceof BytesMessage) {
				
				try {
					BytesMessage msg=(BytesMessage) message;
					
//					int deliveryCount=msg.getIntProperty("JMSXDeliveryCount");
//					if (deliveryCount>1) {
//						log.error("Mensage reenviado, no se procesa.");
//						return;
//					}
					
						QueueMessage m= new QueueMessage();
						/*Parametros esperados
							QueueMessageParamKey.SUBSCRIBER_NUMBER
							QueueMessageParamKey.SHORT_NUMBER
							QueueMessageParamKey.SESSION_REQUIRED
							QueueMessageParamKey.FORCE_SEND
							QueueMessageParamKey.MESSAGE
							QueueMessageParamKey.EVALUATION_ID
							QueueMessageParamKey.SUBSCRIBER_EXECUTION_DETAIL_ID
							QueueMessageParamKey.SUBSCRIBER_EVALUATION_ID
							QueueMessageParamKey.SUBSCRIBER_ID
							QueueMessageParamKey.TYPE
							QueueMessageParamKey.MODULE_ID
							QueueMessageParamKey.PROJECT_ID
						*/
						
						m.addParam(QueueMessageParamKey.SUBSCRIBER_NUMBER, msg.getStringProperty(QueueMessageParamKey.SUBSCRIBER_NUMBER));
						m.addParam(QueueMessageParamKey.SHORT_NUMBER, msg.getStringProperty(QueueMessageParamKey.SHORT_NUMBER));
						m.addParam(QueueMessageParamKey.SESSION_REQUIRED, msg.getObjectProperty(QueueMessageParamKey.SESSION_REQUIRED));
						m.addParam(QueueMessageParamKey.FORCE_SEND, msg.getObjectProperty(QueueMessageParamKey.FORCE_SEND));
						m.addParam(QueueMessageParamKey.MESSAGE, msg.getStringProperty(QueueMessageParamKey.MESSAGE));
						m.addParam(QueueMessageParamKey.TYPE, msg.getStringProperty(QueueMessageParamKey.TYPE));
						m.addParam(QueueMessageParamKey.EVALUATION_ID, msg.getObjectProperty(QueueMessageParamKey.EVALUATION_ID));
						m.addParam(QueueMessageParamKey.SUBSCRIBER_EXECUTION_DETAIL_ID, msg.getObjectProperty(QueueMessageParamKey.SUBSCRIBER_EXECUTION_DETAIL_ID));
						m.addParam(QueueMessageParamKey.SUBSCRIBER_EVALUATION_ID, msg.getObjectProperty(QueueMessageParamKey.SUBSCRIBER_EVALUATION_ID));
						m.addParam(QueueMessageParamKey.SUBSCRIBER_ID, msg.getObjectProperty(QueueMessageParamKey.SUBSCRIBER_ID));
						m.addParam(QueueMessageParamKey.MODULE_ID, msg.getObjectProperty(QueueMessageParamKey.MODULE_ID));
						m.addParam(QueueMessageParamKey.PROJECT_ID, msg.getObjectProperty(QueueMessageParamKey.PROJECT_ID));
							
						
						
						notificationManager.process(m);

						
					
					
				} catch (Exception e) {
					log.error("Error reading message --> queue="
							+ EducadorConstants.Queues.CLIENT_TASK_EVENT, e);
				}
			}
		} catch (Exception e) {
			log.error(
					"Reading queue --> queue=" + EducadorConstants.Queues.NOTIFICATION_REQUEST,
					e);
			return;
		}
        
        
    }

}
