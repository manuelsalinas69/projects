package py.com.global.educador.engine.managers;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
@Singleton
@Startup
public class DataUpdater {

	Logger log=Logger.getLogger(DataUpdater.class);
	
	@PersistenceContext(unitName="EducadorJpa")
	EntityManager entityManager;
	
	@PostConstruct
	public void init(){
		updateDirtyRecords();
	}

	private void updateDirtyRecords() {
		try {
			log.debug("Updating dirty records");
			updateEjecuciones();
			updateEvaluaciones();
		} catch (Exception e) {
			log.error("updateDirtyRecords",e);
		}
		
	}

	private void updateEvaluaciones() {
		String sql="UPDATE EVALUACION_SUSCRIPTOR SET ESTADO_EVALUACION='CANCELADO' WHERE ESTADO_EVALUACION='ACTIVO' AND ID_EJECUCION_DETALLE IN (SELECT DET.ID_EJECUCION_DETALLE FROM EJECUCION_SUSCRIPTOR_DETALLE DET WHERE DET.ESTADO_EJECUCION='CANCELADO')";
		Query q=entityManager.createNativeQuery(sql);
		int r=q.executeUpdate();
		log.info("Evaluaciones [DIRTY] actualizadas--> "+r);
		
	}

	private void updateEjecuciones() {
		String sql="UPDATE EJECUCION_SUSCRIPTOR_DETALLE SET ESTADO_EJECUCION='CANCELADO' WHERE ESTADO_EJECUCION='ACTIVO' AND ID_EJECUCION_SUSCRIPTOR IN (SELECT EJ.ID_EJECUCION_SUSCRIPTOR FROM EJECUCION_SUSCRIPTOR EJ WHERE EJ.ESTADO_EJECUCION='CANCELADO')";
		Query q=entityManager.createNativeQuery(sql);
		int r=q.executeUpdate();
		log.info("Detalle de ejecuciones [DIRTY] actualizados--> "+r);
	}
	
	@Schedule(hour="0",minute="0",second="0", persistent=false)
	//@Schedule(hour="*",minute="*",second="*/10")
	public void updateDirtyData(){
		updateDirtyRecords();
	}
}
