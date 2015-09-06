package py.com.global.educador.gui.managers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.event.AjaxBehaviorEvent;

import org.apache.log4j.Logger;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;

import py.com.global.educador.gui.dto.ValidationResult;
import py.com.global.educador.gui.entity.Pregunta;
import py.com.global.educador.gui.entity.Respuesta;
import py.com.global.educador.gui.enums.EstadoRegistro;
import py.com.global.educador.gui.utils.ModalPattern;

@Name("preguntaAdder")
@Scope(ScopeType.CONVERSATION)
public class PreguntaAdder extends ModalPattern implements Serializable{



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In StatusMessages statusMessages;

	String contenidoPregunta;
	String contenidoRespuesta;
	Boolean preguntaAbierta;
	List<Respuesta> respuestas;

	Logger log=Logger.getLogger(PreguntaAdder.class);

	@Create
	public void init(){
		respuestas= new ArrayList<Respuesta>();
	}

	public void createNewPregunta(){
		contenidoPregunta= null;
		contenidoRespuesta=null;
		preguntaAbierta=null;
		respuestas= new ArrayList<Respuesta>();
	}

	public void esRespuestaChanged(AjaxBehaviorEvent event){
		Integer index=(Integer) event.getComponent().getAttributes().get("index");
		if (index==null) {
			return;
		}
		for (int i = 0; i < respuestas.size(); i++) {
			if (i==index.intValue()) {
				continue;
			}
			respuestas.get(i).setEsRespuestaCorrecta(false);

		}
	}

	public Pregunta getPreguntaToAdd(){
		setHideModal(false);
		ValidationResult v= checkData();
		if (v==null) {
			return null;
		}
		if (!v.isSuccessful()) {
			statusMessages.add(Severity.ERROR,v.getMessage());
			return null;
		}

		Pregunta p = new Pregunta();
		p.setContenidoPregunta(contenidoPregunta);
		p.setEstadoRegistro(EstadoRegistro.ACTIVO.name());
		p.setOrdenPregunta(1L);
		p.setPreguntaFinal(true);
		p.setPreguntaAbierta(preguntaAbierta);

		if (preguntaAbierta==null || !preguntaAbierta) {
			for (int i = 0; i < respuestas.size(); i++) {
				respuestas.get(i).setOrdenRespuesta((i+1)+"");
				respuestas.get(i).setValorEsperado((i+1)+"");


			}
			p.getRespuestas().addAll(respuestas);
		}
		
		

		setHideModal(true);
		return p;

	}

	public ValidationResult checkData(){
		if (contenidoPregunta==null || contenidoPregunta.trim().isEmpty()) {
			return new ValidationResult(false, "El contenido de la pregunta es nulo o vacio.");
		}

		if ((preguntaAbierta==null || !preguntaAbierta) && (respuestas==null || respuestas.isEmpty())) {
			return new ValidationResult(false, "La lista de respuesta");
		}

		return new ValidationResult(true,null);
	}

	public void addRespuesta(){
		if (contenidoRespuesta==null || contenidoRespuesta.trim().isEmpty()) {
			statusMessages.add(Severity.ERROR,"El contenido de la respuesta a agregar es vacio");
			return;
		}

		if (contenidoRespuestaAlreadyInList(respuestas, contenidoRespuesta)) {
			statusMessages.add(Severity.ERROR,"La respuesta ingresada ya existe en la lista");
			return;
		}
		Respuesta r= new Respuesta();
		r.setContenidoRespuesta(contenidoRespuesta);
		r.setEstadoRegistro(EstadoRegistro.ACTIVO.name());
		respuestas.add(r);
	}

	private boolean contenidoRespuestaAlreadyInList(
			List<Respuesta> l, String s) {
		try {
			if (l==null || l.isEmpty() || s==null ||  s.trim().isEmpty()) {
				return false;
			}
			for (Respuesta _r : l) {
				if (_r.getContenidoRespuesta().trim().equalsIgnoreCase(s)) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			log.error(e);
		}
		return false;
	}

	public void upPregunta(int index){
		if (index==0) {
			return;
		}

		Collections.swap(respuestas, index, index-1);

	}

	public void downPregunta(int index){
		if (index>=(respuestas.size()-1)) {
			return;
		}
		Collections.swap(respuestas, index, index+1);

	}
	public void removeRespuesta(int index){
		respuestas.remove(index);
	}

	public String getContenidoPregunta() {
		return contenidoPregunta;
	}

	public void setContenidoPregunta(String contenidoPregunta) {
		this.contenidoPregunta = contenidoPregunta;
	}

	public List<Respuesta> getRespuestas() {
		return respuestas;
	}

	public void setRespuestas(List<Respuesta> respuestas) {
		this.respuestas = respuestas;
	}


	public String getContenidoRespuesta() {
		return contenidoRespuesta;
	}

	public void setContenidoRespuesta(String contenidoRespuesta) {
		this.contenidoRespuesta = contenidoRespuesta;
	}

	public Boolean getPreguntaAbierta() {
		return preguntaAbierta;
	}

	public void setPreguntaAbierta(Boolean preguntaAbierta) {
		this.preguntaAbierta = preguntaAbierta;
	}




}
