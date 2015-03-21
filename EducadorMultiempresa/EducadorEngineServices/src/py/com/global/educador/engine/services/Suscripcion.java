package py.com.global.educador.engine.services;

import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.configuration.EducadorConstants;
import py.com.global.educador.engine.dto.QueueMessage;
import py.com.global.educador.engine.enums.TipoSuscripcion;
import py.com.global.educador.engine.utils.QueueManager;


@Path("/Suscripcion/{operacion}/{numeroCorto}/{numeroSuscriptor}")
public class Suscripcion {
QueueMessage message = new QueueMessage();
	
	Logger log = Logger.getLogger(Suscripcion.class);
	
	@GET()
	@Produces("text/plain")
	public String process(@PathParam("operacion") String operacion,
				@PathParam("numeroCorto")String numeroCorto, 
				@PathParam("numeroSuscriptor") String numeroSuscriptor) {
		
		switch (operacion.toLowerCase()) {
		case "alta":
		case "baja":
		case "educa":
		case "salir":
			message.addParam(
					EducadorConstants.QueueMessageParamKey.SHORT_NUMBER, numeroCorto);
			message.addParam(
					EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER,
					numeroSuscriptor);
			message.addParam(
					EducadorConstants.QueueMessageParamKey.SUBSCRIPTION_TYPE,
					TipoSuscripcion.AUTOMATICA.name());
			message.addParam(EducadorConstants.QueueMessageParamKey.MESSAGE,
					operacion);
			QueueManager.sendObject(message,
					EducadorConstants.Queues.SUBSCRIPTION_IN);
			QueueManager
					.closeQueueConn(EducadorConstants.Queues.SUBSCRIPTION_IN);
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
