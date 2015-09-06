package py.com.global.educador.gui.session;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.framework.EntityHome;

import py.com.global.educador.gui.entity.Empresa;
import py.com.global.educador.gui.entity.Evaluacion;
import py.com.global.educador.gui.entity.Modulo;
import py.com.global.educador.gui.entity.Pregunta;
import py.com.global.educador.gui.entity.Proyecto;
import py.com.global.educador.gui.entity.Tip;
import py.com.global.educador.gui.entity.Usuario;
import py.com.global.educador.gui.enums.EstadoRegistro;
import py.com.global.educador.gui.utils.GeneralHelper;

@Name("usuarioHome")
public class UsuarioHome extends EntityHome<Usuario> {

	private String contrasenaConfirma;
	private Long idEmpresa;
	
	public void init(){
		if (isManaged() || isIdDefined()) {
			setIdEmpresa(getInstance().getEmpresa()!=null?getInstance().getEmpresa().getIdEmpresa():null);
		}
		
	}

	@Override
	public String persist() {
		if (idEmpresa!=null) {
			getInstance().setEmpresa(getEntityManager().find(Empresa.class, idEmpresa));
		}
		getInstance().setContrasena(
				GeneralHelper.MD5(getInstance().getUsuario()));
		return super.persist();
	}
	
	@Override
	@Transactional
	public String update() {
		if (idEmpresa!=null) {
			getInstance().setEmpresa(getEntityManager().find(Empresa.class, idEmpresa));
		}
		return super.update();
	}
	@Override
	public String remove() {
		getInstance().setEstado(EstadoRegistro.ELIMINADO.name());
		return super.update();
	}

	
	public String resetPass(){
		getInstance().setContrasena(
				GeneralHelper.MD5(getInstance().getUsuario()));
		return super.update();
		
	}
	
	public void setUsuarioUsuario(String id) {
		setId(id);
	}

	public String getUsuarioUsuario() {
		return (String) getId();
	}

	@Override
	protected Usuario createInstance() {
		Usuario usuario = new Usuario();
		return usuario;
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

	public Usuario getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Pregunta> getPreguntasForUsuarioModificacion() {
		return getInstance() == null ? null : new ArrayList<Pregunta>(
				getInstance().getPreguntasForUsuarioModificacion());
	}

	public List<Tip> getTipsForUsuarioAlta() {
		return getInstance() == null ? null : new ArrayList<Tip>(getInstance()
				.getTipsForUsuarioAlta());
	}

	public List<Proyecto> getProyectosForUsuarioAlta() {
		return getInstance() == null ? null : new ArrayList<Proyecto>(
				getInstance().getProyectosForUsuarioAlta());
	}

	public List<Pregunta> getPreguntasForUsuarioAlta() {
		return getInstance() == null ? null : new ArrayList<Pregunta>(
				getInstance().getPreguntasForUsuarioAlta());
	}

	public List<Tip> getTipsForUsuarioModificacion() {
		return getInstance() == null ? null : new ArrayList<Tip>(getInstance()
				.getTipsForUsuarioModificacion());
	}

	public List<Evaluacion> getEvaluacionsForUsuarioModificacion() {
		return getInstance() == null ? null : new ArrayList<Evaluacion>(
				getInstance().getEvaluacionsForUsuarioModificacion());
	}

	public List<Proyecto> getProyectosForUsuarioModificacion() {
		return getInstance() == null ? null : new ArrayList<Proyecto>(
				getInstance().getProyectosForUsuarioModificacion());
	}

	public List<Modulo> getModulosForUsuarioAlta() {
		return getInstance() == null ? null : new ArrayList<Modulo>(
				getInstance().getModulosForUsuarioAlta());
	}

	public List<Evaluacion> getEvaluacionsForUsuarioAlta() {
		return getInstance() == null ? null : new ArrayList<Evaluacion>(
				getInstance().getEvaluacionsForUsuarioAlta());
	}

	public List<Modulo> getModulosForUsuarioModificacion() {
		return getInstance() == null ? null : new ArrayList<Modulo>(
				getInstance().getModulosForUsuarioModificacion());
	}

	public String getContrasenaConfirma() {
		return contrasenaConfirma;
	}

	public void setContrasenaConfirma(String contrasenaConfirma) {
		this.contrasenaConfirma = contrasenaConfirma;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	
}
