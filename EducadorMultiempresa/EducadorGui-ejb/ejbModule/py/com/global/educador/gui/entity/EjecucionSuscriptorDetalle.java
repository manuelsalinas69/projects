package py.com.global.educador.gui.entity;

// Generated Aug 21, 2014 6:09:06 PM by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import py.com.global.educador.gui.utils.EntityInterface;

/**
 * EjecucionSuscriptorDetalle generated by hbm2java
 */
@Entity
@Table(name = "EJECUCION_SUSCRIPTOR_DETALLE" )
public class EjecucionSuscriptorDetalle extends EntityInterface {

	private Long idEjecucionDetalle;
	private Evaluacion evaluacion;
	private Tip tip;
	private EjecucionSuscriptor ejecucionSuscriptor;
	private String estadoEjecucion;
	private Long orden;
	private Boolean envioFinal;
	private String enviar;
	private Long esperarHorasAnterior;
	private Long esperarMinutosAnterior;
	private Date fechaEjecucion;
	private Date fechaEnvio;
	private String respuestaSmsc;
	private Set<EvaluacionSuscriptor> evaluacionSuscriptors = new HashSet<EvaluacionSuscriptor>(
			0);

	public EjecucionSuscriptorDetalle() {
	}

	public EjecucionSuscriptorDetalle(Long idEjecucionDetalle) {
		this.idEjecucionDetalle = idEjecucionDetalle;
	}

	public EjecucionSuscriptorDetalle(Long idEjecucionDetalle,
			Evaluacion evaluacion, Tip tip,
			EjecucionSuscriptor ejecucionSuscriptor, String estadoEjecucion,
			Long orden, Boolean envioFinal, String enviar,
			Long esperarHorasAnterior, Date fechaEjecucion,
			Date fechaEnvio, String respuestaSmsc,
			Set<EvaluacionSuscriptor> evaluacionSuscriptors) {
		this.idEjecucionDetalle = idEjecucionDetalle;
		this.evaluacion = evaluacion;
		this.tip = tip;
		this.ejecucionSuscriptor = ejecucionSuscriptor;
		this.estadoEjecucion = estadoEjecucion;
		this.orden = orden;
		this.envioFinal = envioFinal;
		this.enviar = enviar;
		this.esperarHorasAnterior = esperarHorasAnterior;
		this.fechaEjecucion = fechaEjecucion;
		this.fechaEnvio = fechaEnvio;
		this.respuestaSmsc = respuestaSmsc;
		this.evaluacionSuscriptors = evaluacionSuscriptors;
	}

	@Id
	@Column(name = "ID_EJECUCION_DETALLE", unique = true, nullable = false, precision = 22, scale = 0)
	@NotNull
	public Long getIdEjecucionDetalle() {
		return this.idEjecucionDetalle;
	}

	public void setIdEjecucionDetalle(Long idEjecucionDetalle) {
		this.idEjecucionDetalle = idEjecucionDetalle;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_EVALUACION")
	public Evaluacion getEvaluacion() {
		return this.evaluacion;
	}

	public void setEvaluacion(Evaluacion evaluacion) {
		this.evaluacion = evaluacion;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TIP")
	public Tip getTip() {
		return this.tip;
	}

	public void setTip(Tip tip) {
		this.tip = tip;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_EJECUCION_SUSCRIPTOR")
	public EjecucionSuscriptor getEjecucionSuscriptor() {
		return this.ejecucionSuscriptor;
	}

	public void setEjecucionSuscriptor(EjecucionSuscriptor ejecucionSuscriptor) {
		this.ejecucionSuscriptor = ejecucionSuscriptor;
	}

	@Column(name = "ESTADO_EJECUCION", length = 30)
	@Size(max = 30)
	public String getEstadoEjecucion() {
		return this.estadoEjecucion;
	}

	public void setEstadoEjecucion(String estadoEjecucion) {
		this.estadoEjecucion = estadoEjecucion;
	}

	@Column(name = "ORDEN", precision = 22, scale = 0)
	public Long getOrden() {
		return this.orden;
	}

	public void setOrden(Long orden) {
		this.orden = orden;
	}

	@Column(name = "ENVIO_FINAL", precision = 1, scale = 0)
	public Boolean getEnvioFinal() {
		return this.envioFinal;
	}

	public void setEnvioFinal(Boolean envioFinal) {
		this.envioFinal = envioFinal;
	}

	@Column(name = "ENVIAR", length = 50)
	@Size(max = 50)
	public String getEnviar() {
		return this.enviar;
	}

	public void setEnviar(String enviar) {
		this.enviar = enviar;
	}

	@Column(name = "ESPERAR_HORAS_ANTERIOR", precision = 22, scale = 0)
	public Long getEsperarHorasAnterior() {
		return this.esperarHorasAnterior;
	}

	public void setEsperarHorasAnterior(Long esperarHorasAnterior) {
		this.esperarHorasAnterior = esperarHorasAnterior;
	}

	
	@Column(name="ESPERAR_MINUTOS_ANTERIOR",precision=22, scale=0) 
	public Long getEsperarMinutosAnterior() {
		return esperarMinutosAnterior;
	}

	public void setEsperarMinutosAnterior(Long esperarMinutosAnterior) {
		this.esperarMinutosAnterior = esperarMinutosAnterior;
	}

	@Column(name = "FECHA_EJECUCION", length = 7)
	public Date getFechaEjecucion() {
		return this.fechaEjecucion;
	}

	public void setFechaEjecucion(Date fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}

	 
	@Column(name = "FECHA_ENVIO", length = 7)
	public Date getFechaEnvio() {
		return this.fechaEnvio;
	}

	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	@Column(name = "RESPUESTA_SMSC", length = 50)
	@Size(max = 50)
	public String getRespuestaSmsc() {
		return this.respuestaSmsc;
	}

	public void setRespuestaSmsc(String respuestaSmsc) {
		this.respuestaSmsc = respuestaSmsc;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ejecucionSuscriptorDetalle")
	public Set<EvaluacionSuscriptor> getEvaluacionSuscriptors() {
		return this.evaluacionSuscriptors;
	}

	public void setEvaluacionSuscriptors(
			Set<EvaluacionSuscriptor> evaluacionSuscriptors) {
		this.evaluacionSuscriptors = evaluacionSuscriptors;
	}

	@Override
	@Transient
	public Serializable getId() {
		return getIdEjecucionDetalle();
	}

}