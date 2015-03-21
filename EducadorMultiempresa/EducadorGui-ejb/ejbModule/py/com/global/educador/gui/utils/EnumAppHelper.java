/**
 * 
 */
package py.com.global.educador.gui.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.SeamResourceBundle;



/**
 * @author msalinas
 * 
 */
@Name("enumAppHelper")
@Scope(ScopeType.APPLICATION)
public class EnumAppHelper implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private static final String PACK_ENUM="py.com.global.educador.gui.enums.";


	static Logger logger=Logger.getLogger(EnumAppHelper.class);

	public static <T extends Enum<T>> T getEnum(Class<T> clase, String name) {
		return name == null || name.trim().isEmpty() ? null : javax.persistence.EnumType.valueOf(clase, name);

	}

	@SuppressWarnings( { "unchecked", "rawtypes"})
	public static String valueOfEnum(String type, String name) {
		try {
			if (type==null || name==null || type.trim().isEmpty() || name.trim().isEmpty()) {
				return null;
			}

			return Enum.valueOf((Class<Enum>) Class.forName(PACK_ENUM + type), name).toString();
		} catch (ClassNotFoundException e) {
			logger.trace("General.valueOfEnum(): No se encontro la clase pasada como paramentro");
			return name;
		} catch (Exception e) {
			logger.trace("General.valueOfEnum(): " + e);
			return name;
		}

	}

	@SuppressWarnings( { "unchecked", "rawtypes" })
	public static String valueOfEnum(String type, String metodName, Object fieldValue) {
		try {
			Class<Enum> enumClass = (Class<Enum>) Class.forName(PACK_ENUM + type);

			for (Enum o : enumClass.getEnumConstants()) {
				if (o.getClass().getMethod(metodName).invoke(o).toString().equalsIgnoreCase(fieldValue.toString().trim())) {
					return o.toString();
				}
			}
		} catch (Exception e) {
			logger.trace("EnumAppHelper.valueOfEnum(): " + e);
		}
		return null;
	}

	public static String valueOfEnum(String type, String metodName, String fieldValue, String fieldToDisplay) {
		try {
			@SuppressWarnings("unchecked")
			Class<Enum<?>> enumClass = (Class<Enum<?>>) Class.forName(PACK_ENUM + type);

			for (Enum<?> o : enumClass.getEnumConstants()) {
				if (o.getClass().getMethod(metodName).invoke(o).toString().equalsIgnoreCase(fieldValue)) {
					return o.getClass().getMethod(fieldToDisplay).invoke(o).toString();
				}
			}
		} catch (Exception e) {
			logger.trace("EnumAppHelper.valueOfEnum(): " + e);
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<SelectItem> enumsValueItems(String type, boolean mostrarSelect) {
		List<SelectItem> lista = new ArrayList<SelectItem>();

		if (GeneralHelper.getString(type) == null) {
			return lista;
		}

		if (mostrarSelect) {
			lista.add(new SelectItem(null, SeamResourceBundle.getBundle().getString("opt_select_one")));
		}
		try {

			Class<Enum> enumerator = (Class<Enum>) Class.forName(PACK_ENUM + type);
			for (Enum o : enumerator.getEnumConstants()) {
				lista.add(new SelectItem(o.name(), o.toString()));
			}
		} catch (Exception e) {
			logger.error("enumsValueItems",e);
			lista.add(new SelectItem(null, "Error al Cargar el enum. Nombre invalido o no se encuentra."));
		}

		return lista;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<SelectItem> enumsValueItems(String type, String metodName, boolean... mostrarSelect) {
		List<SelectItem> lista = new ArrayList<SelectItem>();

		if (GeneralHelper.getString(type) == null) {
			return lista;
		}

		if (mostrarSelect.length == 0) {
			mostrarSelect = new boolean[1];
			mostrarSelect[0] = true;
		}
		if (mostrarSelect[0]) {
			lista.add(new SelectItem(null, SeamResourceBundle.getBundle().getString("opt_select_one")));
		}
		try {

			Class<Enum> enumerator = (Class<Enum>) Class.forName(PACK_ENUM + type);

			for (Enum o : enumerator.getEnumConstants()) {

				lista.add(new SelectItem(o.getClass().getMethod(metodName).invoke(o), o.toString()));
			}
		} catch (Exception e) {
			logger.error("enumsValueItems [withMethodName]",e);
			lista.add(new SelectItem(null, "Error al Cargar el enum. Nombre invalido o no se encuentra."));
		}

		return lista;
	}
	
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	   	public List<SelectItem> enumsValueItemsEspecific(String type, String metodName, boolean mostrarSelect, String labelMethod, String params) {
	           List<SelectItem> lista = new ArrayList<SelectItem>();

	           if (GeneralHelper.getString(type) == null) {
	               return lista;
	           }

	           if (mostrarSelect) {
	               lista.add(new SelectItem(null, SeamResourceBundle.getBundle().getString("opt_select_one")));
	           }
	           try {

	               Class<Enum> enumerator = (Class<Enum>) Class.forName(PACK_ENUM + type);

	               for (Enum o : enumerator.getEnumConstants()) {
	               	String label=o.getClass().getMethod(labelMethod,params.getClass()).invoke(o, params).toString();
	                   lista.add(new SelectItem(o.getClass().getMethod(metodName).invoke(o), label));
	               }
	           } catch (Exception e) {
	               e.printStackTrace();
	               lista.add(new SelectItem(null, "Error al Cargar el enum. Nombre invalido o no se encuentra."));
	           }

	           return lista;
	       }

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<SelectItem> enumsValueItemsReport(String type, String metodName, boolean... mostrarTodos) {
		List<SelectItem> lista = new ArrayList<SelectItem>();

		if (GeneralHelper.getString(type) == null) {
			return lista;
		}

		if (mostrarTodos.length == 0) {
			mostrarTodos = new boolean[1];
			mostrarTodos[0] = true;
		}
		if (mostrarTodos[0]) {
			lista.add(new SelectItem(null, SeamResourceBundle.getBundle().getString("generico_all_male")));
		}
		try {

			Class<Enum> enumerator = (Class<Enum>) Class.forName(PACK_ENUM + type);

			for (Enum o : enumerator.getEnumConstants()) {
				lista.add(new SelectItem(o.getClass().getMethod(metodName).invoke(o), o.toString()));
			}
		} catch (Exception e) {
			logger.error("enumsValueItemsReport",e);
			lista.add(new SelectItem(null, "Error al Cargar el enum. Nombre invalido o no se encuentra."));
		}

		return lista;
	}


}
