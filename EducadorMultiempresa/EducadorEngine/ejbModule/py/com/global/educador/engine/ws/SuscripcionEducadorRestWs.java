package py.com.global.educador.engine.ws;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.dto.QueueMessage;

@Path("/test")
@Stateless
public class SuscripcionEducadorRestWs {
	
	QueueMessage message = new QueueMessage();
	
	Logger log = Logger.getLogger(SuscripcionEducadorRestWs.class);

	@GET
	@Produces("text/plain")
	public String process(){
		try {
			
			return "ok";
		} catch (Exception e) {
			log.error(e);
			return "failed";

		}

		
		
		
	}
}
