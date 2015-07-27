package py.com.global.educador.jpa.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the LOG_CLIENT_RESPONSE database table.
 * 
 */
@Entity
@Table(name="LOG_CLIENT_RESPONSE")
public class LogClientResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idLogClientResponse;
	private Date fechaRecepcion;
	private String mensaje;
	private Long  idSuscriptor;
	private String numero;
	private String numeroCorto;
	public LogClientResponse() {
	}


	@Id
	@SequenceGenerator(name="LOG_CLIENT_RESPONSE_IDLOGCLIENTRESPONSE_GENERATOR", sequenceName="LOG_CLIENT_RESPONSE_ID_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOG_CLIENT_RESPONSE_IDLOGCLIENTRESPONSE_GENERATOR")
	@Column(name="ID_LOG_CLIENT_RESPONSE")
	public long getIdLogClientResponse() {
		return this.idLogClientResponse;
	}

	public void setIdLogClientResponse(long idLogClientResponse) {
		this.idLogClientResponse = idLogClientResponse;
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

	
	
	@Column(name="NUMERO")
	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column(name="NUMERO_CORTO")
	public String getNumeroCorto() {
		return numeroCorto;
	}


	public void setNumeroCorto(String numeroCorto) {
		this.numeroCorto = numeroCorto;
	}

	@Column(name="ID_SUSCRIPTOR")
	public Long getIdSuscriptor() {
		return idSuscriptor;
	}


	public void setIdSuscriptor(Long idSuscriptor) {
		this.idSuscriptor = idSuscriptor;
	}



	
	

	
}