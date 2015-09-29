package py.com.global.educador.engine.dto;

import java.io.Serializable;
import java.util.Properties;

public class FormularioDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Properties data;
	
	public FormularioDto() {
		data= new Properties();
	}
	
	
	public void putAttr(String attrName,Object attrValue){
		if (data==null) {
			data= new Properties();
		}
		data.put(attrName, attrValue);
	}
	
	
}
