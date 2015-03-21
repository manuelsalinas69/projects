package py.com.global.educador.gui.services;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
@Stateless
public class TestServices2 {

	@WebMethod
	public String saludarNombre(@WebParam String nombre){
		System.out.println("Recibido--> "+nombre);
		return "Hola "+nombre;
	}
}
