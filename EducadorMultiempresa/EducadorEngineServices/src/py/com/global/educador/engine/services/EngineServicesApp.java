package py.com.global.educador.engine.services;

import java.util.Set;
import java.util.HashSet;
import javax.ws.rs.core.Application;

public class EngineServicesApp extends Application {

	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();
	public EngineServicesApp(){
	     singletons.add(new Suscripcion());
	     singletons.add(new UtilsServices());
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