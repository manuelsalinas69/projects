package py.com.global.educador.engine.ws;

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
@WebService(serviceName = "SuscripcionEducadorWS", endpointInterface = "py.com.global.educador.engine.ws.SuscripcionEducadorWS")
public class SuscripcionEducadorWSImpl implements SuscripcionEducadorWS {

	Logger log = Logger.getLogger(SuscripcionEducadorWSImpl.class);

	QueueMessage message = new QueueMessage();

	public SuscripcionEducadorWSImpl() {

	}

	@WebMethod
	public int altaSuscripcion(@WebParam String numeroSuscriptor,
			@WebParam String mensaje,@WebParam String numeroCorto) {

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
					mensaje);
			QueueManager.sendObject(message,
					EducadorConstants.Queues.SUBSCRIPTION_IN);
			QueueManager
					.closeQueueConn(EducadorConstants.Queues.SUBSCRIPTION_IN);
			return 0;
		} catch (Exception e) {
			log.error(e);
			return 1;

		}
	}

	@WebMethod
	public int bajaSuscripcion(@WebParam String numeroSuscriptor,
			@WebParam String mensaje, @WebParam String numeroCorto) {
		try {
			message.addParam(
					EducadorConstants.QueueMessageParamKey.SHORT_NUMBER,
					numeroCorto);
			message.addParam(
					EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER,
					numeroSuscriptor);
			message.addParam(
					EducadorConstants.QueueMessageParamKey.SUBSCRIPTION_TYPE,
					TipoSuscripcion.AUTOMATICA.name());
			message.addParam(EducadorConstants.QueueMessageParamKey.MESSAGE,
					mensaje);
			QueueManager.sendObject(message,
					EducadorConstants.Queues.SUBSCRIPTION_IN);
			QueueManager
					.closeQueueConn(EducadorConstants.Queues.SUBSCRIPTION_IN);
			return 0;
		} catch (Exception e) {
			log.error(e);
			return 1;

		}

	}

}
