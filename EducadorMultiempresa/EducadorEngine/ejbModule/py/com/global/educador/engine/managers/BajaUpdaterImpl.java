package py.com.global.educador.engine.managers;

import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import py.com.global.educador.engine.configuration.EducadorConstants.QueueMessageParamKey;
import py.com.global.educador.engine.dto.QueueMessage;
import py.com.global.educador.engine.enums.EstadoEjecucionSuscriptor;
import py.com.global.educador.engine.enums.MotivosCancelacion;
import py.com.global.educador.engine.interfaces.BajaUpdater;
@Stateless
public class BajaUpdaterImpl implements BajaUpdater {

	
	@PersistenceContext(unitName="EducadorJpa")
	EntityManager entityManager;
	@EJB SessionManager sessionManager;
	
	Logger log=Logger.getLogger(BajaUpdaterImpl.class);
	
	@Override
	@Asynchronous
	public void process(QueueMessage message) {
		String susNro=(String) message.getParam(QueueMessageParamKey.SUBSCRIBER_NUMBER);
		if (susNro==null) {
			log.error("No se encontro el parametro [SUBSCRIBER_NUMBER]");
			return;
		}
		String shortNumber=(String) message.getParam(QueueMessageParamKey.SHORT_NUMBER);
		if (shortNumber==null) {
			log.error("No se encontro el parametro [SHORT_NUMBER] para el suscriptorNro--> "+susNro);
			return;
		}
		Long susId=getSuscriptorId(susNro);
		if (susId==null) {
			log.error("No se encontro el registro de suscriptor para el numero---> "+susNro);
			return;
		}
		try {
			sessionManager.removeSession(susNro, shortNumber);
			updateEvaluationsOfSuscriptor(susId,shortNumber);
		} catch (Exception e) {
			log.error("BajaUpdater---> "+e,e);
		}
		
		
	}

	@SuppressWarnings("unchecked")
	private Long getSuscriptorId(String susNro) {
		if (susNro==null || susNro.trim().isEmpty()) {
			return null;
		}
		String hql="SELECT _s.idSuscriptor FROM Suscriptor _s WHERE _s.numero=:numero ";
		Query q=entityManager.createQuery(hql);
		q.setParameter("numero", susNro.trim());
		List<Long> l= q.getResultList();
		if (l.isEmpty()) {
			return null;
		}
		return l.get(0);
	}

	private void updateEvaluationsOfSuscriptor(Long susId, String shortNumber) {
		
		
		String sql="UPDATE EJECUCION_SUSCRIPTOR SET ESTADO_EJECUCION=:estadoEjecucionNuevo, MOTIVO_CANCELACION= :motivo WHERE ESTADO_EJECUCION=:estadoEjecucionActual AND ID_SUSCRIPTOR= :idSuscriptor" +
				" AND ID_MODULO IN (SELECT ID_MODULO FROM MODULO MO JOIN PROYECTO PR  ON MO.ID_PROYECTO=PR.ID_PROYECTO WHERE PR.NUMERO_CORTO=:shortNumber)";
		Query q= entityManager.createNativeQuery(sql);
		q.setParameter("estadoEjecucionNuevo", EstadoEjecucionSuscriptor.CANCELADO.name());
		q.setParameter("estadoEjecucionActual", EstadoEjecucionSuscriptor.ACTIVO.name());
		q.setParameter("motivo", MotivosCancelacion.BAJA.name());
		q.setParameter("shortNumber",shortNumber);
		q.setParameter("idSuscriptor", susId);
		int r= q.executeUpdate();
		log.info("Cancelando ejecuciones del [suscriptor="+susId+"][shortNumber="+shortNumber+"]--> "+r);
		
//		String sql2="UPDATE EJECUCION_SUSCRIPTOR_DETALLE SET ESTADO_EJECUCION=:estadoEjecucionNuevo WHERE ESTADO_EJECUCION=:estadoEjecucionDetActual " +
//				"	AND ID_EJECUCION_SUSCRIPTOR IN (SELECT ID_EJECUCION_SUSCRIPTOR FROM EJECUCION_SUSCRIPTOR " +
//				"										WHERE ESTADO_EJECUCION=:estadoEjecucionCab AND ID_SUSCRIPTOR=:idSuscriptor)";
//		Query q2=entityManager.createNativeQuery(sql2);
//		q2.setParameter("estadoEjecucionNuevo", EstadoEjecucionSuscriptorDetalle.CANCELADO.name());
//		q2.setParameter("estadoEjecucionDetActual", EstadoEjecucionSuscriptorDetalle.ACTIVO.name());
//		q2.setParameter("estadoEjecucionCab", EstadoEjecucionSuscriptor.CANCELADO.name());
//		q2.setParameter("idSuscriptor", susId);
//		r=q2.executeUpdate();
//		log.info("Cancelando detalles de ejecuciones del [suscriptor="+susId+"][shortNumber="+shortNumber+"]--> "+r);
//		
//		String sql3="UPDATE EVALUACION_SUSCRIPTOR SET ESTADO_EVALUACION=:estadoEvaluacionNuevo WHERE ESTADO_EVALUACION=:estadoEvaluacionActual " +
//				" AND ID_EJECUCION_DETALLE IN (SELECT DET.ID_EJECUCION_DETALLE " +
//				"		FROM EJECUCION_SUSCRIPTOR_DETALLE DET " +
//				"			WHERE DET.ESTADO_EJECUCION=:estadoEjecucionDet AND DET.ID_EJECUCION_SUSCRIPTOR IN (SELECT EJ.ID_EJECUCION_SUSCRIPTOR " +
//				"																									FROM EJECUCION_SUSCRIPTOR EJ WHERE EJ.ID_SUSCRIPTOR=:idSuscriptor ))";
//		Query q3=entityManager.createNativeQuery(sql3);
//		q3.setParameter("estadoEvaluacionNuevo", EstadoEvaluacionSuscriptor.CANCELADO.name());
//		q3.setParameter("estadoEvaluacionActual", EstadoEvaluacionSuscriptor.ACTIVO.name());
//		q3.setParameter("estadoEjecucionDet", EstadoEjecucionSuscriptorDetalle.CANCELADO.name());
//		q3.setParameter("idSuscriptor", susId);
//		
//		r=q3.executeUpdate();
//		log.info("Cancelando evaluaciones del [suscriptor="+susId+"][shortNumber="+shortNumber+"]--> "+r);
	}

}
