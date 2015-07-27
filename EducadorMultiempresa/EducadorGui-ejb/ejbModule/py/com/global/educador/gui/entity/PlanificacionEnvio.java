package py.com.global.educador.gui.entity;

// Generated Aug 21, 2014 6:09:06 PM by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;
import java.lang.Long;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import py.com.global.educador.gui.utils.EntityInterface;

/**
 * PlanificacionEnvio generated by hbm2java
 */
@Entity
@Table(name = "PLANIFICACION_ENVIO" )
public class PlanificacionEnvio extends EntityInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long idPlanificacionEnvio;
	private Evaluacion evaluacion;
	private Tip tip;
	private Modulo modulo;
	private String enviar;
	private Long orden;
	private Boolean envioFinal;
	private Long esperarHorasAnterior;
	private Long esperarMinutosAnterior;

	public PlanificacionEnvio() {
	}

	public PlanificacionEnvio(Long idPlanificacionEnvio) {
		this.idPlanificacionEnvio = idPlanificacionEnvio;
	}

	public PlanificacionEnvio(Long idPlanificacionEnvio,
			Evaluacion evaluacion, Tip tip, Modulo modulo, String enviar,
			Long orden, Boolean envioFinal,
			Long esperarHorasAnterior) {
		this.idPlanificacionEnvio = idPlanificacionEnvio;
		this.evaluacion = evaluacion;
		this.tip = tip;
		this.modulo = modulo;
		this.enviar = enviar;
		this.orden = orden;
		this.envioFinal = envioFinal;
		this.esperarHorasAnterior = esperarHorasAnterior;
	}

	@Id
	@Column(name = "ID_PLANIFICACION_ENVIO", unique = true, nullable = false, precision = 22, scale = 0)
	@SequenceGenerator(name="PLANIFICACION_ENVIO_IDPLANIFICACIONENVIO_GENERATOR", sequenceName="PLANIFICACION_ENVIO_ID_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PLANIFICACION_ENVIO_IDPLANIFICACIONENVIO_GENERATOR")
	@NotNull
	public Long getIdPlanificacionEnvio() {
		return this.idPlanificacionEnvio;
	}

	public void setIdPlanificacionEnvio(Long idPlanificacionEnvio) {
		this.idPlanificacionEnvio = idPlanificacionEnvio;
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
	@JoinColumn(name = "ID_MODULO")
	public Modulo getModulo() {
		return this.modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

	@Column(name = "ENVIAR", length = 100)
	@Size(max = 100)
	public String getEnviar() {
		return this.enviar;
	}

	public void setEnviar(String enviar) {
		this.enviar = enviar;
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

	@Override
	@Transient
	public Serializable getId() {
		return getIdPlanificacionEnvio();
	}

}