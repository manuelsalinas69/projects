package py.com.global.educador.gui.enums;

public enum FiltroSuscriptor {
	ANY("ANY","Todos"),
	NEWS("NEWS","Nuevos Suscriptores"),
	FINALIZADOS("IN_STATE:FINALIZADO","Nuevos y Finalizados");
	String codigo0;
	String descripcion;
	private FiltroSuscriptor(String codigo0, String descripcion) {
		this.codigo0 = codigo0;
		this.descripcion = descripcion;
	}
	public String getCodigo0() {
		return codigo0;
	}
	public String getDescripcion() {
		return descripcion;
	}
	
	@Override
	public String toString() {
		return getDescripcion();
	}
}
