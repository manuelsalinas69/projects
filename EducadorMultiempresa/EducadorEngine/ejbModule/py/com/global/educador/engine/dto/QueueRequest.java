package py.com.global.educador.engine.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class QueueRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1048599066783170972L;
	private Map<String,Object> params= new HashMap<String, Object>();
	
	public Object getParam(String key){
		return params.get(key);
	}
	
	public boolean addParam(String key, Object value){
		if (key==null || value==null || key.trim().isEmpty()) {
			return false;
		}
		return params.put(key, value)!=null;
	}
	
}
