package py.com.global.educador.gui.managers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import py.com.global.educador.gui.entity.Pregunta;
import py.com.global.educador.gui.entity.Tip;
import py.com.global.educador.gui.enums.EstadoEjecucionSuscriptor;
import py.com.global.educador.gui.enums.EstadoEjecucionSuscriptorDetalle;
import py.com.global.educador.gui.enums.EstadoEnvio;
import py.com.global.educador.gui.enums.EstadoEvaluacionSuscriptor;
import py.com.global.educador.gui.enums.PlanificacionEnvioType;

@Name("moduleReporManager")
@Scope(ScopeType.PAGE)
public class ModuleReporManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Logger log= Logger.getLogger(ModuleReporManager.class);
	
	@In(create=true)
	EntityManager entityManager;
	@In(create=true)
	DynamicReportHelper dynamicReportHelper;
	@In(create=true)
	SessionManager sessionManager;
	
	List<Object[]> resumenEjecucion;
	List<Object[]> resumenCancelados;
	List<Object[]> resumenfechaRespuestas;
	List<Object[]> resumenPregunta;
	List<Object[]> resumenPreguntasRepondidas;
	
	List<Object[]> jsonData;
	List<Tip> tips;
	Long idEmpresa;
	Long idProyecto;
	Long idModulo=null;
	List<Pregunta> preguntas;
	Long idPregunta;
	
	@Create
	public void init(){
		updateDirtyRecords();
		loadPreguntasByModulo();
		if (!sessionManager.userFromSuperCompany()) {
			idEmpresa=sessionManager.getLoggedUserCompany();
		}
	}
	
	private void updateDirtyRecords() {
		//updateEjecucionDetalles();
		//updateEvaluacionSuscriptor();
		
		
	}

	public void reportTest(){
		if (idModulo==null) {
			return;
		}
		Long finalizados= getEjecucionesCount(EstadoEjecucionSuscriptor.FINALIZADO, idModulo);
		Long cancelados= getEjecucionesCount(EstadoEjecucionSuscriptor.CANCELADO, idModulo);
		Long activos= getEjecucionesCount(EstadoEjecucionSuscriptor.ACTIVO, idModulo);
		
		resumenEjecucion= new ArrayList<Object[]>();
		
		resumenEjecucion.add(new Object[]{"Finalizados",finalizados});
		
		resumenEjecucion.add(new Object[]{"Cancelados",cancelados});
		resumenEjecucion.add(new Object[]{"Activos",activos});
	}
	
	
	public void reportCancelados(){
		if (idModulo==null) {
			return;
		}
		resumenCancelados=getResumenCancelados0(idModulo);
	}
	
	public void reportFechaRespuestas(){
		if (idModulo==null) {
			return;
		}
		resumenfechaRespuestas=getResumenFechaRespuesta(idModulo);
	}

	public void loadPreguntasByModulo(){
		if (idModulo==null) {
			return;
		}
		preguntas=getPreguntasByModulo0(idModulo);
	}
	
	public void reportPregunta(){
		if (idModulo==null) {
			return;
		}
		resumenPregunta=getResumenPregunta0(idPregunta);
	}
	
	public void reportPreguntaRespondidas(){
		if (idModulo==null) {
			return;
		}
		resumenPreguntasRepondidas=getResumenPreguntasRespondidas0(idPregunta);
	}
	
	public void reportTipEviados(){
		if (idModulo==null) {
			return;
		}
		tips=getTips(idModulo);
		jsonData=getResumenTipPorEstado(idModulo, EstadoEjecucionSuscriptorDetalle.ENVIADO);
	}
	
	public void reporteFallidoPorTip(Long idTip){
		if (idTip==null) {
			return;
		}
		jsonData=getResumenTipFallidos(idTip);
	}
	
	@SuppressWarnings("unchecked")
	private List<Object[]> getResumenTipFallidos(Long idTip) {
		String hql="SELECT COALESCE(SUBSTR(_ed.respuestaSmsc, 15),'NO_RESP_SMSC'),count(_ed) FROM EjecucionSuscriptorDetalle _ed WHERE _ed.estadoEjecucion=:estadoEjecucion AND _ed.enviar=:tip AND _ed.tip.idTip= :idTip GROUP BY  _ed.respuestaSmsc ORDER BY _ed.respuestaSmsc";
		Query q= entityManager.createQuery(hql);
		q.setParameter("tip", "TIP");
		q.setParameter("estadoEjecucion", EstadoEjecucionSuscriptorDetalle.FALLIDO.name());
		q.setParameter("idTip", idTip);
		return q.getResultList();
		
	}

	public void reportTipFallidos(){
		if (idModulo==null) {
			return;
		}
		tips=getTips(idModulo);
		jsonData=getResumenTipPorEstado(idModulo, EstadoEjecucionSuscriptorDetalle.FALLIDO);
	}
	
	@SuppressWarnings("unchecked")
	private List<Tip> getTips(Long idModulo) {
		String hql="SELECT _pe.tip FROM PlanificacionEnvio _pe WHERE _pe.enviar=:envio AND _pe.modulo.idModulo= :idModulo ORDER BY _pe.orden";
		Query q=entityManager.createQuery(hql);
		q.setParameter("idModulo", idModulo);
		q.setParameter("envio", PlanificacionEnvioType.TIP.name());
		
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	private List<Object[]> getResumenTipPorEstado(Long idModulo, EstadoEjecucionSuscriptorDetalle estadoEnvio) {
		String hql="SELECT _ed.tip.contenido,count(_ed.orden) FROM EjecucionSuscriptorDetalle _ed WHERE _ed.estadoEjecucion=:enviado AND _ed.enviar=:tip AND _ed.ejecucionSuscriptor.modulo.idModulo= :idModulo GROUP BY _ed.orden,_ed.tip.contenido ORDER BY _ed.orden";
//		String hql="SELECT _ed.orden,_ed.estadoEjecucion,count(*) FROM EjecucionSuscriptorDetalle _ed WHERE _ed.enviar=:tip AND _ed.ejecucionSuscriptor.modulo.idModulo= :idModulo GROUP BY  _ed.orden,_ed.estadoEjecucion ORDER BY _ed.orden";
		Query q= entityManager.createQuery(hql);
		q.setParameter("tip", "TIP");
		q.setParameter("enviado", estadoEnvio.name());
		q.setParameter("idModulo", idModulo);
		return q.getResultList();
	}

	public void reportPreguntasCorrectas(){
		Long r1=getResumentPreguntasCorrectas0(idPregunta,true);
		jsonData= new ArrayList<Object[]>();
		jsonData.add(new Object[]{"Correctas",r1});
		r1=getResumentPreguntasCorrectas0(idPregunta,false);
		jsonData.add(new Object[]{"Incorrectas",r1});
	}
	
	public void reportEnvioPregunta(){
		
		jsonData=getResumenEnvioPreguntas(idPregunta);
		
	}
	
	public void reporteEnvioFallidoEnvioPregunta(){
		jsonData=getResumenEnvioPreguntasFallidas(idPregunta);
	}
	
	@SuppressWarnings("unchecked")
	private List<Object[]> getResumenEnvioPreguntas(Long idPregunta) {
		String hql="SELECT _es.estadoEnvio ,count(_es) FROM EvaluacionSuscriptor _es WHERE _es.pregunta.idPregunta= :idPregunta GROUP BY _es.estadoEnvio ORDER BY _es.estadoEnvio";
		Query q= entityManager.createQuery(hql);
		q.setParameter("idPregunta", idPregunta);
		return q.getResultList();
		
	}
	
	@SuppressWarnings("unchecked")
	private List<Object[]> getResumenEnvioPreguntasFallidas(Long idPregunta) {
		String hql="SELECT COALESCE(SUBSTR( _es.respuestaSenderSmsc, 15),'NO_RESP_SMSC') ,count(_es) FROM EvaluacionSuscriptor _es WHERE _es.pregunta.idPregunta= :idPregunta AND _es.estadoEnvio=:estadoEnvio  GROUP BY _es.respuestaSenderSmsc ORDER BY _es.respuestaSenderSmsc";
		Query q= entityManager.createQuery(hql);
		q.setParameter("idPregunta", idPregunta);
		q.setParameter("estadoEnvio", EstadoEnvio.FALLIDO.name());
		return q.getResultList();
		
	}
	
	public void downloadNumerosPreguntas(Long idEvaluacion){
		try {
			
			String sql="SELECT DISTINCT suscriptor.numero FROM evaluacion_suscriptor JOIN suscriptor ON evaluacion_suscriptor.id_suscriptor=suscriptor.id_suscriptor "+
					" WHERE evaluacion_suscriptor.id_evaluacion  ="+idEvaluacion+" AND evaluacion_suscriptor.estado_evaluacion='RESPONDIDO'";
			
			dynamicReportHelper.generateReport(sql);
			
		} catch (Exception e) {
			System.out.println("ModuleReporManager.downloadNumerosPreguntas(): "+e);
			e.printStackTrace();
		}
	}
	

	private Long getResumentPreguntasCorrectas0(Long idPregunta, boolean correctas) {
		String hql="SELECT count(_es) FROM EvaluacionSuscriptor _es WHERE _es.pregunta.idPregunta= :idPregunta AND _es.estadoEvaluacion= :estadoEvaluacion"; 
		if (correctas) {
			hql+=" AND _es.respuestaCorrecta= :correctas";
		}	
		else{
			hql+=" AND (_es.respuestaCorrecta!= :correctas OR _es.respuestaCorrecta is null)";
		}
				
		Query q= entityManager.createQuery(hql);
		q.setParameter("idPregunta", idPregunta);
		q.setParameter("estadoEvaluacion", EstadoEvaluacionSuscriptor.RESPONDIDO.name());
		q.setParameter("correctas", Boolean.TRUE);
		Number r=(Number) q.getSingleResult();
		return r.longValue();
	}

	@SuppressWarnings("unchecked")
	private List<Object[]> getResumenPreguntasRespondidas0(Long idPregunta) {
		Pregunta p= entityManager.find(Pregunta.class, idPregunta);
		String hql="SELECT _es.respuesta.contenidoRespuesta ,count(_es) FROM EvaluacionSuscriptor _es WHERE _es.pregunta.idPregunta= :idPregunta GROUP BY _es.respuesta.contenidoRespuesta";
		if (p.getPreguntaAbierta()==null || !p.getPreguntaAbierta()) {
			hql="SELECT _es.respuesta.contenidoRespuesta ,count(_es) FROM EvaluacionSuscriptor _es WHERE _es.pregunta.idPregunta= :idPregunta GROUP BY _es.respuesta.contenidoRespuesta";
		}
		else{
			hql="SELECT _es.respuestaAbierta ,count(_es) FROM EvaluacionSuscriptor _es WHERE _es.pregunta.idPregunta= :idPregunta GROUP BY _es.respuestaAbierta";
		}
		Query q= entityManager.createQuery(hql);
		q.setParameter("idPregunta", idPregunta);
		return q.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	private List<Object[]> getResumenPregunta0(Long idPregunta) {
		String hql="SELECT _es.estadoEvaluacion,count(_es) FROM EvaluacionSuscriptor _es WHERE _es.pregunta.idPregunta= :idPregunta GROUP BY _es.estadoEvaluacion";
		Query q= entityManager.createQuery(hql);
		q.setParameter("idPregunta", idPregunta);
		return q.getResultList();
	}
	
	

	@SuppressWarnings("unchecked")
	private List<Pregunta> getPreguntasByModulo0(Long idModulo) {
		try {
			String hql="SELECT _p FROM Pregunta _p WHERE _p.evaluacion.modulo.idModulo=:idModulo ORDER BY _p.evaluacion.idEvaluacion,_p.ordenPregunta";
			Query q= entityManager.createQuery(hql);
			q.setParameter("idModulo", idModulo);
			return q.getResultList();
		} catch (Exception e) {
			log.error("getPreguntasByModulo0",e);
		}
		return new ArrayList<Pregunta>();
	}

	
	
	private Long getEjecucionesCount(EstadoEjecucionSuscriptor estado, Long idModulo) {
		String hql="SELECT count(_e) FROM EjecucionSuscriptor _e WHERE _e.estadoEjecucion=:estado AND _e.modulo.idModulo= :idModulo";
		Query q=entityManager.createQuery(hql);
		q.setParameter("estado", estado.name());
		q.setParameter("idModulo", idModulo);
		
		try {
			Number r= (Number) q.getSingleResult();
			return r.longValue();
		} catch (Exception e) {
			log.error("getEjecucionesCount",e);
		}
		
		return 0L;
	}
	
	@SuppressWarnings("unchecked")
	private List<Object[]> getResumenCancelados0(Long idModulo){
		String hql="SELECT _e.motivoCancelacion,count(_e) FROM EjecucionSuscriptor _e WHERE _e.estadoEjecucion=:estado AND _e.modulo.idModulo= :idModulo GROUP BY _e.motivoCancelacion ORDER BY _e.motivoCancelacion";
		Query q=entityManager.createQuery(hql);
		q.setParameter("estado", EstadoEjecucionSuscriptor.CANCELADO.name());
		q.setParameter("idModulo", idModulo);
		
		try {
			return q.getResultList();
		} catch (Exception e) {
			log.error("getEjecucionesCount",e);
		}
		
		return new ArrayList<Object[]>();
	}

	
	@SuppressWarnings("unchecked")
	private List<Object[]> getResumenFechaRespuesta(Long idModulo){
		try {
			String hql="SELECT to_char(_ev.fechaRespuesta,'YYYY-MM-DD HH24') as fecha,count(_ev) FROM EvaluacionSuscriptor _ev WHERE _ev.fechaRespuesta is not null AND _ev.ejecucionSuscriptorDetalle.ejecucionSuscriptor.modulo.idModulo=:idModulo GROUP BY to_char(_ev.fechaRespuesta,'YYYY-MM-DD HH24') ORDER BY to_char(_ev.fechaRespuesta,'YYYY-MM-DD HH24')";
			Query q= entityManager.createQuery(hql);
			q.setParameter("idModulo", idModulo);
			return q.getResultList();
		} catch (Exception e) {
			log.error("getResumenFechaRespuesta",e);
		}
		return new ArrayList<Object[]>();
	}
	

	public List<Object[]> getResumenEjecucion() {
		return resumenEjecucion;
	}

	public void setResumenEjecucion(List<Object[]> resumenEjecucion) {
		this.resumenEjecucion = resumenEjecucion;
	}

	public Long getIdModulo() {
		return idModulo;
	}

	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}

	public List<Object[]> getResumenCancelados() {
		return resumenCancelados;
	}

	public void setResumenCancelados(List<Object[]> resumenCancelados) {
		this.resumenCancelados = resumenCancelados;
	}

	public List<Object[]> getResumenfechaRespuestas() {
		return resumenfechaRespuestas;
	}

	public void setResumenfechaRespuestas(List<Object[]> resumenfechaRespuestas) {
		this.resumenfechaRespuestas = resumenfechaRespuestas;
	}

	public List<Object[]> getResumenPregunta() {
		return resumenPregunta;
	}

	public void setResumenPregunta(List<Object[]> resumenPregunta) {
		this.resumenPregunta = resumenPregunta;
	}

	public List<Object[]> getResumenPreguntasRepondidas() {
		return resumenPreguntasRepondidas;
	}

	public void setResumenPreguntasRepondidas(
			List<Object[]> resumenPreguntasRepondidas) {
		this.resumenPreguntasRepondidas = resumenPreguntasRepondidas;
	}

	public List<Pregunta> getPreguntas() {
		if (idModulo==null) {
			preguntas=null;
		}
		preguntas=getPreguntasByModulo0(idModulo);
		return preguntas;
	}

	public void setPreguntas(List<Pregunta> preguntas) {
		this.preguntas = preguntas;
	}

	public List<Object[]> getJsonData() {
		return jsonData;
	}

	public void setJsonData(List<Object[]> jsonData) {
		this.jsonData = jsonData;
	}

	public Long getIdPregunta() {
		return idPregunta;
	}

	public void setIdPregunta(Long idPregunta) {
		this.idPregunta = idPregunta;
	}

	public List<Tip> getTips() {
		if (idModulo==null) {
			tips=null;
		}
		tips=getTips(idModulo);
		return tips;
	}

	public void setTips(List<Tip> tips) {
		this.tips = tips;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public Long getIdProyecto() {
		return idProyecto;
	}

	public void setIdProyecto(Long idProyecto) {
		this.idProyecto = idProyecto;
	}
	
	

	
	
	
}
