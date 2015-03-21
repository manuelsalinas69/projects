package py.com.global.educador.gui.utils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map.Entry;
import java.util.Properties;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

public abstract class EntityInterface implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Logger logger = Logger.getLogger(EntityInterface.class);
	GlobalProperties p;
	boolean selected;
	public abstract Serializable getId();

	public Properties getProperties() {
		if (p!=null) {
			return p;
		} 
		p= new GlobalProperties();

		Class<?> clazz = this.getClass();

		Method[] methods = clazz.getMethods();
		try {
			for (Method method : methods) {
				if (method.isAnnotationPresent(Column.class)
						|| method.isAnnotationPresent(ManyToOne.class) || method.isAnnotationPresent(Transient.class)) {
					String name = getName(method);
					//Object result= method.invoke(this);
					p.put(name, method.invoke(this));

				}
			}
			
			p.put("id", getId());
		} catch (IllegalArgumentException e) {
			logger.error("Error inesperado-->"+e.getMessage(),e);
		} catch (IllegalAccessException e) {
			logger.error("Error inesperado-->"+e.getMessage(),e);
		} catch (InvocationTargetException e) {
			logger.error("Error inesperado-->"+e.getMessage(),e);
		}

		return p;
	}

	private String getName(Method method) {
		String[] prefixes = {"get","is"};
		String methodName = method.getName();
		
		for (String prefix : prefixes) {
			if (methodName.startsWith(prefix)) {
				
				methodName = methodName.substring(prefix.length());
				String first = methodName.substring(0, 1);
				first = first.toLowerCase();
				methodName = first + methodName.substring(1);
				return methodName;
			}
			
		}
		return methodName;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder= new StringBuilder();
		stringBuilder.append("{");
		for (Entry<Object, Object> entry : getProperties().entrySet()) {
			if (entry.getValue() instanceof EntityInterface) {
				EntityInterface entityInterface= (EntityInterface) entry.getValue();
				stringBuilder.append(entry.getKey()+" : "+entityInterface.getId());
			}
			else {
				stringBuilder.append(entry.toString());
				
			}
			stringBuilder.append(", ");
		}
		stringBuilder.append("}");
		return stringBuilder.toString();
	}

	public Object clone() throws CloneNotSupportedException {

		return (EntityInterface) super.clone();

	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	
	
}
