package py.com.global.educador.jpa.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the LOG_SMS_OUT database table.
 * 
 */
@Entity
@Table(name="LOG_SMS_OUT")
public class LogSmsOut implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idLogSmsOut;
	private String estado;
	private Date fechaPeticionEnvio;
	private Date fechaEnvio;
	private String mensaje;
	private String numeroDestino;
	private String numeroOrigen;
	private String respuestaSmsc;
	private Long messageId;

	public LogSmsOut() {
	}


	@Id
	@SequenceGenerator(name="LOG_SMS_OUT_IDLOGSMSOUT_GENERATOR", sequenceName="LOG_SMS_OUT_ID_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOG_SMS_OUT_IDLOGSMSOUT_GENERATOR")
	@Column(name="ID_LOG_SMS_OUT")
	public long getIdLogSmsOut() {
		return this.idLogSmsOut;
	}

	public void setIdLogSmsOut(long idLogSmsOut) {
		this.idLogSmsOut = idLogSmsOut;
	}


	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}


	 
	@Column(name="FECHA_PETICION_ENVIO")
	public Date getFechaPeticionEnvio() {
		return this.fechaPeticionEnvio;
	}

	public void setFechaPeticionEnvio(Date fechaPeticionEnvio) {
		this.fechaPeticionEnvio = fechaPeticionEnvio;
	}


	public String getMensaje() {
		return this.mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}


	@Column(name="NUMERO_DESTINO")
	public String getNumeroDestino() {
		return this.numeroDestino;
	}

	public void setNumeroDestino(String numeroDestino) {
		this.numeroDestino = numeroDestino;
	}


	@Column(name="NUMERO_ORIGEN")
	public String getNumeroOrigen() {
		return this.numeroOrigen;
	}

	public void setNumeroOrigen(String numeroOrigen) {
		this.numeroOrigen = numeroOrigen;
	}

	@Column(name="RESPUESTA_SMSC")
	public String getRespuestaSmsc() {
		return respuestaSmsc;
	}


	public void setRespuestaSmsc(String respuestaSmsc) {
		this.respuestaSmsc = respuestaSmsc;
	}

	@Column(name="FECHA_ENVIO")
	public Date getFechaEnvio() {
		return fechaEnvio;
	}


	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	@Column(name="MESSAGE_ID")
	public Long getMessageId() {
		return messageId;
	}


	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}
	
	

}