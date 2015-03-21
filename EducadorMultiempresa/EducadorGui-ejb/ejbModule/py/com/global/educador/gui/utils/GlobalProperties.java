package py.com.global.educador.gui.utils;

import java.util.Properties;

public class GlobalProperties extends Properties{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public synchronized Object put(Object key, Object value) {
		if (key==null || key.toString().isEmpty() ) {
			return null;
		}
		if (value==null || value.toString().isEmpty()) {
			return null;
		}
		return super.put(key, value);
	}
}
