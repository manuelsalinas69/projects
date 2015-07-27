package py.com.global.educador.gui.entity;

// Generated Oct 1, 2014 10:48:10 AM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.Long;

import py.com.global.educador.gui.utils.EntityInterfaceId;

/**
 * ParametroProyectoId generated by hbm2java
 */
@Embeddable
public class ParametroProyectoId extends EntityInterfaceId {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String parametro;
	private Long idProyecto;

	public ParametroProyectoId() {
	}

	public ParametroProyectoId(String parametro, Long idProyecto) {
		this.parametro = parametro;
		this.idProyecto = idProyecto;
	}

	@Column(name = "PARAMETRO", nullable = false, length = 300)
	@NotNull
	@Size(max = 300)
	public String getParametro() {
		return this.parametro;
	}

	public void setParametro(String parametro) {
		this.parametro = parametro;
	}

	@Column(name = "ID_PROYECTO", nullable = false, precision = 22, scale = 0)
	@NotNull
	public Long getIdProyecto() {
		return this.idProyecto;
	}

	public void setIdProyecto(Long idProyecto) {
		this.idProyecto = idProyecto;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ParametroProyectoId))
			return false;
		ParametroProyectoId castOther = (ParametroProyectoId) other;

		return ((this.getParametro() == castOther.getParametro()) || (this
				.getParametro() != null && castOther.getParametro() != null && this
				.getParametro().equals(castOther.getParametro())))
				&& ((this.getIdProyecto() == castOther.getIdProyecto()) || (this
						.getIdProyecto() != null
						&& castOther.getIdProyecto() != null && this
						.getIdProyecto().equals(castOther.getIdProyecto())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getParametro() == null ? 0 : this.getParametro().hashCode());
		result = 37
				* result
				+ (getIdProyecto() == null ? 0 : this.getIdProyecto()
						.hashCode());
		return result;
	}

}