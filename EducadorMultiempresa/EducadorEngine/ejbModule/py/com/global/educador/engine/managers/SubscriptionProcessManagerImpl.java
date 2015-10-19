package py.com.global.educador.engine.managers;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.configuration.EducadorConstants;
import py.com.global.educador.engine.dto.EducadorError;
import py.com.global.educador.engine.dto.QueueMessage;
import py.com.global.educador.engine.enums.EstadoSuscriptorProyecto;
import py.com.global.educador.engine.interfaces.SubscriptionProcessManager;
import py.com.global.educador.jpa.entity.LogSuscripcion;
import py.com.global.educador.jpa.entity.Proyecto;
import py.com.global.educador.jpa.entity.Suscriptor;
import py.com.global.educador.jpa.entity.SuscriptorProyecto;
import py.com.global.educador.jpa.entity.SuscriptorProyectoPK;

/**
 * Session Bean implementation class SubscriptionProcessManagerImpl
 */
@Stateless
public class SubscriptionProcessManagerImpl implements
SubscriptionProcessManager {

	Logger log = Logger.getLogger(SubscriptionProcessManagerImpl.class);

	@PersistenceContext
	EntityManager entityManager;
	//@EJB SubscriberCache subscriberCache;

	//Da de alta a un suscriptor
	public EducadorError addSubscriber(QueueMessage message) {
		EducadorError error = new EducadorError();
		String subscriberNumber = (String) message
				.getParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER);
		String shortNumber = (String) message
				.getParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER);

		Suscriptor subscriber = findSubscriber(subscriberNumber);
		Proyecto project = findProject(shortNumber);

		SuscriptorProyectoPK spPK;
		SuscriptorProyecto subscriberProject;

		// Si el suscriptor ya existe en base de datos
		if (subscriber != null && project != null) {
			spPK = new SuscriptorProyectoPK();
			spPK.setIdProyecto(project.getIdProyecto());
			spPK.setIdSuscriptor(subscriber.getIdSuscriptor());
			//subscriberProject=subscriberCache.getSuscriptorProyecto(spPK);
			//			if (subscriberProject==null) {
			//			}
			subscriberProject = entityManager.find(SuscriptorProyecto.class,
					spPK);
			if (subscriberProject != null) {
				if (subscriberProject.getEstadoSuscriptorProyecto().equals(
						EstadoSuscriptorProyecto.ACTIVO.name())) {
					error.setCode(EducadorConstants.ErrorCode.SUBSCRIBER_EXIST_ERROR);
					logSubscription((String)message.getParam(EducadorConstants.QueueMessageParamKey.MESSAGE), 
							(String)message.getParam(EducadorConstants.QueueMessageParamKey.OPERATION_TYPE),
							subscriberNumber, project.getNombre(), (String)message.getParam(EducadorConstants.QueueMessageParamKey.SUBSCRIPTION_TYPE),
							error.getCode(),(Date)message.getParam(EducadorConstants.QueueMessageParamKey.RECEPTION_DATE));
					return error;
				} else {
					subscriberProject
					.setEstadoSuscriptorProyecto(EstadoSuscriptorProyecto.ACTIVO
							.name());
					entityManager.merge(subscriberProject);
					//subscriberCache.addToSuscriptorProyectoCache(spPK, subscriberProject);
					//entityManager.flush();
					error.setCode(EducadorConstants.ErrorCode.SUCCESS);

					logSubscription((String)message.getParam(EducadorConstants.QueueMessageParamKey.MESSAGE), 
							(String)message.getParam(EducadorConstants.QueueMessageParamKey.OPERATION_TYPE),
							subscriberNumber, project.getNombre(), (String)message.getParam(EducadorConstants.QueueMessageParamKey.SUBSCRIPTION_TYPE),
							error.getCode(),(Date)message.getParam(EducadorConstants.QueueMessageParamKey.RECEPTION_DATE));
					return error;
				}
			}
		}

		// Si el suscriptor NO existe en base de datos
		if (subscriber == null && project != null) {
			subscriber = new Suscriptor();
			subscriber.setNumero(subscriberNumber);
			subscriber.setFechaAlta(new Date());
			subscriber
			.setTipoAlta((String) message
					.getParam(EducadorConstants.QueueMessageParamKey.SUBSCRIPTION_TYPE));
			entityManager.persist(subscriber);
			//subscriberCache.addToSuscriptorCache(subscriberNumber, subscriber);
			spPK = new SuscriptorProyectoPK();
			spPK.setIdProyecto(project.getIdProyecto());
			spPK.setIdSuscriptor(subscriber.getIdSuscriptor());

			subscriberProject = new SuscriptorProyecto();
			subscriberProject.setId(spPK);
			subscriberProject
			.setEstadoSuscriptorProyecto(EstadoSuscriptorProyecto.ACTIVO.name());
			entityManager.persist(subscriberProject);
			//subscriberCache.addToSuscriptorProyectoCache(spPK, subscriberProject);
			//entityManager.flush();
			error.setCode(EducadorConstants.ErrorCode.SUCCESS);
			logSubscription((String)message.getParam(EducadorConstants.QueueMessageParamKey.MESSAGE), 
					(String)message.getParam(EducadorConstants.QueueMessageParamKey.OPERATION_TYPE),
					subscriberNumber, project.getNombre(), (String)message.getParam(EducadorConstants.QueueMessageParamKey.SUBSCRIPTION_TYPE),
					error.getCode(),(Date)message.getParam(EducadorConstants.QueueMessageParamKey.RECEPTION_DATE));
			return error;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private Suscriptor findSubscriber(String subscriberNumber) {
		//		Suscriptor s=subscriberCache.getSuscriptor(subscriberNumber);
		//		if (s!=null) {
		//			return s;
		//		}

		String hql = "SELECT _s FROM Suscriptor _s WHERE _s.numero =:subscriberNumber";
		Query q = entityManager.createQuery(hql);
		q.setParameter("subscriberNumber", subscriberNumber);
		List<Suscriptor> subscriberList = q.getResultList();
		if (!subscriberList.isEmpty()) {
			return subscriberList.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private Proyecto findProject(String shortNumber) {

		String hql = "SELECT _p FROM Proyecto _p WHERE _p.numeroCorto =:shortNumber";
		Query q = entityManager.createQuery(hql);
		q.setParameter("shortNumber", shortNumber);
		List<Proyecto> projectList = q.getResultList();
		if (!projectList.isEmpty()) {
			return projectList.get(0);
		}
		return null;
	}

	//Da de baja al suscriptor
	public EducadorError deleteSubscriber(QueueMessage message) {
		EducadorError error = new EducadorError();
		String subscriberNumber = (String) message
				.getParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER);
		String shortNumber = (String) message
				.getParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER);
		Suscriptor subscriber = findSubscriber(subscriberNumber);
		Proyecto project = findProject(shortNumber);
		SuscriptorProyectoPK spPK;
		SuscriptorProyecto subscriberProject;
		if (subscriber != null && project != null) {
			spPK = new SuscriptorProyectoPK();
			spPK.setIdProyecto(project.getIdProyecto());
			spPK.setIdSuscriptor(subscriber.getIdSuscriptor());
			subscriberProject = entityManager.find(SuscriptorProyecto.class,
					spPK);
			if (subscriberProject != null) {
				subscriberProject
				.setEstadoSuscriptorProyecto(EstadoSuscriptorProyecto.INACTIVO
						.name());
				entityManager.merge(subscriberProject);
				//entityManager.flush();
				//subscriberCache.addToSuscriptorProyectoCache(spPK, subscriberProject);
				error.setCode(EducadorConstants.ErrorCode.SUCCESS);
				logSubscription((String)message.getParam(EducadorConstants.QueueMessageParamKey.MESSAGE), 
						(String)message.getParam(EducadorConstants.QueueMessageParamKey.OPERATION_TYPE),
						subscriberNumber, project.getNombre(), (String)message.getParam(EducadorConstants.QueueMessageParamKey.SUBSCRIPTION_TYPE),
						error.getCode(),(Date)message.getParam(EducadorConstants.QueueMessageParamKey.RECEPTION_DATE));
				return error;
			}
		}
		if (subscriber == null) {
			error.setCode(EducadorConstants.ErrorCode.SUBSCRIBER_DOES_NOT_EXIST_ERROR);
			logSubscription((String)message.getParam(EducadorConstants.QueueMessageParamKey.MESSAGE), 
					(String)message.getParam(EducadorConstants.QueueMessageParamKey.OPERATION_TYPE),
					subscriberNumber, project.getNombre(), (String)message.getParam(EducadorConstants.QueueMessageParamKey.SUBSCRIPTION_TYPE),
					error.getCode(),(Date)message.getParam(EducadorConstants.QueueMessageParamKey.RECEPTION_DATE));
			return error;
		}
		return null;
	}


	private void logSubscription(String message, String action,
			String subscriberNumber, String project, String subcriptionType,
			String result, Date receptionDate) {
		LogSuscripcion logSuscription = new LogSuscripcion();

		logSuscription.setMensaje(message);
		logSuscription.setAccion(action);
		logSuscription.setNumero(subscriberNumber);
		logSuscription.setProyecto(project);
		logSuscription.setTipoSuscripcion(subcriptionType);
		logSuscription.setResultado(result);
		logSuscription.setFechaRecepcion(receptionDate);
		logSuscription.setFechaRespuesta(new Date());

		entityManager.persist(logSuscription);
		//entityManager.flush();

	}

	@Override
	public EducadorError addSubscriber(Long idSuscriptor, Long idProyecto) {
		EducadorError error = new EducadorError();
		try {
			SuscriptorProyectoPK spPK;
			SuscriptorProyecto subscriberProject;
			spPK= new SuscriptorProyectoPK();
			spPK.setIdProyecto(idProyecto);
			spPK.setIdSuscriptor(idSuscriptor);
			subscriberProject= entityManager.find(SuscriptorProyecto.class, spPK);
			if (subscriberProject==null) {
				subscriberProject= new SuscriptorProyecto();
				subscriberProject.setId(spPK);
				subscriberProject.setEstadoSuscriptorModulo(EstadoSuscriptorProyecto.ACTIVO.name());
				entityManager.persist(subscriberProject);
				
			}
			else{
				subscriberProject.setEstadoSuscriptorModulo(EstadoSuscriptorProyecto.ACTIVO.name());
				entityManager.merge(subscriberProject);
			}
			error.setCode(EducadorConstants.ErrorCode.SUCCESS);
		} catch (Exception e) {
			error.setCode(EducadorConstants.ErrorCode.DEFAULT_ERROR);
			e.printStackTrace();
		}
		return error;
		

	}




}
