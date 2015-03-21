package py.com.global.educador.engine.cache;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.log4j.Logger;

import py.com.global.educador.jpa.entity.Suscriptor;
import py.com.global.educador.jpa.entity.SuscriptorProyecto;
import py.com.global.educador.jpa.entity.SuscriptorProyectoPK;

@Singleton
@Startup
public class SubscriberCache {

	Map<String, Suscriptor> suscriptorMap;
	Map<SuscriptorProyectoPK, SuscriptorProyecto> suscriptorProyectoMap;
	
	Logger log=Logger.getLogger(SubscriberCache.class);
	
	@PostConstruct
	public void init(){
		suscriptorProyectoMap= new HashMap<SuscriptorProyectoPK, SuscriptorProyecto>();
		suscriptorMap= new HashMap<String,Suscriptor>();
	}

	@Lock(LockType.READ)
	public Suscriptor getSuscriptor(String numero){
		return suscriptorMap.get(numero);
	}
	
	@Lock(LockType.READ)
	public SuscriptorProyecto getSuscriptorProyecto(SuscriptorProyectoPK pk){
		return suscriptorProyectoMap.get(pk);
	}
	
	@Lock(LockType.WRITE)
	public void addToSuscriptorCache(String numero, Suscriptor suscriptor){
		if (numero==null || suscriptor==null || suscriptor.getIdSuscriptor()==0) {
			return;
		}
		suscriptorMap.put(numero, suscriptor);
	}
	
	@Lock(LockType.WRITE)
	public void addToSuscriptorProyectoCache(SuscriptorProyectoPK pk, SuscriptorProyecto suscriptorProyecto){
		if (pk==null || suscriptorProyecto==null) {
			return;
		}
		suscriptorProyectoMap.put(pk, suscriptorProyecto);
	}
	
	@Schedule(hour="*",minute="*/30")
	public void clearCache(){
		suscriptorMap.clear();
		suscriptorProyectoMap.clear();
	}
	
	
}
