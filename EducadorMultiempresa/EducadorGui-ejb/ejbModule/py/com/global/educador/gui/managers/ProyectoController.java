package py.com.global.educador.gui.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;

import py.com.global.educador.gui.dto.ValidationResult;
import py.com.global.educador.gui.entity.Evaluacion;
import py.com.global.educador.gui.entity.Modulo;
import py.com.global.educador.gui.entity.PlanificacionEnvio;
import py.com.global.educador.gui.entity.Proyecto;
import py.com.global.educador.gui.entity.Respuesta;
import py.com.global.educador.gui.entity.Tip;
import py.com.global.educador.gui.enums.ABMActions;
import py.com.global.educador.gui.enums.EstadoRegistro;
import py.com.global.educador.gui.enums.PlanificacionEnvioType;
import py.com.global.educador.gui.session.ModuloHome;
import py.com.global.educador.gui.utils.EntityBaseController;


@Name("proyectoController")
@Scope(ScopeType.CONVERSATION)
public class ProyectoController extends EntityBaseController<Proyecto> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@In EntityManager entityManager;
	@In StatusMessages statusMessages;
	
	
	
	
	public ProyectoController() {
		super(Proyecto.class);
	}


	@Create
	public void init(){
		setInitialValues();
		

	}
	
	
	private void setInitialValues() {
	
		
	}


	@Override
	public void setDetailsData() {
		
		
	}

	@Override
	public ValidationResult checkData(ABMActions action) {
		
		return new ValidationResult(true, null);
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StatusMessages getStatusMessages() {
		return statusMessages;
	}

	@Override
	public void setCommonData(Proyecto instance) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canEdit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canRemove() {
		// TODO Auto-generated method stub
		return false;
	}

	

	
	

}
