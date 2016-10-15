package py.com.global.educador.engine.filters;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.utils.DefaultResponse;

@Provider
public class CorRequestFilter implements ContainerRequestFilter {

    private final static Logger log = Logger.getLogger( CorRequestFilter.class.getName() );

    @Override
    public void filter( ContainerRequestContext requestCtx ) throws IOException {
        log.debug( "Executing REST request filter" );

        // When HttpMethod comes as OPTIONS, just acknowledge that it accepts...
        if ( requestCtx.getRequest().getMethod().equals( "OPTIONS" ) ) {
            log.debug( "HTTP Method (OPTIONS) - Detected!" );

            // Just send a OK signal back to the browser
            requestCtx.abortWith( DefaultResponse.getACKResponse() );
        }
    }
}