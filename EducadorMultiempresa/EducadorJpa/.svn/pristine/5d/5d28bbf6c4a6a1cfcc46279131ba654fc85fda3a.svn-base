package py.com.global.educador.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;


/**
 * The persistent class for the PROMO database table.
 * 
 */
@Entity
public class Promo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long idPromo;
	private String codigoFfe;
	private String comentarioFfe;
	private String descripcion;
	private String nombre;
	private String parametrosFfe;


	public Promo() {
	}


	@Id
	@SequenceGenerator(name="PROMO_IDPROMO_GENERATOR", sequenceName="PROMO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PROMO_IDPROMO_GENERATOR")
	@Column(name="ID_PROMO")
	public Long getIdPromo() {
		return this.idPromo;
	}

	public void setIdPromo(Long idPromo) {
		this.idPromo = idPromo;
	}


	@Column(name="CODIGO_FFE")
	public String getCodigoFfe() {
		return this.codigoFfe;
	}

	public void setCodigoFfe(String codigoFfe) {
		this.codigoFfe = codigoFfe;
	}


	@Column(name="COMENTARIO_FFE")
	public String getComentarioFfe() {
		return this.comentarioFfe;
	}

	public void setComentarioFfe(String comentarioFfe) {
		this.comentarioFfe = comentarioFfe;
	}


	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	@Column(name="PARAMETROS_FFE")
	public String getParametrosFfe() {
		return this.parametrosFfe;
	}

	public void setParametrosFfe(String parametrosFfe) {
		this.parametrosFfe = parametrosFfe;
	}


}