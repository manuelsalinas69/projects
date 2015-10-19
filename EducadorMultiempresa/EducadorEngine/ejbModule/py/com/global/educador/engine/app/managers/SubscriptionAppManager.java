package py.com.global.educador.engine.app.managers;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import py.com.global.educador.engine.configuration.EducadorConstants.ErrorCode;
import py.com.global.educador.engine.dto.EducadorError;
import py.com.global.educador.engine.interfaces.SubscriptionProcessManager;

@Stateless
public class SubscriptionAppManager {

	@PersistenceContext(unitName="EducadorJpa")
	EntityManager entityManager;

	@EJB
	SubscriptionProcessManager subscriptionProcessManager;
	public Properties subscribe(Long idSuscriptor, Long idProyecto){
		Properties p= new Properties();
		
		try {
			EducadorError resultInfo = subscriptionProcessManager.addSubscriber(idSuscriptor, idProyecto);
			p.put("success", ErrorCode.SUCCESS.equalsIgnoreCase(resultInfo.getCode()));
		} catch (Exception e) {
			System.out.println("SubscriptionAppManager.subscribe(): "+e);
			e.printStackTrace();
			p.put("success", Boolean.FALSE);
			p.put("cause", e.getMessage());
		}
		return p;
	}

}
