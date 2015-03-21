package py.com.global.educador.gui.enums;

import py.com.global.educador.gui.utils.GeneralHelper;

public enum ScheduleDaysOfWeek {
	//{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}
	/*
	 * 	GENERICO_DOMINGO=Domingo
		GENERICO_LUNES=Lunes
		GENERICO_MARTES=Martes
		GENERICO_MIERCOLES=Miércoles
		GENERICO_JUEVES=Jueves
		GENERICO_VIERNES=Viernes
		GENERICO_SABADO=Sábado
	 * */
	DOMINGO("Sun","GENERICO_DOMINGO"),
	LUNES("Mon","GENERICO_LUNES"),
	MARTES("Tue","GENERICO_MARTES"),
	MIERCOLES("Wed","GENERICO_MIERCOLES"),
	JUEVES("Thu","GENERICO_JUEVES"),
	VIERNES("Fri","GENERICO_VIERNES"),
	SAT("Sat","GENERICO_SABADO");
	
	String codigo0;
	String description;
	private ScheduleDaysOfWeek(String codigo0, String description) {
		this.codigo0 = codigo0;
		this.description = description;
	}
	public String getCodigo0() {
		return codigo0;
	}
	public String getDescription() {
		return description;
	}
	
	public static String [] allDaysCodigo0(){
		String[] dowArr= new String[7];
		int i=0;
		for (ScheduleDaysOfWeek _dow : ScheduleDaysOfWeek.values()) {
			dowArr[i]=_dow.getCodigo0();
			i++;
		}
		
		return dowArr;
	}
	
	@Override
	public String toString() {
		return GeneralHelper.getMessages(getDescription());
	}
}
