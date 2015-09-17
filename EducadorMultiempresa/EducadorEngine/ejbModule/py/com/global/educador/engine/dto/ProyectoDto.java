package py.com.global.educador.engine.dto;

import java.io.Serializable;

public class ProyectoDto implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 425717613172184686L;
	public Long projectId;
	public String projectName;
	public String projectDescription;
	
	
	public ProyectoDto(Long projectId, String projectName,
			String projectDescription) {
		this.projectId = projectId;
		this.projectName = projectName;
		this.projectDescription = projectDescription;
	}


	@Override
	public String toString() {
		return "ProyectoDto ["
				+ (projectId != null ? "projectId=" + projectId + ", " : "")
				+ (projectName != null ? "projectName=" + projectName + ", "
						: "")
				+ (projectDescription != null ? "projectDescription="
						+ projectDescription : "") + "]";
	}
	
	
}
