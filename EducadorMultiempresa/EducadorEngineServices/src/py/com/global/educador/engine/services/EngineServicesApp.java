package py.com.global.educador.engine.services;

import java.util.Set;
import java.util.HashSet;
import javax.ws.rs.core.Application;

import py.com.global.educador.engine.filters.AuthorizationRequestFilter;
import py.com.global.educador.engine.filters.CorRequestFilter;

public class EngineServicesApp extends Application {

	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();
	
	public EngineServicesApp(){
		
//	     singletons.add(new Suscripcion());
//	     singletons.add(new UtilsServices());
//	     singletons.add(new Services());
		getClasses().add(AuthorizationRequestFilter.class);
		getClasses().add(CorRequestFilter.class);
		getClasses().add(Suscripcion.class);
		getClasses().add(UtilsServices.class);
		getClasses().add(SurveyServices.class);
		getClasses().add(LoginServices.class);
	}
	@Override
	public Set<Class<?>> getClasses() {
	     return empty;
	}
	@Override
	public Set<Object> getSingletons() {
	     return singletons;
	}
}
