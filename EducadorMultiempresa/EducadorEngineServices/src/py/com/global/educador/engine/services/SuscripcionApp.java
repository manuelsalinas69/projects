package py.com.global.educador.engine.services;

import java.util.Set;
import java.util.HashSet;
import javax.ws.rs.core.Application;

public class SuscripcionApp extends Application {

	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();
	public SuscripcionApp(){
	     singletons.add(new Suscripcion());
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
