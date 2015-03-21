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
import py.com.global.educador.engine.interfaces.SubscriptionInDispatcher;

/**
 * Message-Driven Bean implementation class for: SuscriptionInQueueListener
 */
@MessageDriven(
		activationConfig = { @ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Queue"), @ActivationConfigProperty(
				propertyName = "destination", propertyValue = EducadorConstants.Queues.SUBSCRIPTION_IN),
				@ActivationConfigProperty(propertyName="maxSession",propertyValue="150")
		})
public class SubscriptionInQueueListener implements MessageListener {

	@EJB SubscriptionInDispatcher subscriptionInDispatcher;
	
	Logger log=Logger.getLogger(SubscriptionInQueueListener.class);
	
    /**
     * Default constructor. 
     */
    public SubscriptionInQueueListener() {
        // TODO Auto-generated constructor stub
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
						//Parametros esperados
						//QueueMessageParamKey.SUBSCRIBER_NUMBER
						//QueueMessageParamKey.SHORT_NUMBER
						//QueueMessageParamKey.MESSAGE
						//QueueMessageParamKey.SUBSCRIPTION_TYPE
//						int deliveryCount=msg.getIntProperty("JMSXDeliveryCount");
//						if (deliveryCount>1) {
//							log.error("Mensage reenviado, no se procesa.");
//							return;
//						}
						
						QueueMessage m=new QueueMessage();
						
						m.addParam(QueueMessageParamKey.SUBSCRIBER_NUMBER, msg.getStringProperty(QueueMessageParamKey.SUBSCRIBER_NUMBER));
						m.addParam(QueueMessageParamKey.SHORT_NUMBER, msg.getStringProperty(QueueMessageParamKey.SHORT_NUMBER));
						m.addParam(QueueMessageParamKey.MESSAGE, msg.getStringProperty(QueueMessageParamKey.MESSAGE));
						m.addParam(QueueMessageParamKey.SUBSCRIPTION_TYPE, msg.getStringProperty(QueueMessageParamKey.SUBSCRIPTION_TYPE));
						subscriptionInDispatcher.dispatch(m);

						
					 
					
				} catch (Exception e) {
					log.error("Error reading message --> queue="
							+ EducadorConstants.Queues.SUBSCRIPTION_IN, e);
				}
			}
		} catch (Exception e) {
			log.error(
					"Reading queue --> queue=" + EducadorConstants.Queues.SUBSCRIPTION_IN,
					e);
			return;
		}
        
    }

}
