package py.com.global.educador.engine.managers;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.enums.EstadoSuscriptorModulo;
import py.com.global.educador.engine.interfaces.SubscriberStateUpdater;
import py.com.global.educador.jpa.entity.SuscriptorProyecto;
import py.com.global.educador.jpa.entity.SuscriptorProyectoPK;
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SubscriberStateUpdaterImpl implements SubscriberStateUpdater{

	@PersistenceContext(unitName="EducadorJpa")
	EntityManager entityManager;
	
	Logger log=Logger.getLogger(SubscriberStateUpdaterImpl.class);
	
	
	@Override
	public void updateSuscriptorModulo(Long idProyecto, Long idModulo,
			Long idSuscriptor, EstadoSuscriptorModulo estadoSuscriptorModulo) {
		SuscriptorProyectoPK id= new SuscriptorProyectoPK();
		id.setIdProyecto(idProyecto);
		id.setIdSuscriptor(idSuscriptor);
		SuscriptorProyecto sp= entityManager.find(SuscriptorProyecto.class, id);
		sp.setEstadoSuscriptorModulo(estadoSuscriptorModulo.name());
		entityManager.merge(sp);
		
		log.info("Updating suscription result for module [suscriptorId="+idSuscriptor+"][module="+idModulo+"] to state ["+estadoSuscriptorModulo+"]");
		
	}

}
