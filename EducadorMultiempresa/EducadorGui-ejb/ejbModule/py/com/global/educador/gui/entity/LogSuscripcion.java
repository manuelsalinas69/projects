package py.com.global.educador.gui.entity;

// Generated Aug 21, 2014 6:09:06 PM by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;
import java.lang.Long;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import py.com.global.educador.gui.utils.EntityInterface;

/**
 * LogSuscripcion generated by hbm2java
 */
@Entity
@Table(name = "LOG_SUSCRIPCION" )
public class LogSuscripcion extends EntityInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long idLogSuscripcion;
	private String mensaje;
	private String accion;
	private String resultado;
	private Date fechaRecepcion;
	private String numero;
	private String proyecto;
	private Date fechaRespuesta;
	private String tipoSuscripcion;

	public LogSuscripcion() {
	}

	public LogSuscripcion(Long idLogSuscripcion) {
		this.idLogSuscripcion = idLogSuscripcion;
	}

	public LogSuscripcion(Long idLogSuscripcion, String mensaje,
			String accion, String resultado, Date fechaRecepcion,
			String numero, String proyecto, Date fechaRespuesta,
			String tipoSuscripcion) {
		this.idLogSuscripcion = idLogSuscripcion;
		this.mensaje = mensaje;
		this.accion = accion;
		this.resultado = resultado;
		this.fechaRecepcion = fechaRecepcion;
		this.numero = numero;
		this.proyecto = proyecto;
		this.fechaRespuesta = fechaRespuesta;
		this.tipoSuscripcion = tipoSuscripcion;
	}

	@Id
	@Column(name = "ID_LOG_SUSCRIPCION", unique = true, nullable = false, precision = 22, scale = 0)
	@NotNull
	public Long getIdLogSuscripcion() {
		return this.idLogSuscripcion;
	}

	public void setIdLogSuscripcion(Long idLogSuscripcion) {
		this.idLogSuscripcion = idLogSuscripcion;
	}

	@Column(name = "MENSAJE", length = 200)
	@Size(max = 200)
	public String getMensaje() {
		return this.mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	@Column(name = "ACCION", length = 30)
	@Size(max = 30)
	public String getAccion() {
		return this.accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	@Column(name = "RESULTADO", length = 100)
	@Size(max = 100)
	public String getResultado() {
		return this.resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	 
	@Column(name = "FECHA_RECEPCION", length = 7)
	public Date getFechaRecepcion() {
		return this.fechaRecepcion;
	}

	public void setFechaRecepcion(Date fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}

	@Column(name = "NUMERO", length = 30)
	@Size(max = 30)
	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column(name = "PROYECTO", length = 100)
	@Size(max = 100)
	public String getProyecto() {
		return this.proyecto;
	}

	public void setProyecto(String proyecto) {
		this.proyecto = proyecto;
	}

	 
	@Column(name = "FECHA_RESPUESTA", length = 7)
	public Date getFechaRespuesta() {
		return this.fechaRespuesta;
	}

	public void setFechaRespuesta(Date fechaRespuesta) {
		this.fechaRespuesta = fechaRespuesta;
	}

	@Column(name = "TIPO_SUSCRIPCION", length = 30)
	@Size(max = 30)
	public String getTipoSuscripcion() {
		return this.tipoSuscripcion;
	}

	public void setTipoSuscripcion(String tipoSuscripcion) {
		this.tipoSuscripcion = tipoSuscripcion;
	}

	@Override
	@Transient
	public Serializable getId() {
		return getIdLogSuscripcion();
	}

}