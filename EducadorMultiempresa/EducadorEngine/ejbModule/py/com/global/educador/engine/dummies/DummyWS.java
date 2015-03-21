package py.com.global.educador.engine.dummies;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.configuration.EducadorConstants;
import py.com.global.educador.engine.dto.QueueMessage;
import py.com.global.educador.engine.enums.TipoSuscripcion;
import py.com.global.educador.engine.utils.QueueManager;

@Stateless
@WebService(serviceName = "DummyWSService", endpointInterface = "py.com.global.educador.engine.dummies.DummyWSInterface")
public class DummyWS implements DummyWSInterface	 {

	Logger log= Logger.getLogger(DummyWS.class);
	

	QueueMessage message= new QueueMessage();
	
	public DummyWS(){
		
	}
	
	
	@WebMethod
	public void sendMessageIn(@WebParam String subscriberNumber, @WebParam String messageContent){
		log.info("sendMessageIn Execution --> subscriberNumber: "+subscriberNumber + ", Message:"+messageContent);
		message.addParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER, "606");
		message.addParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER, subscriberNumber);
		message.addParam(EducadorConstants.QueueMessageParamKey.SUBSCRIPTION_TYPE, TipoSuscripcion.AUTOMATICA.name());
		message.addParam(EducadorConstants.QueueMessageParamKey.MESSAGE, messageContent);
		QueueManager.sendObject(message, EducadorConstants.Queues.SMS_IN);
		QueueManager.closeQueueConn(EducadorConstants.Queues.SMS_IN);
	}
	
}
