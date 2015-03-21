package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import java.lang.Long;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("moduloHome")
public class ModuloHome extends EntityHome<Modulo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@In(create = true)
	ProyectoHome proyectoHome;

	public void setModuloIdModulo(Long id) {
		setId(id);
	}

	public Long getModuloIdModulo() {
		return (Long) getId();
	}

	@Override
	protected Modulo createInstance() {
		Modulo modulo = new Modulo();
		return modulo;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Proyecto proyecto = proyectoHome.getDefinedInstance();
		if (proyecto != null) {
			getInstance().setProyecto(proyecto);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Modulo getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<EjecucionSuscriptor> getEjecucionSuscriptors() {
		return getInstance() == null ? null
				: new ArrayList<EjecucionSuscriptor>(getInstance()
						.getEjecucionSuscriptors());
	}

	public List<PlanificacionEnvio> getPlanificacionEnvios() {
		return getInstance() == null ? null
				: new ArrayList<PlanificacionEnvio>(getInstance()
						.getPlanificacionEnvios());
	}

	public List<SuscriptorProyecto> getSuscriptorProyectos() {
		return getInstance() == null ? null
				: new ArrayList<SuscriptorProyecto>(getInstance()
						.getSuscriptorProyectos());
	}

	public List<Tip> getTips() {
		return getInstance() == null ? null : new ArrayList<Tip>(getInstance()
				.getTips());
	}

	public List<Evaluacion> getEvaluacions() {
		return getInstance() == null ? null : new ArrayList<Evaluacion>(
				getInstance().getEvaluacions());
	}

}
