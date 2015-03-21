package py.com.global.educador.gui.utils;

import java.util.Comparator;

import org.apache.log4j.Logger;

import py.com.global.educador.gui.entity.Respuesta;

public class RespuestasComparator implements Comparator<Respuesta>{

	Logger log=Logger.getLogger(RespuestasComparator.class);
	
	@Override
	public int compare(Respuesta paramT1, Respuesta paramT2) {
		if (paramT1==null || paramT2==null) {
			return 0;
		}
		if (paramT1.getOrdenRespuesta()==null || paramT1.getOrdenRespuesta().trim().isEmpty()) {
			return 0;
		}
		
		if (paramT2.getOrdenRespuesta()==null || paramT2.getOrdenRespuesta().trim().isEmpty()) {
			return 0;
		}
		
		try {
			Integer oT1=Integer.parseInt(paramT1.getOrdenRespuesta());
			Integer oT2=Integer.parseInt(paramT2.getOrdenRespuesta());
			
			return oT1.compareTo(oT2);
		} catch (NumberFormatException e) {
			log.error(e);
		}
		return 0;
	}

}
