package py.com.global.educador.engine.enums;

public enum ServiceStatus {

	OK(0,"OK"),
	UNKNOM_ERROR(-1,"Error desconocido");
	
	int code;
	String descripcion;
	private ServiceStatus(int code, String descripcion) {
		this.code = code;
		this.descripcion = descripcion;
	}
	public int getCode() {
		return code;
	}
	public String getDescripcion() {
		return descripcion;
	}
	
	
	
}
