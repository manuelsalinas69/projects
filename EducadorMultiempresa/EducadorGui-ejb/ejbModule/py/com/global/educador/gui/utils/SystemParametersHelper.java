package py.com.global.educador.gui.utils;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import py.com.global.educador.gui.entity.ParametroSistema;

@Name("systemParametersHelper")
public class SystemParametersHelper implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@In EntityManager entityManager;
	
	public String getParameterValue(String key){
		try {
			return entityManager.find(ParametroSistema.class, key).getValor();
		} catch (Exception e) {
			System.out.println("SystemParametersHelper.getParameterValue(): "+e);
		}
		return null;
	}

}
