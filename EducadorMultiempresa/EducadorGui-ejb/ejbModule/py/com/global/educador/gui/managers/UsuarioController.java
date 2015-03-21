package py.com.global.educador.gui.managers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.international.StatusMessage.Severity;

import py.com.global.educador.gui.dto.ValidationResult;
import py.com.global.educador.gui.entity.Proyecto;
import py.com.global.educador.gui.entity.Usuario;
import py.com.global.educador.gui.enums.ABMActions;
import py.com.global.educador.gui.enums.EstadoRegistro;
import py.com.global.educador.gui.utils.EntityBaseController;


@Name("usuarioController")
@Scope(ScopeType.CONVERSATION)
public class UsuarioController extends EntityBaseController<Usuario> {

	private static final long serialVersionUID = 1L;
	@In(create = true)
	EntityManager entityManager;
	@In(create = true)
	StatusMessages statusMessages;
	

	public UsuarioController() {
		super(Usuario.class);
	}
	
	
	@Create
	public void init(){
		System.out.println("Init usuarioController");
	}
	
	
	@Override
	public void select(Serializable id) {
		super.select(id);
	}
	

	@Override
	public String remove(){
		try{
			getInstance().setEstado(EstadoRegistro.ELIMINADO.name());
			entityManager.merge(getInstance());
			entityManager.flush();
			statusMessages.add(Severity.INFO,"#{messages.msg_remove_successful}");
			return "removed";
		}catch(Exception e){
			statusMessages.add(Severity.ERROR,"#{messages.msg_unexpected_error}");
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void createNew() {
		super.createNew();
	}

	@Override
	public ValidationResult checkData(ABMActions action) {
		switch (action) {
		case PERSIST:
		case UPDATE:
			if (existeUsuario()) {
				return new ValidationResult(false, "Usuario_msg_exist");
			}
			return new ValidationResult(true, null);
		case REMOVE:
			return new ValidationResult(true, null);

		default:
			break;
		}
		return new ValidationResult(true, null);
	}

	private boolean existeUsuario() {
		String hql = "SELECT COUNT(c) FROM Usuario c WHERE lower(trim(c.nombreUsuario)) = lower(trim(:nombreUsuario))";
		if (isManaged()) {
			hql += " AND c.idUsuario != :idUsuario";
		}
		Query q = entityManager.createQuery(hql);
		q.setParameter("nombreUsuario", getInstance().getNombre());

		if (isManaged()) {
			q.setParameter("idUsuario", getInstance().getId());
		}
		Number n = (Number) q.getSingleResult();
		return n.intValue() > 0;
	}

	@Override
	public void setDetailsData() {

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
	public void setCommonData(Usuario i) {
		if (isNew()) {
			i.setFechaRegistro(new Date());
			i.setEstado((EstadoRegistro.ACTIVO.name()));
		}
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
