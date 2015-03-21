package py.com.global.educador.gui.managers;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import py.com.global.educador.gui.entity.ParametroSistema;


@Name(value="parameterController")
@Scope(ScopeType.PAGE)
public class ParameterController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In
	EntityManager entityManager;
	
	ParametroSistema parametroSelected;
	boolean edit;
	
	@Create
	public void init(){
		System.out.println("Init ParameterController");
	}
	
	public String getParameterValue(String idParametro){
		if (idParametro==null || idParametro.trim().isEmpty()) {
			return null; 
		}
		
		 ParametroSistema parametro=entityManager.find(ParametroSistema.class, idParametro);
		
		return parametro==null?null:parametro.getValor();
	}

	public void update(){
		entityManager.merge(parametroSelected);
		entityManager.flush();
	}
	
	public void view(String idParametro){
		select(idParametro, false);
	}
	
	public void edit(String idParametro){
		select(idParametro, true);
	}
	
	private void select(String idParametro, boolean edit){
		parametroSelected=entityManager.find(ParametroSistema.class, idParametro);
		this.edit=edit;
	}
	
	
	
	public ParametroSistema getParametroSelected() {
		return parametroSelected;
	}

	public void setParametroSelected(ParametroSistema parametroSelected) {
		this.parametroSelected = parametroSelected;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

}
