package py.com.global.educador.gui.entity;

// Generated Aug 21, 2014 6:09:06 PM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * SessionTrx generated by hbm2java
 */
@Entity
@Table(name = "SESSION_TRX" )
public class SessionTrx implements java.io.Serializable {

	private BigDecimal idSessionTrx;
	private Suscriptor suscriptor;
	private String numero;
	private String numeroCorto;
	private Date fechaInicioSession;
	private BigDecimal idEvaluacion;
	private BigDecimal idEjecucionDetalle;
	private BigDecimal idEvaluacionSuscriptor;

	public SessionTrx() {
	}

	public SessionTrx(BigDecimal idSessionTrx) {
		this.idSessionTrx = idSessionTrx;
	}

	public SessionTrx(BigDecimal idSessionTrx, Suscriptor suscriptor,
			String numero, String numeroCorto, Date fechaInicioSession,
			BigDecimal idEvaluacion, BigDecimal idEjecucionDetalle,
			BigDecimal idEvaluacionSuscriptor) {
		this.idSessionTrx = idSessionTrx;
		this.suscriptor = suscriptor;
		this.numero = numero;
		this.numeroCorto = numeroCorto;
		this.fechaInicioSession = fechaInicioSession;
		this.idEvaluacion = idEvaluacion;
		this.idEjecucionDetalle = idEjecucionDetalle;
		this.idEvaluacionSuscriptor = idEvaluacionSuscriptor;
	}

	@Id
	@Column(name = "ID_SESSION_TRX", unique = true, nullable = false, precision = 22, scale = 0)
	@NotNull
	public BigDecimal getIdSessionTrx() {
		return this.idSessionTrx;
	}

	public void setIdSessionTrx(BigDecimal idSessionTrx) {
		this.idSessionTrx = idSessionTrx;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SUSCRIPTOR")
	public Suscriptor getSuscriptor() {
		return this.suscriptor;
	}

	public void setSuscriptor(Suscriptor suscriptor) {
		this.suscriptor = suscriptor;
	}

	@Column(name = "NUMERO", length = 30)
	@Size(max = 30)
	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column(name = "NUMERO_CORTO", length = 30)
	@Size(max = 30)
	public String getNumeroCorto() {
		return this.numeroCorto;
	}

	public void setNumeroCorto(String numeroCorto) {
		this.numeroCorto = numeroCorto;
	}

	 
	@Column(name = "FECHA_INICIO_SESSION", length = 7)
	public Date getFechaInicioSession() {
		return this.fechaInicioSession;
	}

	public void setFechaInicioSession(Date fechaInicioSession) {
		this.fechaInicioSession = fechaInicioSession;
	}

	@Column(name = "ID_EVALUACION", precision = 22, scale = 0)
	public BigDecimal getIdEvaluacion() {
		return this.idEvaluacion;
	}

	public void setIdEvaluacion(BigDecimal idEvaluacion) {
		this.idEvaluacion = idEvaluacion;
	}

	@Column(name = "ID_EJECUCION_DETALLE", precision = 22, scale = 0)
	public BigDecimal getIdEjecucionDetalle() {
		return this.idEjecucionDetalle;
	}

	public void setIdEjecucionDetalle(BigDecimal idEjecucionDetalle) {
		this.idEjecucionDetalle = idEjecucionDetalle;
	}

	@Column(name = "ID_EVALUACION_SUSCRIPTOR", precision = 22, scale = 0)
	public BigDecimal getIdEvaluacionSuscriptor() {
		return this.idEvaluacionSuscriptor;
	}

	public void setIdEvaluacionSuscriptor(BigDecimal idEvaluacionSuscriptor) {
		this.idEvaluacionSuscriptor = idEvaluacionSuscriptor;
	}

}