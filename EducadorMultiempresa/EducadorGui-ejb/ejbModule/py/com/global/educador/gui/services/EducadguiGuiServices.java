package py.com.global.educador.gui.services;

import java.util.Random;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
@Stateless
public class EducadguiGuiServices {

	
	@WebMethod
	public String holaMundo(@WebParam(name="nombre") String param1){
		//String [] rs={"ok","error_data","error_server","error_external_services"};
		return param1;
	}
	
	public static void main(String[] args) {
		Random r= new Random(System.currentTimeMillis());
		for (int i = 0; i < 10; i++) {
			System.out.println(r.nextInt(6));
		}
		
	}
}
