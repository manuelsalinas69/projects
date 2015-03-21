package py.com.global.educador.gui.session;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import py.com.global.educador.gui.entity.ParametroProyecto;
import py.com.global.educador.gui.entity.ParametroProyectoId;
import py.com.global.educador.gui.entity.Proyecto;

@Name("parametroProyectoHome")
public class ParametroProyectoHome extends EntityHome<ParametroProyecto> {

	@In(create = true)
	ProyectoHome proyectoHome;

	public void setParametroProyectoId(ParametroProyectoId id) {
		setId(id);
	}

	public ParametroProyectoId getParametroProyectoId() {
		return (ParametroProyectoId) getId();
	}

	public ParametroProyectoHome() {
		setParametroProyectoId(new ParametroProyectoId());
	}

	@Override
	public boolean isIdDefined() {
		if (getParametroProyectoId().getParametro() == null
				|| "".equals(getParametroProyectoId().getParametro()))
			return false;
		if (getParametroProyectoId().getIdProyecto() == null)
			return false;
		return true;
	}

	@Override
	protected ParametroProyecto createInstance() {
		ParametroProyecto parametroProyecto = new ParametroProyecto();
		parametroProyecto.setId(new ParametroProyectoId());
		return parametroProyecto;
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
		if (getInstance().getProyecto() == null)
			return false;
		return true;
	}

	public ParametroProyecto getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
