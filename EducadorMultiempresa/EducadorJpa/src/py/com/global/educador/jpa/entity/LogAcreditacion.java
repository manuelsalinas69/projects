package py.com.global.educador.jpa.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.lang.Long;
import java.util.Date;


/**
 * The persistent class for the LOG_ACREDITACION database table.
 * 
 */
@Entity
@Table(name="LOG_ACREDITACION")
public class LogAcreditacion implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long idLogAcreditacion;
	private Long codRespuestaFfe;
	private String codigoProductoFfe;
	private String comentarioFfe;
	private String descRespuestaFfe;
	private Long idPromo;
	private String numero;
	private String parametrosAcreditacion;
	private String plataforma;
	private String observacionAcreditacion;
	private Date fechaEjecucion;

	public LogAcreditacion() {
	}


	@Id
	@SequenceGenerator(name="LOG_ACREDITACION_IDLOGACREDITACION_GENERATOR", sequenceName="LOG_ACREDITACION_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOG_ACREDITACION_IDLOGACREDITACION_GENERATOR")
	@Column(name="ID_LOG_ACREDITACION")
	public Long getIdLogAcreditacion() {
		return this.idLogAcreditacion;
	}

	public void setIdLogAcreditacion(Long idLogAcreditacion) {
		this.idLogAcreditacion = idLogAcreditacion;
	}


	@Column(name="COD_RESPUESTA_FFE")
	public Long getCodRespuestaFfe() {
		return this.codRespuestaFfe;
	}

	public void setCodRespuestaFfe(Long codRespuestaFfe) {
		this.codRespuestaFfe = codRespuestaFfe;
	}


	@Column(name="CODIGO_PRODUCTO_FFE")
	public String getCodigoProductoFfe() {
		return this.codigoProductoFfe;
	}

	public void setCodigoProductoFfe(String codigoProductoFfe) {
		this.codigoProductoFfe = codigoProductoFfe;
	}


	@Column(name="COMENTARIO_FFE")
	public String getComentarioFfe() {
		return this.comentarioFfe;
	}

	public void setComentarioFfe(String comentarioFfe) {
		this.comentarioFfe = comentarioFfe;
	}


	@Column(name="DESC_RESPUESTA_FFE")
	public String getDescRespuestaFfe() {
		return this.descRespuestaFfe;
	}

	public void setDescRespuestaFfe(String descRespuestaFfe) {
		this.descRespuestaFfe = descRespuestaFfe;
	}


	@Column(name="ID_PROMO")
	public Long getIdPromo() {
		return this.idPromo;
	}

	public void setIdPromo(Long idPromo) {
		this.idPromo = idPromo;
	}


	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}


	@Column(name="PARAMETROS_ACREDITACION")
	public String getParametrosAcreditacion() {
		return this.parametrosAcreditacion;
	}

	public void setParametrosAcreditacion(String parametrosAcreditacion) {
		this.parametrosAcreditacion = parametrosAcreditacion;
	}


	public String getPlataforma() {
		return this.plataforma;
	}

	public void setPlataforma(String plataforma) {
		this.plataforma = plataforma;
	}

	@Column(name="OBSERVACION_ACREDITACION")
	public String getObservacionAcreditacion() {
		return observacionAcreditacion;
	}


	public void setObservacionAcreditacion(String observacionAcreditacion) {
		this.observacionAcreditacion = observacionAcreditacion;
	}

	@Column(name="FECHA_EJECUCION")
	public Date getFechaEjecucion() {
		return fechaEjecucion;
	}


	public void setFechaEjecucion(Date fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}

	
}