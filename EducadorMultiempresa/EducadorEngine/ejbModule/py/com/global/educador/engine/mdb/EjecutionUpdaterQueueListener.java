package py.com.global.educador.engine.mdb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.configuration.EducadorConstants.QueueMessageParamKey;
import py.com.global.educador.engine.configuration.Educador_Constants;
import py.com.global.educador.engine.dto.QueueMessage;
import py.com.global.educador.engine.interfaces.EjecucionUpdater;

/**
 * Message-Driven Bean implementation class for: EjecutionUpdaterQueueListener
 */
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = Educador_Constants.Queues.EJECUTION_UPDATER) ,
		@ActivationConfigProperty(propertyName="maxSession",propertyValue="150")
})

public class EjecutionUpdaterQueueListener implements MessageListener {

	@EJB
	EjecucionUpdater ejecucionUpdater;
	Logger log = Logger.getLogger(EjecutionUpdaterQueueListener.class);

	/**
	 * Default constructor.
	 */

	public EjecutionUpdaterQueueListener() {
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
//					int deliveryCount=msg.getIntProperty("JMSXDeliveryCount");
//					if (deliveryCount>1) {
//						log.error("Mensage reenviado, no se procesa.");
//						return;
//					}
					
					QueueMessage m= new QueueMessage();
					/*Parametros esperados
						QueueMessageParamKey.SUBSCRIBER_NUMBER
						QueueMessageParamKey.SHORT_NUMBER
						QueueMessageParamKey.MESSAGE
						QueueMessageParamKey.LOG_ID
						QueueMessageParamKey.EVALUATION_ID
						QueueMessageParamKey.SUBSCRIBER_EXECUTION_DETAIL_ID
						QueueMessageParamKey.SUBSCRIBER_EVALUATION_ID
						QueueMessageParamKey.SUBSCRIBER_ID
						QueueMessageParamKey.COMMAND_STATUS
						QueueMessageParamKey.TYPE
						QueueMessageParamKey.PROJECT_ID
						QueueMessageParamKey.MODULE_ID
					 */

//					m.addParam(QueueMessageParamKey.SUBSCRIBER_NUMBER, msg.getStringProperty(QueueMessageParamKey.SUBSCRIBER_NUMBER));
//					m.addParam(QueueMessageParamKey.SHORT_NUMBER, msg.getStringProperty(QueueMessageParamKey.SHORT_NUMBER));
//					m.addParam(QueueMessageParamKey.MESSAGE, msg.getStringProperty(QueueMessageParamKey.MESSAGE));
//					m.addParam(QueueMessageParamKey.LOG_ID, msg.getObjectProperty(QueueMessageParamKey.LOG_ID));
//					m.addParam(QueueMessageParamKey.EVALUATION_ID, msg.getObjectProperty(QueueMessageParamKey.EVALUATION_ID));
//					m.addParam(QueueMessageParamKey.SUBSCRIBER_EXECUTION_DETAIL_ID, msg.getObjectProperty(QueueMessageParamKey.SUBSCRIBER_EXECUTION_DETAIL_ID));
//					m.addParam(QueueMessageParamKey.SUBSCRIBER_EVALUATION_ID, msg.getObjectProperty(QueueMessageParamKey.SUBSCRIBER_EVALUATION_ID));
//					m.addParam(QueueMessageParamKey.SUBSCRIBER_ID, msg.getObjectProperty(QueueMessageParamKey.SUBSCRIBER_ID));
					m.addParam(QueueMessageParamKey.COMMAND_STATUS, msg.getObjectProperty(QueueMessageParamKey.COMMAND_STATUS));
					m.addParam(QueueMessageParamKey.SEQ_NUM, msg.getObjectProperty(QueueMessageParamKey.SEQ_NUM));
//					m.addParam(QueueMessageParamKey.TYPE, msg.getStringProperty(QueueMessageParamKey.TYPE));
//					m.addParam(QueueMessageParamKey.PROJECT_ID, msg.getObjectProperty(QueueMessageParamKey.PROJECT_ID));
//					m.addParam(QueueMessageParamKey.MODULE_ID, msg.getObjectProperty(QueueMessageParamKey.MODULE_ID));



					ejecucionUpdater.process(m);



				} catch (JMSException e) {
					log.error("Error reading message --> queue="
							+ Educador_Constants.Queues.EJECUTION_UPDATER, e);
				}
			}
		} catch (Exception e) {
			log.error("Reading queue --> queue="
					+ Educador_Constants.Queues.EJECUTION_UPDATER, e);
			return;
		}
	}
}
