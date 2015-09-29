package py.com.global.educador.engine.app.managers;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import py.com.global.educador.engine.dto.FormularioDto;
import py.com.global.educador.engine.enums.EstadoEjecucionSuscriptor;
import py.com.global.educador.engine.enums.EstadoEjecucionSuscriptorDetalle;
import py.com.global.educador.jpa.entity.EjecucionSuscriptor;
import py.com.global.educador.jpa.entity.EjecucionSuscriptorDetalle;
import py.com.global.educador.jpa.entity.EvaluacionSuscriptor;
import py.com.global.educador.jpa.entity.Modulo;
import py.com.global.educador.jpa.entity.PlanificacionEnvio;
import py.com.global.educador.jpa.entity.Pregunta;
import py.com.global.educador.jpa.entity.Suscriptor;

@Stateless
public class EjecucionManager {

	
	@PersistenceContext 
	EntityManager entityManager;
	
	public FormularioDto nuevaFormulario(Long idModulo, Long idSuscriptor){
		EjecucionSuscriptor ejecucionNueva=null;
		EjecucionSuscriptorDetalle det;
		try {
			ejecucionNueva= createEjecucionSubcriber0(idModulo, idSuscriptor, EstadoEjecucionSuscriptor.ACTIVO);
			det= crearEjecucionSuscriptorDetalle(ejecucionNueva, 1);
			FormularioDto dto= createForm(ejecucionNueva, det, getEvaluacion(det));
			return dto;
		} catch (Exception e) {
			System.out.println("AppServices.createEvaluacion(): "+e);
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	private EvaluacionSuscriptor getEvaluacion(EjecucionSuscriptorDetalle det) {
		// TODO Auto-generated method stub
		return null;
	}



	private FormularioDto createForm(EjecucionSuscriptor ej,
			EjecucionSuscriptorDetalle det, EvaluacionSuscriptor ev) {
		
		FormularioDto d= new FormularioDto();
		
		d.putAttr("idEjecucion", ej.getIdEjecucionSuscriptor());
		d.putAttr("idDetalleEjecucion", det.getIdEjecucionDetalle());
		d.putAttr("formType", det.getEnviar());
		if (isTip(det.getEnviar())) {
			d.putAttr("info", det.getTip().getContenido());
		}
		else{
			FormularioDto evDto=new FormularioDto();
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



	private Object getRespuestas(Pregunta pregunta) {
		// TODO Auto-generated method stub
		return null;
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
}
