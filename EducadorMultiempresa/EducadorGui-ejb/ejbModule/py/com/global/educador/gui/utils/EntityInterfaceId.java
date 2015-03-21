package py.com.global.educador.gui.utils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.persistence.Column;
import javax.persistence.ManyToOne;

import org.apache.log4j.Logger;

public abstract class EntityInterfaceId implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Logger logger = Logger.getLogger(EntityInterfaceId.class);

	GlobalProperties p;

	public Properties getProperties() {
		if (p!=null) {
			return p;
		}
		p = new GlobalProperties();

		Class<?> clazz = this.getClass();

		Method[] methods = clazz.getMethods();
		try {
			for (Method method : methods) {
				if (method.isAnnotationPresent(Column.class)
						|| method.isAnnotationPresent(ManyToOne.class)) {
					String name = getName(method);

					p.put(name, method.invoke(this));

				}
			}
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
		String prefix = "get";
		String methodName = method.getName();
		if (methodName.startsWith(prefix)) {

			methodName = methodName.substring(prefix.length());
			String first = methodName.substring(0, 1);
			first = first.toLowerCase();
			methodName = first + methodName.substring(1);
		}

		return methodName;
	}

	@Override
	public String toString() {
		return getProperties().toString();
	}

	public Object clone() throws CloneNotSupportedException {

		return (EntityInterfaceId) super.clone();

	}
}
