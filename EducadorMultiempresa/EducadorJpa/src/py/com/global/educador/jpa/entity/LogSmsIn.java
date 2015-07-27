package py.com.global.educador.jpa.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the LOG_SMS_IN database table.
 * 
 */
@Entity
@Table(name="LOG_SMS_IN")
public class LogSmsIn implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idLogSmsIn;
	private Date fechaRecepcion;
	private String mensaje;
	private String numeroDestino;
	private String numeroOrigen;
	private String jmsMessageId;
	public LogSmsIn() {
	}


	@Id
	@SequenceGenerator(name="LOG_SMS_IN_IDLOGSMSIN_GENERATOR", sequenceName="LOG_SMS_IN_ID_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOG_SMS_IN_IDLOGSMSIN_GENERATOR")
	@Column(name="ID_LOG_SMS_IN")
	public long getIdLogSmsIn() {
		return this.idLogSmsIn;
	}

	public void setIdLogSmsIn(long idLogSmsIn) {
		this.idLogSmsIn = idLogSmsIn;
	}


	 
	@Column(name="FECHA_RECEPCION")
	public Date getFechaRecepcion() {
		return this.fechaRecepcion;
	}

	public void setFechaRecepcion(Date fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
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

	@Column(name="JMS_MESSAGE_ID")
	public String getJmsMessageId() {
		return jmsMessageId;
	}


	public void setJmsMessageId(String jmsMessageId) {
		this.jmsMessageId = jmsMessageId;
	}
	
	

}