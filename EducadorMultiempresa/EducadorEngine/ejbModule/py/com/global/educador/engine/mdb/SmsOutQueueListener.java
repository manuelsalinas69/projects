package py.com.global.educador.engine.mdb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.configuration.EducadorConstants;
import py.com.global.educador.engine.configuration.EducadorConstants.QueueMessageParamKey;
import py.com.global.educador.engine.dto.QueueMessage;
import py.com.global.educador.engine.interfaces.SmsOutDispatcher;

/**
 * Message-Driven Bean implementation class for: SmsOutQueueListener
 */
@MessageDriven(
		activationConfig = { @ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Queue"), @ActivationConfigProperty(
						propertyName = "destination", propertyValue = EducadorConstants.Queues.SMS_OUT),
						@ActivationConfigProperty(propertyName="maxSession",propertyValue="150")
		})

public class SmsOutQueueListener implements MessageListener {

	@EJB SmsOutDispatcher smsOutDispatcher;

	Logger log=Logger.getLogger(SmsOutQueueListener.class);

	/**
	 * Default constructor. 
	 */
	public SmsOutQueueListener() {
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
					QueueMessage m= new QueueMessage();
					/*Parametros esperados
						QueueMessageParamKey.SUBSCRIBER_NUMBER
						QueueMessageParamKey.SHORT_NUMBER
						QueueMessageParamKey.SESSION_REQUIRED
						QueueMessageParamKey.FORCE_SEND
						QueueMessageParamKey.MESSAGE

						Si SESSION_REQUIRED=true
						QueueMessageParamKey.EVALUATION_ID
						QueueMessageParamKey.SUBSCRIBER_EXECUTION_DETAIL_ID
						QueueMessageParamKey.SUBSCRIBER_EVALUATION_ID
						QueueMessageParamKey.SUBSCRIBER_ID
						QueueMessageParamKey.TYPE
						QueueMessageParamKey.SUBSCRIBER_ID
						QueueMessageParamKey.TYPE
	
					 */

//					int deliveryCount=msg.getIntProperty("JMSXDeliveryCount");
//					if (deliveryCount>1) {
//						log.error("Mensage reenviado, no se procesa.");
//						return;
//					}

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


					smsOutDispatcher.dispatch(m);




				} catch (JMSException e) {
					log.error("Error reading message --> queue="
							+ EducadorConstants.Queues.SMS_OUT, e);
				}
			}
		} catch (Exception e) {
			log.error(
					"Reading queue --> queue=" + EducadorConstants.Queues.SMS_OUT,
					e);
			return;
		}

	}

}
