package py.com.global.educador.engine.services;

import java.io.Serializable;
import java.util.Date;

public class EjecucionDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Long idEjecucion;
	public Long idModulo;
	public String estadoEjecucion;
	public Date fechaInicio;
	
	public EjecucionDto() {
	}

	public EjecucionDto(Long idEjecucion, Long idModulo,
			String estadoEjecucion, Date fechaInicio) {
		this.idEjecucion = idEjecucion;
		this.idModulo = idModulo;
		this.estadoEjecucion = estadoEjecucion;
		this.fechaInicio = fechaInicio;
	}

	public Long getIdEjecucion() {
		return idEjecucion;
	}

	public void setIdEjecucion(Long idEjecucion) {
		this.idEjecucion = idEjecucion;
	}

	public Long getIdModulo() {
		return idModulo;
	}

	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}

	public String getEstadoEjecucion() {
		return estadoEjecucion;
	}

	public void setEstadoEjecucion(String estadoEjecucion) {
		this.estadoEjecucion = estadoEjecucion;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	@Override
	public String toString() {
		return "EjecucionDto ["
				+ (idEjecucion != null ? "idEjecucion=" + idEjecucion + ", "
						: "")
				+ (idModulo != null ? "idModulo=" + idModulo + ", " : "")
				+ (estadoEjecucion != null ? "estadoEjecucion="
						+ estadoEjecucion + ", " : "")
				+ (fechaInicio != null ? "fechaInicio=" + fechaInicio : "")
				+ "]";
	}

	
	
}
