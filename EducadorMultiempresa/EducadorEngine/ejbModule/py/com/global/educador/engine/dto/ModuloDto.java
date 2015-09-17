package py.com.global.educador.engine.dto;

import java.io.Serializable;

public class ModuloDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Long idModulo;
	public String nombreModulo;
	public Long cantPreguntas;
	public ModuloDto(Long idModulo, String nombreModulo, Long cantPreguntas) {
		this.idModulo = idModulo;
		this.nombreModulo = nombreModulo;
		this.cantPreguntas = cantPreguntas;
	}
	@Override
	public String toString() {
		return "ModuloDto ["
				+ (idModulo != null ? "idModulo=" + idModulo + ", " : "")
				+ (nombreModulo != null ? "nombreModulo=" + nombreModulo + ", "
						: "")
				+ (cantPreguntas != null ? "cantPreguntas=" + cantPreguntas
						: "") + "]";
	}
	
	
}
