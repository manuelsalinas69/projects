package py.com.global.educador.engine.dto;

public class CredentialsDto {

	public Long idSuscriptor;
	public Long idEmpresa;
	public String nombreEmpresa;
	public CredentialsDto() {
	}
	
	public CredentialsDto(Long idSuscriptor, Long idEmpresa,
			String nombreEmpresa) {
		this.idSuscriptor = idSuscriptor;
		this.idEmpresa = idEmpresa;
		this.nombreEmpresa = nombreEmpresa;
	}

	public Long getIdSuscriptor() {
		return idSuscriptor;
	}
	public void setIdSuscriptor(Long idSuscriptor) {
		this.idSuscriptor = idSuscriptor;
	}
	public Long getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	public String getNombreEmpresa() {
		return nombreEmpresa;
	}
	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}
	
	
	
	
	
	
}
