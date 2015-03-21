package py.com.global.educador.engine.dispatchers;

import java.util.Date;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.configuration.EducadorConstants;
import py.com.global.educador.engine.configuration.EducadorConstants.QueueMessageParamKey;
import py.com.global.educador.engine.configuration.Educador_Constants;
import py.com.global.educador.engine.dto.QueueMessage;
import py.com.global.educador.engine.enums.EstadoEnvio;
import py.com.global.educador.engine.interfaces.SmsOutDispatcher;
import py.com.global.educador.engine.utils.QueueManager;
import py.com.global.educador.jpa.entity.LogSmsOut;
import py.com.global.educador.sms.manager.SenderManager;


@Stateless
@Asynchronous
public class SmsOutDispatcherImpl implements SmsOutDispatcher {

	@PersistenceContext(unitName="EducadorJpa")
	EntityManager entityManager;
	@EJB SenderManager senderManager;
	boolean dummy=false;

	Logger log=Logger.getLogger(SmsOutDispatcherImpl.class);

	@Asynchronous
	public void dispatch(QueueMessage message) {
		//LogSmsOut log= new LogSmsOut();
		log.info("Sending SMS to SenderManager--> "+message);
		//Random r=new Random(System.nanoTime());
		//long msgId= r.nextLong();
		LogSmsOut logSmsOut= new LogSmsOut();
		logSmsOut.setEstado(EstadoEnvio.PENDIENTE.name());
		logSmsOut.setFechaPeticionEnvio(new Date());
		logSmsOut.setMensaje((String) message.getParam(QueueMessageParamKey.MESSAGE));
		logSmsOut.setNumeroOrigen((String) message.getParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER));
		logSmsOut.setNumeroDestino((String) message.getParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER));
		//logSmsOut.setMessageId((long) msgId);
		entityManager.persist(logSmsOut);
		message.addParam(QueueMessageParamKey.LOG_ID, logSmsOut.getIdLogSmsOut());
		message.addParam(QueueMessageParamKey.MESSAGE_ID, logSmsOut.getIdLogSmsOut());
		if (dummy) {
			message.addParam(QueueMessageParamKey.COMMAND_STATUS, new Integer(0));
			
			QueueManager.sendObject(message, Educador_Constants.Queues.EJECUTION_UPDATER);
			QueueManager.closeQueueConn(Educador_Constants.Queues.EJECUTION_UPDATER);
		}
		else{
			senderManager.send(message);
		}
		
		
	}

	

}
