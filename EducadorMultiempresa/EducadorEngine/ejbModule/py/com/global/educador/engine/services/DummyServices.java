package py.com.global.educador.engine.services;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import py.com.global.educador.engine.configuration.EducadorConstants;
import py.com.global.educador.engine.configuration.Educador_Constants;
import py.com.global.educador.engine.dto.QueueMessage;
import py.com.global.educador.engine.interfaces.DummyServiceControl;
import py.com.global.educador.engine.utils.QueueManager;

@Stateless
@WebService
public class DummyServices implements DummyServiceControl {

	@Override
	@WebMethod
	public boolean sendMessage(@WebParam String number,@WebParam String message,@WebParam String shortNumber) {
		QueueMessage queueMessage= new QueueMessage();
		queueMessage.addParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER, number);
		queueMessage.addParam("MESSAGE", message);
		queueMessage.addParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER, shortNumber);
		
		QueueManager.sendObject(queueMessage, Educador_Constants.Queues.SMS_IN);
		QueueManager.closeQueueConn(Educador_Constants.Queues.SMS_IN);
		
		return true;
		
	}

}
