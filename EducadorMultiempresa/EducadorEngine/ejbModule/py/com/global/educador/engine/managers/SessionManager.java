package py.com.global.educador.engine.managers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.configuration.EducadorConstants;
import py.com.global.educador.engine.configuration.EducadorConstants.QueueMessageParamKey;
import py.com.global.educador.engine.configuration.EducadorConstants.SystemParameterKey;
import py.com.global.educador.engine.dto.QueueMessage;
import py.com.global.educador.jpa.entity.ParametroSistema;
import py.com.global.educador.jpa.entity.SessionTrx;
import py.com.global.educador.jpa.entity.Suscriptor;

@Stateless

public class SessionManager  {

	

	Logger log=Logger.getLogger(SessionManager.class);

	//Map<String, QueueMessage> sessions;

	int sesionHours=4;
	int sessionMinutes=0;

	@PersistenceContext(unitName="EducadorJpa")
	EntityManager entityManager;


	@PostConstruct
	public void init(){
		
	}

	

	@Asynchronous
	public void updateSessionIniDate(String susNro, String shortNumber, Date newIniDate){
		QueueMessage params=getSession(susNro, shortNumber);
		if (params==null) {
			log.error("NO se ha encontrado la sesion para el [suscriptorNro="+susNro+"][shortNumber="+shortNumber+"]");
			return;
		}
		if (params.getParam("SESSION_ID")==null) {
			log.error("NO se ha encontrado el parametro de [SESSION_ID] para la sesion para el " +
					" [suscriptorNro="+susNro+"][shortNumber="+shortNumber+"] [session params]--> "+params);
			return;
		}
		SessionTrx session=entityManager.find(SessionTrx.class, (Long) params.getParam("SESSION_ID"));
		if (session==null) {
			log.error("No se ha encontrado de la sesion en la base de datos para el valor de [SESSION_ID]---> "+params.getParam("SESSION_ID"));
			return;
		}
		session.setFechaInicioSession(newIniDate);
		entityManager.merge(session);
		params.addParam("SESSION_INI_DATE", newIniDate);
		log.debug("New Session params for [suscriptorNro="+susNro+"][shortNumber= "+shortNumber+"]--> "+getSession(susNro, shortNumber));

		//sessions.put(shortNumber.trim()+"_"+susNro.trim(), params);
	}

	public SessionManager() {
		//sessions= new HashMap<String, QueueMessage>();
	}

	 
	public QueueMessage getSession(String susNro,String shortNumber){
		return parseSessionToQueueMessage(getDbSession(susNro, shortNumber));
	}
	
	@SuppressWarnings("unchecked")
	public SessionTrx getDbSession(String susNro,String shortNumber){
		String hql="SELECT _s FROM SessionTrx _s WHERE _s.numero= :numero AND _s.numeroCorto= :numeroCorto";
		Query q= entityManager.createQuery(hql);
		q.setParameter("numero", susNro);
		q.setParameter("numeroCorto", shortNumber);
		List<SessionTrx> l=q.getResultList();
		if (l.isEmpty()) {
			return null;
		}
		return l.get(0);
	}

	public QueueMessage parseSessionToQueueMessage(SessionTrx _s){
		if (_s==null) {
			return null;
		}
		
		QueueMessage message;
		message= new QueueMessage();
		
		
		message.addParam("SESSION_ID", _s.getIdSessionTrx());
		message.addParam("SESSION_INI_DATE", _s.getFechaInicioSession());
		message.addParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER, _s.getNumero());
		message.addParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_ID, _s.getSuscriptor().getIdSuscriptor());
		message.addParam(EducadorConstants.QueueMessageParamKey.EVALUATION_ID, _s.getIdEvaluacion());
		message.addParam(QueueMessageParamKey.SUBSCRIBER_EXECUTION_DETAIL_ID, _s.getIdEjecucionDetalle());
		message.addParam(QueueMessageParamKey.SUBSCRIBER_EVALUATION_ID, _s.getIdEvaluacionSuscriptor());
		message.addParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER, _s.getNumeroCorto());
		return message;
	}
	
	 
	public boolean isSessionActive(String susNro, String shortNumber){
		return getDbSession(susNro, shortNumber)!=null;
	}

	 
	public boolean createSession(String susNro,String shortNumber, QueueMessage params){
		try {
//			if (isSessionActive(susNro, shortNumber)) {
//				log.error("Existe una sesion activa para el [susNro: "+susNro+"][short"+shortNumber+"], no se puede crear otra ");
//				return false;
//			}

			return createSessionFor(susNro,shortNumber, params)!=null;
		} catch (Exception e) {
			log.error("Error in createSession for [Suscriptor: "+susNro+", [params: "+params+"]", e);
		}

		return false;
	}

	 
	public boolean createSession(String susNro,String shortNumber, BytesMessage params){
		try {
//			if (isSessionActive(susNro, shortNumber)) {
//				log.error("Existe una sesion activa para el [susNro: "+susNro+"][short"+shortNumber+"], no se puede crear otra ");
//				return false;
//			}

			return createSessionFor(susNro,shortNumber, params)!=null;
		} catch (Exception e) {
			log.error("Error in createSession for [Suscriptor: "+susNro+", [params: "+params+"]", e);
		}

		return false;
	}

	@Asynchronous
	public void removeSession(String susNro, String shortNumber){
		try {
			tryDeleteSubscriberData(susNro, shortNumber);
			
		} catch (Exception e) {
			log.error("Error in removeSession for [Suscriptor: "+susNro+"]", e);
		}
		
	}

	private int tryDeleteSubscriberData(String susNro, String shortNumber) {
		String sql="DELETE FROM SESSION_TRX WHERE NUMERO=:susNro AND NUMERO_CORTO= :shortNumber";
		Query q= entityManager.createNativeQuery(sql);
		q.setParameter("susNro", susNro);
		q.setParameter("shortNumber", shortNumber);
		int r= q.executeUpdate();
		log.debug("Deleting sesions for [suscriptor:"+susNro+"] [rows]---> "+r);
		return r;

	}
	private SessionTrx createSessionFor(String susNro, String shortNumber, QueueMessage params) {
		susNro=susNro==null?(String) params.getParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER):susNro;
		if (susNro==null || susNro.trim().isEmpty()) {
			log.error("No se encontro el parametro [SUSCRIPTOR_NRO], no se puede crear la session");
			return null;
		}
		if (shortNumber==null || shortNumber.trim().isEmpty()) {
			log.error("No se encontro el parametro [SUSCRIPTOR_NRO], no se puede crear la session");
			return null;
		}
		Long evalId=(Long) params.getParam(EducadorConstants.QueueMessageParamKey.EVALUATION_ID);
		Long ejeDetId=(Long) params.getParam(QueueMessageParamKey.SUBSCRIBER_EXECUTION_DETAIL_ID);
		Long evalSusId=(Long) params.getParam(QueueMessageParamKey.SUBSCRIBER_EVALUATION_ID);
		if (evalId==null) {
			log.error("No se encontro el parametro [EVALUACION_ID], no se puede crear la session");
			return null;
		}

		if (ejeDetId==null) {
			log.error("No se encontro el parametro [EJECUCION_SUSCRIPTOR_DETALLE_ID], no se puede crear la session");
			return null;
		}

		if (evalSusId==null) {
			log.error("No se encontro el parametro [EVALUACION_SUSCRIPTOR_ID], no se puede crear la session");
			return null;
		}
		SessionTrx newSessionTrx= new SessionTrx();
		Date iniSessionDate=new Date();
		newSessionTrx.setFechaInicioSession(iniSessionDate);
		newSessionTrx.setNumero((String) params.getParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER));
		newSessionTrx.setNumeroCorto((String) params.getParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER));
		newSessionTrx.setIdEjecucionDetalle(ejeDetId);
		newSessionTrx.setIdEvaluacion(evalId);
		newSessionTrx.setIdEvaluacionSuscriptor(evalSusId);
		Long susId=(Long) params.getParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_ID);
		newSessionTrx.setSuscriptor(entityManager.find(Suscriptor.class, susId));
		entityManager.persist(newSessionTrx);
		params.addParam("SESSION_ID", newSessionTrx.getIdSessionTrx());
		params.addParam("SESSION_INI_DATE", iniSessionDate);
		//sessions.put(shortNumber.trim()+"_"+susNro.trim(), params);
		return newSessionTrx;


	}

	private SessionTrx createSessionFor(String susNro, String shortNumber, BytesMessage params) {
		try {
			susNro=susNro==null?(String) params.getObjectProperty(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER):susNro;
			if (susNro==null || susNro.trim().isEmpty()) {
				log.error("No se encontro el parametro [SUSCRIPTOR_NRO], no se puede crear la session");
				return null;
			}
			if (shortNumber==null || shortNumber.trim().isEmpty()) {
				log.error("No se encontro el parametro [SUSCRIPTOR_NRO], no se puede crear la session");
				return null;
			}
			Long evalId=(Long) params.getObjectProperty(EducadorConstants.QueueMessageParamKey.EVALUATION_ID);
			Long ejeDetId=(Long) params.getObjectProperty(QueueMessageParamKey.SUBSCRIBER_EXECUTION_DETAIL_ID);
			Long evalSusId=(Long) params.getObjectProperty(QueueMessageParamKey.SUBSCRIBER_EVALUATION_ID);
			if (evalId==null) {
				log.error("No se encontro el parametro [EVALUACION_ID], no se puede crear la session");
				return null;
			}

			if (ejeDetId==null) {
				log.error("No se encontro el parametro [EJECUCION_SUSCRIPTOR_DETALLE_ID], no se puede crear la session");
				return null;
			}

			if (evalSusId==null) {
				log.error("No se encontro el parametro [EVALUACION_SUSCRIPTOR_ID], no se puede crear la session");
				return null;
			}
			SessionTrx newSessionTrx= new SessionTrx();
			Date iniSessionDate=new Date();
			newSessionTrx.setFechaInicioSession(iniSessionDate);
			newSessionTrx.setNumero((String) params.getObjectProperty(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER));
			newSessionTrx.setNumeroCorto((String) params.getObjectProperty(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER));
			newSessionTrx.setIdEjecucionDetalle(ejeDetId);
			newSessionTrx.setIdEvaluacion(evalId);
			newSessionTrx.setIdEvaluacionSuscriptor(evalSusId);
			Long susId=(Long) params.getObjectProperty(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_ID);
			newSessionTrx.setSuscriptor(entityManager.find(Suscriptor.class, susId));
			entityManager.persist(newSessionTrx);

			QueueMessage message= new QueueMessage();
			message.addParam("SESSION_ID", newSessionTrx.getIdSessionTrx());
			message.addParam("SESSION_INI_DATE", newSessionTrx.getFechaInicioSession());
			message.addParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_NUMBER, newSessionTrx.getNumero());
			message.addParam(EducadorConstants.QueueMessageParamKey.SUBSCRIBER_ID, newSessionTrx.getSuscriptor().getIdSuscriptor());
			message.addParam(EducadorConstants.QueueMessageParamKey.EVALUATION_ID, newSessionTrx.getIdEvaluacion());
			message.addParam(QueueMessageParamKey.SUBSCRIBER_EXECUTION_DETAIL_ID, newSessionTrx.getIdEjecucionDetalle());
			message.addParam(QueueMessageParamKey.SUBSCRIBER_EVALUATION_ID, newSessionTrx.getIdEvaluacionSuscriptor());
			message.addParam(EducadorConstants.QueueMessageParamKey.SHORT_NUMBER,newSessionTrx.getNumeroCorto());

			//sessions.put(shortNumber.trim()+"_"+susNro.trim(), message);
			return newSessionTrx;
		} catch (JMSException e) {
			log.error(e);
		}
		return null;

	}

	
	 
	
	@Asynchronous
	public void updateSessionsTime(){

		Long t1=System.currentTimeMillis();
//		if (sessions==null) {
//			return;
//		}
		Calendar c=Calendar.getInstance();
		sesionHours=getSessionHours();
		sessionMinutes=getSessionMinutes();
		log.debug("Session timeout params [HOUR:"+sesionHours+"][MINUTE:"+sessionMinutes+"]");
		c.add(Calendar.HOUR_OF_DAY, sesionHours*-1);
		c.add(Calendar.MINUTE, sessionMinutes*-1);

		try {
			
//			String sql2="SELECT NUMERO,NUMERO_CORTO FROM SESSION_TRX WHERE FECHA_INICIO_SESSION< :LOWEST_INI_DATE";
//			Query q=entityManager.createNativeQuery(sql2);
//			q.setParameter("LOWEST_INI_DATE", c.getTime());
//			List<Object[]> expiredSessionNumbers=q.getResultList();
//			log.debug("Returned numbers from expired sessions---> "+expiredSessionNumbers.size());

			String sql="DELETE FROM SESSION_TRX WHERE FECHA_INICIO_SESSION < :LOWEST_INI_DATE";

			Query q2= entityManager.createNativeQuery(sql);
			q2.setParameter("LOWEST_INI_DATE", c.getTime());

			int rows=q2.executeUpdate();
			log.debug("Deleted Sessions---> "+rows);


			Long t2=System.currentTimeMillis();
			log.debug("Update session delay---> "+(t2-t1)+"ms.");
		} catch (Exception e) {
			log.error("updateSessionsTime --->",e);
		}

	}

	private int getSessionHours() {
		try {
			return Integer.parseInt(getValue(SystemParameterKey.SYSTEM_ENGINE_PROCESS_SESSION_TIMEOUT_HOURS));
		} catch (Exception e) {
			log.error(e);
		}
		return 2;//default 2 horas
	}

	private String getValue(String paramKey) {
		return entityManager.find(ParametroSistema.class, paramKey).getValor();
	}

	private int getSessionMinutes() {
		try {
			return Integer.parseInt(getValue(SystemParameterKey.SYSTEM_ENGINE_PROCESS_SESSION_TIMEOUT_MINUTES));
		} catch (Exception e) {
			log.error(e);
		}
		return 0;
	}


}

