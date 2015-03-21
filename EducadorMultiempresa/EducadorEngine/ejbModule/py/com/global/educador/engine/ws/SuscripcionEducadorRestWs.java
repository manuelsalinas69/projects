package py.com.global.educador.engine.ws;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.configuration.EducadorConstants;
import py.com.global.educador.engine.dto.QueueMessage;
import py.com.global.educador.engine.enums.TipoSuscripcion;
import py.com.global.educador.engine.utils.QueueManager;

@Path("/Suscripcion/{operacion}/{numeroCorto}/{numeroSuscriptor}")
@Stateless
public class SuscripcionEducadorRestWs {
	
	QueueMessage message = new QueueMessage();
	
	Logger log = Logger.getLogger(SuscripcionEducadorRestWs.class);

	@GET
	@Produces("text/plain")
	public String process(@PathParam("numeroCorto") String operacion,@PathParam("numeroSuscriptor") String numeroSuscriptor, @PathParam("numeroCorto") String numeroCorto){
		try {
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
			return "ok";
		} catch (Exception e) {
			log.error(e);
			return "failed";

		}

		
		
		
	}
}
