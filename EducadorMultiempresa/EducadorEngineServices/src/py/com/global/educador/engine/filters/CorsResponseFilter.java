package py.com.global.educador.engine.filters;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

@Provider
public class CorsResponseFilter implements ContainerResponseFilter {

	Logger log=Logger.getLogger(CorsResponseFilter.class);
	
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		log.info("Ejecutando filtro");
		System.out.println("Filtro la puta");  
		responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
	        responseContext.getHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
	        String reqHeader = requestContext.getHeaderString("Access-Control-Request-Headers");
	        if (reqHeader != null && reqHeader != "") {
	            responseContext.getHeaders().putSingle("Access-Control-Allow-Headers", reqHeader);
	        }
	    
	
	}

}
