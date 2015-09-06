package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import java.lang.Long;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("empresaHome")
public class EmpresaHome extends EntityHome<Empresa> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setEmpresaIdEmpresa(Long id) {
		setId(id);
	}

	public Long getEmpresaIdEmpresa() {
		return (Long) getId();
	}

	@Override
	protected Empresa createInstance() {
		Empresa empresa = new Empresa();
		return empresa;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public Empresa getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Usuario> getUsuarios() {
		return getInstance() == null ? null : new ArrayList<Usuario>(
				getInstance().getUsuarios());
	}

	public List<Proyecto> getProyectos() {
		return getInstance() == null ? null : new ArrayList<Proyecto>(
				getInstance().getProyectos());
	}

}
