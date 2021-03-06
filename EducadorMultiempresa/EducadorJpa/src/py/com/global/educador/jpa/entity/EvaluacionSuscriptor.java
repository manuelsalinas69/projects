package py.com.global.educador.jpa.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the EVALUACION_SUSCRIPTOR database table.
 * 
 */
@Entity
@Table(name="EVALUACION_SUSCRIPTOR")
public class EvaluacionSuscriptor implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idEvaluacionSuscriptor;
	private String estadoEvaluacion;
	private Date fechaAlta;
	private Respuesta respuesta;
	private Boolean respuestaCorrecta;
	private EjecucionSuscriptorDetalle ejecucionSuscriptorDetalle;
	private Evaluacion evaluacion;
	private Pregunta pregunta;
	private Suscriptor suscriptor;
	private Date fechaRespuesta;
	private Date fechaEnvioPregunta;
	private Long intento;
	private String respuestaSenderSmsc;
	private String estadoEnvio;
	private String respuestaAbierta;
	

	public EvaluacionSuscriptor() {
	}


	@Id
	@SequenceGenerator(name="EVALUACION_SUSCRIPTOR_IDEVALUACIONSUSCRIPTOR_GENERATOR", sequenceName="EVALUACION_SUSCRIPTOR_ID_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="EVALUACION_SUSCRIPTOR_IDEVALUACIONSUSCRIPTOR_GENERATOR")
	@Column(name="ID_EVALUACION_SUSCRIPTOR")
	public long getIdEvaluacionSuscriptor() {
		return this.idEvaluacionSuscriptor;
	}

	public void setIdEvaluacionSuscriptor(long idEvaluacionSuscriptor) {
		this.idEvaluacionSuscriptor = idEvaluacionSuscriptor;
	}


	@Column(name="ESTADO_EVALUACION")
	public String getEstadoEvaluacion() {
		return this.estadoEvaluacion;
	}

	public void setEstadoEvaluacion(String estadoEvaluacion) {
		this.estadoEvaluacion = estadoEvaluacion;
	}


	@Column(name="FECHA_RESPUESTA")
	public Date getFechaRespuesta() {
		return fechaRespuesta;
	}


	public void setFechaRespuesta(Date fechaRespuesta) {
		this.fechaRespuesta = fechaRespuesta;
	}

	@Column(name="FECHA_ENVIO_PREGUNTA")
	public Date getFechaEnvioPregunta() {
		return fechaEnvioPregunta;
	}


	public void setFechaEnvioPregunta(Date fechaEnvioPregunta) {
		this.fechaEnvioPregunta = fechaEnvioPregunta;
	}


	 
	@Column(name="FECHA_ALTA")
	public Date getFechaAlta() {
		return this.fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}


	
	@ManyToOne
	@JoinColumn(name="ID_RESPUESTA")
	public Respuesta getRespuesta() {
		return respuesta;
	}


	public void setRespuesta(Respuesta respuesta) {
		this.respuesta = respuesta;
	}


	@Column(name="RESPUESTA_CORRECTA")
	public Boolean getRespuestaCorrecta() {
		return this.respuestaCorrecta;
	}

	public void setRespuestaCorrecta(Boolean respuestaCorrecta) {
		this.respuestaCorrecta = respuestaCorrecta;
	}

	
	
	@Column(name="INTENTO")
	public Long getIntento() {
		return intento;
	}



	public void setIntento(Long intento) {
		this.intento = intento;
	}

	@Column(name="RESPUESTA_SENDER_SMSC")
	public String getRespuestaSenderSmsc() {
		return respuestaSenderSmsc;
	}


	public void setRespuestaSenderSmsc(String respuestaSenderSmsc) {
		this.respuestaSenderSmsc = respuestaSenderSmsc;
	}


	
	@Column(name="ESTADO_ENVIO")
	public String getEstadoEnvio() {
		return estadoEnvio;
	}


	public void setEstadoEnvio(String estadoEnvio) {
		this.estadoEnvio = estadoEnvio;
	}

	
	@Column(name="RESPUESTA_ABIERTA")
	public String getRespuestaAbierta() {
		return respuestaAbierta;
	}


	public void setRespuestaAbierta(String respuestaAbierta) {
		this.respuestaAbierta = respuestaAbierta;
	}


	//bi-directional many-to-one association to EjecucionSuscriptorDetalle
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_EJECUCION_DETALLE")
	public EjecucionSuscriptorDetalle getEjecucionSuscriptorDetalle() {
		return this.ejecucionSuscriptorDetalle;
	}

	public void setEjecucionSuscriptorDetalle(EjecucionSuscriptorDetalle ejecucionSuscriptorDetalle) {
		this.ejecucionSuscriptorDetalle = ejecucionSuscriptorDetalle;
	}



	//bi-directional many-to-one association to Evaluacion
	@ManyToOne
	@JoinColumn(name="ID_EVALUACION")
	public Evaluacion getEvaluacion() {
		return this.evaluacion;
	}

	public void setEvaluacion(Evaluacion evaluacion) {
		this.evaluacion = evaluacion;
	}



	//bi-directional many-to-one association to Pregunta
	@ManyToOne
	@JoinColumn(name="ID_PREGUNTA")
	public Pregunta getPregunta() {
		return this.pregunta;
	}

	public void setPregunta(Pregunta pregunta) {
		this.pregunta = pregunta;
	}



	//bi-directional many-to-one association to Suscriptor
	@ManyToOne
	@JoinColumn(name="ID_SUSCRIPTOR")
	public Suscriptor getSuscriptor() {
		return this.suscriptor;
	}

	public void setSuscriptor(Suscriptor suscriptor) {
		this.suscriptor = suscriptor;
	}


}