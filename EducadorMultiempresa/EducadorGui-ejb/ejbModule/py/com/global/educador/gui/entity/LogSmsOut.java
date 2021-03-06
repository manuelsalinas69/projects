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
 * LogSmsOut generated by hbm2java
 */
@Entity
@Table(name = "LOG_SMS_OUT" )
public class LogSmsOut extends EntityInterface {

	private Long idLogSmsOut;
	private String numeroDestino;
	private String numeroOrigen;
	private String mensaje;
	private Date fechaPeticionEnvio;
	private String estado;
	private String respuestaSmsc;
	private Date fechaEnvio;
	private Long messageId;

	public LogSmsOut() {
	}

	public LogSmsOut(Long idLogSmsOut) {
		this.idLogSmsOut = idLogSmsOut;
	}

	public LogSmsOut(Long idLogSmsOut, String numeroDestino,
			String numeroOrigen, String mensaje, Date fechaPeticionEnvio,
			String estado, String respuestaSmsc, Date fechaEnvio,
			Long messageId) {
		this.idLogSmsOut = idLogSmsOut;
		this.numeroDestino = numeroDestino;
		this.numeroOrigen = numeroOrigen;
		this.mensaje = mensaje;
		this.fechaPeticionEnvio = fechaPeticionEnvio;
		this.estado = estado;
		this.respuestaSmsc = respuestaSmsc;
		this.fechaEnvio = fechaEnvio;
		this.messageId = messageId;
	}

	@Id
	@Column(name = "ID_LOG_SMS_OUT", unique = true, nullable = false, precision = 22, scale = 0)
	@NotNull
	public Long getIdLogSmsOut() {
		return this.idLogSmsOut;
	}

	public void setIdLogSmsOut(Long idLogSmsOut) {
		this.idLogSmsOut = idLogSmsOut;
	}

	@Column(name = "NUMERO_DESTINO", length = 30)
	@Size(max = 30)
	public String getNumeroDestino() {
		return this.numeroDestino;
	}

	public void setNumeroDestino(String numeroDestino) {
		this.numeroDestino = numeroDestino;
	}

	@Column(name = "NUMERO_ORIGEN", length = 30)
	@Size(max = 30)
	public String getNumeroOrigen() {
		return this.numeroOrigen;
	}

	public void setNumeroOrigen(String numeroOrigen) {
		this.numeroOrigen = numeroOrigen;
	}

	@Column(name = "MENSAJE", length = 200)
	@Size(max = 200)
	public String getMensaje() {
		return this.mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	 
	@Column(name = "FECHA_PETICION_ENVIO", length = 7)
	public Date getFechaPeticionEnvio() {
		return this.fechaPeticionEnvio;
	}

	public void setFechaPeticionEnvio(Date fechaPeticionEnvio) {
		this.fechaPeticionEnvio = fechaPeticionEnvio;
	}

	@Column(name = "ESTADO", length = 30)
	@Size(max = 30)
	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Column(name = "RESPUESTA_SMSC", length = 500)
	@Size(max = 500)
	public String getRespuestaSmsc() {
		return this.respuestaSmsc;
	}

	public void setRespuestaSmsc(String respuestaSmsc) {
		this.respuestaSmsc = respuestaSmsc;
	}

	 
	@Column(name = "FECHA_ENVIO", length = 7)
	public Date getFechaEnvio() {
		return this.fechaEnvio;
	}

	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	@Column(name = "MESSAGE_ID", precision = 22, scale = 0)
	public Long getMessageId() {
		return this.messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	@Override
	@Transient
	public Serializable getId() {
		return getIdLogSmsOut();
	}

}
