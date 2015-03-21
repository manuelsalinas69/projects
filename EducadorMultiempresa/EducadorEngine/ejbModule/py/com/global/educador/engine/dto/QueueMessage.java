package py.com.global.educador.engine.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class QueueMessage {

	
	
	private Map<String,Object> params;
	
	public QueueMessage() {
		params= new HashMap<String, Object>();
	}
	
	public QueueMessage(Map<String, Object> params) {
		this.params = params;
	}

	public Object getParam(String key){
		return params.get(key);
	}
	
	public boolean addParam(String key, Object value){
		if (key==null || value==null || key.trim().isEmpty()) {
			return false;
		}
		if (params==null) {
			params= new HashMap<String, Object>();
		}
		return params.put(key, value)!=null;
	}
	
	public Map<String, Object> getAllParams(){
		return params;
	}

	@Override
	public String toString() {
		return "QueueMessage [" + (params != null ? "params=" + params : "")
				+ "]";
	}
	
}
