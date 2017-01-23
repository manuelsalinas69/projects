package py.com.global.educador.engine.filters;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;
import org.jboss.resteasy.plugins.server.servlet.HttpServletInputMessage;
import org.jboss.resteasy.spi.HttpRequest;

import py.com.global.educador.engine.app.services.AppServices;
import py.com.global.educador.engine.config.EducadorEngineServices.AppPath;

@Provider
public class AuthorizationRequestFilter implements ContainerRequestFilter{

	 private final static Logger log = Logger.getLogger( AuthorizationRequestFilter.class.getName() );
	
	 @EJB
	 AppServices appService;
	 
	@Override
	public void filter(ContainerRequestContext requestCtx) throws IOException {
		 log.debug( "Executing REST request filter" );
		 
		

	        // When HttpMethod comes as OPTIONS, just acknowledge that it accepts...
	        if ( requestCtx.getRequest().getMethod().equals( "OPTIONS" ) ) {
	            log.debug( "HTTP Method (OPTIONS) - Detected!" );

	            // Just send a OK signal back to the browser
	           // requestCtx.abortWith( Response.status( Response.Status.OK ).build() );
	            return;
	        }
	        
	        log.info("RequestPath-->"+ requestCtx.getUriInfo().getPath());
	       
	        if (AppPath.loginRootTree.equalsIgnoreCase(requestCtx.getUriInfo().getPath())) {
				log.debug("Pass to login method");
	        	return;
			}
	        
	        if (requestCtx.getHeaders().get("sessionId")==null) {
	        	log.error("[sessionId] no esta presente en la cabecera");
	        	requestCtx.abortWith(Response
	                      .status(Response.Status.UNAUTHORIZED)
	                      .entity("User cannot access the resource.")
	                      .build());
			}
		
	}

}
