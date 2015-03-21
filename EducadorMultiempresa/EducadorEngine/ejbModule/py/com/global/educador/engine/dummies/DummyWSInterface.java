package py.com.global.educador.engine.dummies;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface DummyWSInterface {

	@WebMethod
	public void sendMessageIn(@WebParam String subscriberNumber,@WebParam  String messageContent);
}
