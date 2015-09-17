package py.com.global.educador.engine.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.configuration.EducadorConstants;
import py.com.global.educador.engine.configuration.EducadorConstants.QueueMessageParamKey;
import py.com.global.educador.engine.configuration.Educador_Constants;
import py.com.global.educador.engine.dto.QueueMessage;
import py.com.global.educador.engine.utils.QueueManager;


@Path("/Utils")
public class UtilsServices {
QueueMessage message = new QueueMessage();
	
	Logger log = Logger.getLogger(UtilsServices.class);
	@GET()
	@Produces("text/plain")
	public String homeMessages(){
		return "Texto";
	}
	
	@GET()
	@Path("notification/{operacion}/{numeroCorto}/{numeroSuscriptor}/{messagesText}")
	@Produces("text/plain")
	public String process(@PathParam("operacion") String operacion,
				@PathParam("numeroCorto")String numeroCorto, 
				@PathParam("numeroSuscriptor") String numeroSuscriptor,
				@PathParam("messagesText") String messagesParam) {
		
		switch (operacion.toLowerCase()) {
		case "sms":
			QueueMessage notificationMessage=new QueueMessage();
			notificationMessage.addParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER, numeroCorto);
			notificationMessage.addParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER, numeroSuscriptor);
			notificationMessage.addParam(QueueMessageParamKey.SESSION_REQUIRED, false);
			notificationMessage.addParam(QueueMessageParamKey.FORCE_SEND, true);
			notificationMessage.addParam(QueueMessageParamKey.MESSAGE, messagesParam);
			QueueManager.sendObject(notificationMessage, Educador_Constants.Queues.NOTIFICATION_REQUEST);
			QueueManager.closeQueueConn(Educador_Constants.Queues.NOTIFICATION_REQUEST);
			break;
		case "test":
			log.info("Just test restul");
			break;

		default:
			break;
		}
		
		return "Parametros---> "+operacion+" - "+numeroCorto+" - "+numeroSuscriptor;
	}
}
