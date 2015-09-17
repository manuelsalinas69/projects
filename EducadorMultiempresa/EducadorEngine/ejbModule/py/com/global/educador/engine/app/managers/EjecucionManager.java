package py.com.global.educador.engine.app.managers;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import py.com.global.educador.engine.enums.EstadoEjecucionSuscriptor;
import py.com.global.educador.jpa.entity.EjecucionSuscriptor;
import py.com.global.educador.jpa.entity.Modulo;
import py.com.global.educador.jpa.entity.Suscriptor;

@Stateless
public class EjecucionManager {

	
	@PersistenceContext 
	EntityManager entityManager;
	
	public EjecucionSuscriptor nuevaFormulario(Long idModulo, Long idSuscriptor){
		EjecucionSuscriptor ejecucionNueva=null;
		try {
			ejecucionNueva= createEjecucionSubcriber0(idModulo, idSuscriptor, EstadoEjecucionSuscriptor.ACTIVO);
		} catch (Exception e) {
			System.out.println("AppServices.createEvaluacion(): "+e);
			e.printStackTrace();
		}
		return ejecucionNueva;
	}
	
	
	
	private EjecucionSuscriptor createEjecucionSubcriber0(Long moduloId,
			Long subsId, EstadoEjecucionSuscriptor estado) {
		EjecucionSuscriptor es= new EjecucionSuscriptor();
		es.setModulo(entityManager.find(Modulo.class, moduloId));
		es.setSuscriptor(entityManager.find(Suscriptor.class, subsId));
		es.setFechaAlta(new Date());
		es.setEstadoEjecucion(estado.name());
		entityManager.persist(es);
		return es;
	}
}
