package py.com.global.educador.engine.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface SuscripcionEducadorWS {

	@WebMethod
	public int altaSuscripcion(@WebParam String numeroSuscriptor,@WebParam  String mensaje,@WebParam String numeroCorto);
	
	@WebMethod
	public int bajaSuscripcion(@WebParam String numeroSuscriptor,@WebParam  String mensaje,@WebParam String numeroCorto);

}
