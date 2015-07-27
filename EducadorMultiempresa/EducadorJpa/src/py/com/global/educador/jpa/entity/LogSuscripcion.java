package py.com.global.educador.jpa.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the LOG_SUSCRIPCION database table.
 * 
 */
@Entity
@Table(name="LOG_SUSCRIPCION")
public class LogSuscripcion implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idLogSuscripcion;
	private String accion;
	private Date fechaRecepcion;
	private Date fechaRespuesta;
	private String mensaje;
	private String numero;
	private String proyecto;
	private String resultado;
	private String tipoSuscripcion;

	public LogSuscripcion() {
	}


	@Id
	@SequenceGenerator(name="LOG_SUSCRIPCION_IDLOGSUSCRIPCION_GENERATOR", sequenceName="LOG_SUSCRIPCION_ID_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOG_SUSCRIPCION_IDLOGSUSCRIPCION_GENERATOR")
	@Column(name="ID_LOG_SUSCRIPCION")
	public long getIdLogSuscripcion() {
		return this.idLogSuscripcion;
	}

	public void setIdLogSuscripcion(long idLogSuscripcion) {
		this.idLogSuscripcion = idLogSuscripcion;
	}


	public String getAccion() {
		return this.accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}


	 
	@Column(name="FECHA_RECEPCION")
	public Date getFechaRecepcion() {
		return this.fechaRecepcion;
	}

	public void setFechaRecepcion(Date fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}


	 
	@Column(name="FECHA_RESPUESTA")
	public Date getFechaRespuesta() {
		return this.fechaRespuesta;
	}

	public void setFechaRespuesta(Date fechaRespuesta) {
		this.fechaRespuesta = fechaRespuesta;
	}


	public String getMensaje() {
		return this.mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}


	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}


	public String getProyecto() {
		return this.proyecto;
	}

	public void setProyecto(String proyecto) {
		this.proyecto = proyecto;
	}


	public String getResultado() {
		return this.resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}


	@Column(name="TIPO_SUSCRIPCION")
	public String getTipoSuscripcion() {
		return this.tipoSuscripcion;
	}

	public void setTipoSuscripcion(String tipoSuscripcion) {
		this.tipoSuscripcion = tipoSuscripcion;
	}

}