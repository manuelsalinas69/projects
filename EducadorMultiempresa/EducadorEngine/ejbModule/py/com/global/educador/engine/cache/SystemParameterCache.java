package py.com.global.educador.engine.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.enums.EstadoRegistro;
import py.com.global.educador.jpa.entity.ParametroProyecto;
import py.com.global.educador.jpa.entity.ParametroSistema;
import py.com.global.educador.jpa.entity.Proyecto;

@Startup
@Singleton
public class SystemParameterCache {
	private Logger log = Logger.getLogger(SystemParameterCache.class);

	@PersistenceContext(unitName = "EducadorJpa")
	EntityManager entityManager;

	private Map<String, String> parameters = new HashMap<String, String>();
	private Map<Long,Map<String, String>> parametersPerProject= new HashMap<Long,Map<String, String>>();
	private Map<String,Map<String, String>> parametersPerShortNumber= new HashMap<String,Map<String, String>>();

	@PostConstruct
	public void init() {
		log.debug("Iniciando Singleton SystemParameterCache");
		populateCache();
		populateProjectsCache();
	}

	private void populateProjectsCache() {
		try {
			List<Proyecto> activeProjects= getActiveProjects();
			for (Proyecto proyecto : activeProjects) {
				Map<String, String> mapForProject= createMapForProject(proyecto);
				parametersPerProject.put(proyecto.getIdProyecto(), mapForProject);
				parametersPerShortNumber.put(proyecto.getNumeroCorto(), mapForProject);
			}
		} catch (Exception e) {
			log.error("Error al poblar los parametros de proyecto",e);
		}
		
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> createMapForProject(Proyecto proyecto) {
		List<ParametroProyecto> parametros;
		String hql="SELECT _p FROM ParametroProyecto _p WHERE _p.proyecto= :proyecto ";
		Query q=entityManager.createQuery(hql);
		q.setParameter("proyecto", proyecto);
		parametros=q.getResultList();
		Map<String, String> map= new HashMap<String, String>();
		for (ParametroProyecto _pp : parametros) {
			map.put(_pp.getId().getParametro(), _pp.getValor());
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	private List<Proyecto> getActiveProjects() {
		String hql="SELECT p FROM Proyecto p WHERE p.estadoRegistro= :estadoRegistro";
		Query q= entityManager.createQuery(hql);
		q.setParameter("estadoRegistro", EstadoRegistro.ACTIVO.name());
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public void populateCache() {

		try {
			List<ParametroSistema> parametrosList;
			String hql = "SELECT p FROM ParametroSistema p";
			Query q = entityManager.createQuery(hql);
			parametrosList = q.getResultList();
			if (parametrosList != null) {
				for (ParametroSistema parametro : parametrosList) {
					parameters.put(parametro.getParametro(),
							parametro.getValor());
				}
				log.debug("El cache de SystemParameter se pobló correctamente");
				log.debug(parameters.toString());
			}
		} catch (Exception e) {
			log.error("Error al poblar cache de SystemParameter "+e);
		}

	}

	@Lock(LockType.READ)
	public String getValue(String key) {
		if (key != null) {
			return parameters.get(key);
		}
		return null;
	}
	
	@Lock(LockType.READ)
	public String getValue(String key, String shortNumber) {
		if (key != null) {
			Map<String, String> m=parametersPerShortNumber.get(shortNumber);
			if (m!=null && m.containsKey(key)) {
				return m.get(key);
			}
			return parameters.get(key);
		}
		return null;
	}
	
	@Lock(LockType.READ)
	public String getValue(String key, Long idProject) {
		if (key != null) {
			Map<String, String> m =parametersPerProject.get(idProject);
			if (m!=null && m.containsKey(key)) {
				return m.get(key);
			}
			return parameters.get(key);
		}
		return null;
	}
	
	@Schedule(dayOfMonth = "*", hour = "*", minute = "*/1",persistent=false)
	public void reloadCache(){
		try {
			log.debug("Iniciando reload de cache de SystemParameter");
			parameters.clear();
			parametersPerProject.clear();
			parametersPerShortNumber.clear();
			populateCache();
			populateProjectsCache();
		} catch (Exception e) {
			log.error("Error al realizar el reload del cache de SystemParameter "+e);
		}
	}
	
}
