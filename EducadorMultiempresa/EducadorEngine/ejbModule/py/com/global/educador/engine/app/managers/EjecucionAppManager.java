package py.com.global.educador.engine.app.managers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import py.com.global.educador.engine.dto.FormularioDto;
import py.com.global.educador.engine.enums.EstadoEjecucionSuscriptor;
import py.com.global.educador.engine.enums.EstadoEjecucionSuscriptorDetalle;
import py.com.global.educador.engine.enums.EstadoEvaluacionSuscriptor;
import py.com.global.educador.jpa.entity.EjecucionSuscriptor;
import py.com.global.educador.jpa.entity.EjecucionSuscriptorDetalle;
import py.com.global.educador.jpa.entity.Evaluacion;
import py.com.global.educador.jpa.entity.EvaluacionSuscriptor;
import py.com.global.educador.jpa.entity.Modulo;
import py.com.global.educador.jpa.entity.PlanificacionEnvio;
import py.com.global.educador.jpa.entity.Pregunta;
import py.com.global.educador.jpa.entity.Respuesta;
import py.com.global.educador.jpa.entity.Suscriptor;

@Stateless
public class EjecucionAppManager {

	
	@PersistenceContext 
	EntityManager entityManager;
	
	public FormularioDto createNew(Long idModulo, Long idSuscriptor){
		EjecucionSuscriptor ejecucionNueva=null;
		EjecucionSuscriptorDetalle det;
		try {
			ejecucionNueva= createEjecucionSubcriber0(idModulo, idSuscriptor, EstadoEjecucionSuscriptor.ACTIVO);
			det= crearEjecucionSuscriptorDetalle(ejecucionNueva, 1);
			FormularioDto dto= createForm(ejecucionNueva, det);
			return dto;
		} catch (Exception e) {
			System.out.println("AppServices.createEvaluacion(): "+e);
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	@SuppressWarnings("unchecked")
	private EvaluacionSuscriptor getEvaluacion(EjecucionSuscriptorDetalle det) {
		String hql="SELECT ev FROM EvaluacionSuscriptor ev WHERE ev.ejecucionSuscriptorDetalle= :det";
		Query q= entityManager.createQuery(hql);
		q.setParameter("det", det);
		List<EvaluacionSuscriptor> l=q.getResultList();
		if (l.isEmpty()) {
			return crearEvaluacionSuscriptor(det, 1);
		}
		return l.get(0);
	}
	
	public FormularioDto statusEjecucion(Long idEjecucion, Long idDetalle){
		EjecucionSuscriptor ej= entityManager.find(EjecucionSuscriptor.class, idEjecucion);
		EjecucionSuscriptorDetalle det= entityManager.find(EjecucionSuscriptorDetalle.class, idDetalle);
		return createForm(ej, det);
	}
	
	public FormularioDto nextAction(Long idEjecucion, Long idDetalle, Long idEvaluacion, Long idPregunta, Long idRespuesta, String respuesta){
		EjecucionSuscriptor ej= entityManager.find(EjecucionSuscriptor.class, idEjecucion);
		EjecucionSuscriptorDetalle det= entityManager.find(EjecucionSuscriptorDetalle.class, idDetalle);
		
		EjecucionSuscriptorDetalle nextDetail= nextDetail(ej, det);
		if (!isTip(det.getEnviar())) {
			Pregunta p= entityManager.find(Pregunta.class, idPregunta);
			EvaluacionSuscriptor eva= entityManager.find(EvaluacionSuscriptor.class, idEvaluacion);
			if (p.getPreguntaAbierta()!=null && p.getPreguntaAbierta()) {
				eva.setRespuestaAbierta(respuesta);
			}
			else{
				Respuesta r= entityManager.find(Respuesta.class, idRespuesta);
				eva.setRespuesta(r);
				eva.setRespuestaCorrecta(r.getEsRespuestaCorrecta());
			}
			eva.setEstadoEvaluacion(EstadoEvaluacionSuscriptor.RESPONDIDO.name());
			det.setEstadoEjecucion(EstadoEjecucionSuscriptorDetalle.ENVIADO.name());
			entityManager.merge(eva);
			entityManager.merge(det);
		}
		return createForm(ej, nextDetail);
	}



	@SuppressWarnings("unchecked")
	private EjecucionSuscriptorDetalle nextDetail(EjecucionSuscriptor ej,
			EjecucionSuscriptorDetalle det) {
		String hql= "SELECT det FROM EjecucionSuscriptorDetalle det WHERE det.idEjecucionDetalle> :idDetalle AND det.ejecucionSuscriptor= :ej ORDER BY det.idEjecucionDetalle";
		Query q= entityManager.createQuery(hql);
		q.setParameter("idDetalle", det.getIdEjecucionDetalle());
		q.setParameter("ej", ej);
		List<EjecucionSuscriptorDetalle> l=q.getResultList();
		if (l.isEmpty()) {
			return crearEjecucionSuscriptorDetalle(ej, det.getOrden()+1);
		}
		return l.get(0);
	}



	private FormularioDto createForm(EjecucionSuscriptor ej,
			EjecucionSuscriptorDetalle det) {
		
		FormularioDto d= new FormularioDto();
		
		d.putAttr("idEjecucion", ej.getIdEjecucionSuscriptor());
		d.putAttr("idDetalleEjecucion", det.getIdEjecucionDetalle());
		d.putAttr("formType", det.getEnviar());
		d.putAttr("final", det.getEnvioFinal());
		if (isTip(det.getEnviar())) {
			d.putAttr("info", det.getTip().getContenido());
		}
		else{
			FormularioDto evDto=new FormularioDto();
			EvaluacionSuscriptor ev= getEvaluacion(det);
			evDto.putAttr("idEvaluacion",ev.getIdEvaluacionSuscriptor());
			evDto.putAttr("idPregunta", ev.getPregunta().getIdPregunta());
			evDto.putAttr("preguntaAbierta", ev.getPregunta().getPreguntaAbierta()==null?false:ev.getPregunta().getPreguntaAbierta());
			evDto.putAttr("estadoEvaluacion", ev.getEstadoEvaluacion());
			if (ev.getPregunta().getPreguntaAbierta()) {
				if (ev.getRespuestaAbierta()!=null && !ev.getRespuestaAbierta().trim().isEmpty()) {
					evDto.putAttr("respuesta", ev.getRespuestaAbierta());
				}
			}
			else{
				if (ev.getRespuesta()!=null) {
					evDto.putAttr("idRespuesta", ev.getRespuesta().getIdRespuesta());
					evDto.putAttr("respuestas", getRespuestas(ev.getPregunta()));
				}
			}
		}
		
		return null;
	}



	@SuppressWarnings("unchecked")
	private List<FormularioDto> getRespuestas(Pregunta pregunta) {
		List<Respuesta> l;
		List<FormularioDto> respuestas= new ArrayList<FormularioDto>();
		String hql="SELECT r from Respuesta r WHERE r.pregunta= :preg ORDER BY r.ordenRespuesta";
		Query q= entityManager.createQuery(hql);
		q.setParameter("preg", pregunta);
		l=q.getResultList();
		FormularioDto resDto;
		for (Respuesta _r : l) {
			resDto= new FormularioDto();
			resDto.putAttr("idRespuesta", _r.getIdRespuesta());
			resDto.putAttr("contenidoRespuesta", _r.getContenidoRespuesta());
			respuestas.add(resDto);
		}
		return respuestas;
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
	
	private EjecucionSuscriptorDetalle crearEjecucionSuscriptorDetalle(
			EjecucionSuscriptor es, long orden) {
		PlanificacionEnvio _pe=getPlanificacionEnvio(es.getModulo().getIdModulo(), orden);
		if (_pe==null) {
			System.out.println("No hay plan de envio para el [modulo="+es.getModulo().getIdModulo()+"] y [orden="+orden+"]");
			return null;
		}
		EjecucionSuscriptorDetalle _ed= new EjecucionSuscriptorDetalle();
		_ed.setEstadoEjecucion(EstadoEjecucionSuscriptorDetalle.ACTIVO.name());
		if (_pe.getEnviar().equalsIgnoreCase("TIP")) {
			_ed.setTip(_pe.getTip());
		}
		else if(_pe.getEnviar().equalsIgnoreCase("EVALUACION")){
			_ed.setEvaluacion(_pe.getEvaluacion());
		}
		_ed.setOrden(_pe.getOrden());
		_ed.setEjecucionSuscriptor(es);
		_ed.setEnvioFinal(_pe.getEnvioFinal());
		_ed.setEnviar(_pe.getEnviar());
		_ed.setEsperarHorasAnterior(_pe.getEsperarHorasAnterior());
		_ed.setEsperarMinutosAnterior(_pe.getEsperarMinutosAnterior());
		entityManager.persist(_ed);
		return _ed;
	}
	
	private PlanificacionEnvio getPlanificacionEnvio(Long moduloId, Long orden) {
		try {
			String hql="SELECT _pe FROM  PlanificacionEnvio _pe WHERE _pe.modulo.idModulo= :idModulo AND _pe.orden= :orden ";
			Query q=entityManager.createQuery(hql);
			q.setParameter("idModulo", moduloId);
			q.setParameter("orden", orden);
			return (PlanificacionEnvio) q.getSingleResult();
		} catch (Exception e) {
			System.out.println("EjecucionManager.getPlanificacionEnvio(): "+e);
		}
		return null;

	}
	
	public boolean isTip(String enviar){
		return "TIP".equalsIgnoreCase(enviar.trim());

	}
	
	
	private EvaluacionSuscriptor crearEvaluacionSuscriptor(
			EjecucionSuscriptorDetalle detalleEjecucion, long orden) {
		Pregunta _p=getPregunta(detalleEjecucion.getEvaluacion(),orden);
		if (_p==null) {
			System.out.println("No hay preguntas asociadas para la [evaluacion_id: "+detalleEjecucion.getEvaluacion().getIdEvaluacion()+"] y el [orden:"+orden+" ]");
			return null;
		}
		EvaluacionSuscriptor _evs= new EvaluacionSuscriptor();
		_evs.setEjecucionSuscriptorDetalle(detalleEjecucion);
		_evs.setEvaluacion(detalleEjecucion.getEvaluacion());
		_evs.setPregunta(_p);
		_evs.setSuscriptor(detalleEjecucion.getEjecucionSuscriptor().getSuscriptor());
		_evs.setEstadoEvaluacion(EstadoEvaluacionSuscriptor.ACTIVO.name());
		_evs.setFechaAlta(new Date());
		entityManager.persist(_evs);
		return _evs;
	}
	
	private Pregunta getPregunta(Evaluacion evaluacion, long orden) {
		String hql="SELECT _p FROM Pregunta _p WHERE _p.evaluacion= :evaluacion AND _p.ordenPregunta= :orden ";
		Query q= entityManager.createQuery(hql);
		q.setParameter("evaluacion", evaluacion);
		q.setParameter("orden", orden);
		return (Pregunta) q.getSingleResult();
	}

}
