package py.com.global.educador.gui.managers;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessages;

import py.com.global.educador.gui.dto.ValidationResult;
import py.com.global.educador.gui.entity.Empresa;
import py.com.global.educador.gui.enums.ABMActions;
import py.com.global.educador.gui.enums.EstadoRegistro;
import py.com.global.educador.gui.session.EmpresaHome;
import py.com.global.educador.gui.utils.EntityBaseController;
import py.com.global.educador.gui.utils.GeneralHelper;

@Name("empresaFormController")
@Scope(ScopeType.PAGE)
public class EmpresaFormController extends EntityBaseController<Empresa>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@In()EntityManager entityManager;
	@In()StatusMessages statusMessages;
	@In(create=true) EmpresaHome empresaHome;
	

	public EmpresaFormController() {
		super(Empresa.class);
		
	}
	
	public void init(){
		if (empresaHome.isManaged()) {
			edit(empresaHome.getInstance().getId());
		}
		else{
			createNew();
		}
	}

	@Override
	public void setDetailsData() {
		getInstance().setHashId(GeneralHelper.MD5(getInstance().getIdEmpresa().toString()));
		
	}

	@Override
	public ValidationResult checkData(ABMActions action) {
		switch (action) {
		case PERSIST:
		case UPDATE:
			if (existNombre(getInstance())) {
				String field=GeneralHelper.getMessages("GENERICO_NOMBRE");
				return new ValidationResult(false, "msg_error_already_exists",field);
			}
			break;
		case REMOVE:
			break;
		default:
			break;
		}
		
		return new ValidationResult(true, null);
	}
	
	@Override
	public String remove() {
		return super.remove();
	}

	private boolean existNombre(Empresa i) {
		String hql="SELECT count(_e.idEmpresa) FROM Empresa _e WHERE lower(_e.nombre)=lower(:nombre) ";
		if (isManaged()) {
			hql+=" AND _e != :instance";
		}
		Query q= entityManager.createQuery(hql);
		q.setParameter("nombre", i.getNombre());
		if (isManaged()) {
			q.setParameter("instance", i);
		}
		Number _r= (Number) q.getSingleResult();
		return _r.intValue()>0;
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
	public void setCommonData(Empresa instance) {
		if (isNew()) {
			getInstance().setFechaCreacion(new Date());
			getInstance().setEstadoRegistro(EstadoRegistro.ACTIVO.name());
			
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
