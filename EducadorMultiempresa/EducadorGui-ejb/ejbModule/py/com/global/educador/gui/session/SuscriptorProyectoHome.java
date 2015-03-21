package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import java.math.BigDecimal;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("suscriptorProyectoHome")
public class SuscriptorProyectoHome extends EntityHome<SuscriptorProyecto> {

	@In(create = true)
	ProyectoHome proyectoHome;
	@In(create = true)
	SuscriptorHome suscriptorHome;
	@In(create = true)
	ModuloHome moduloHome;

	public void setSuscriptorProyectoId(SuscriptorProyectoId id) {
		setId(id);
	}

	public SuscriptorProyectoId getSuscriptorProyectoId() {
		return (SuscriptorProyectoId) getId();
	}

	public SuscriptorProyectoHome() {
		setSuscriptorProyectoId(new SuscriptorProyectoId());
	}

	@Override
	public boolean isIdDefined() {
		if (getSuscriptorProyectoId().getIdSuscriptor() == null)
			return false;
		if (getSuscriptorProyectoId().getIdProyecto() == null)
			return false;
		return true;
	}

	@Override
	protected SuscriptorProyecto createInstance() {
		SuscriptorProyecto suscriptorProyecto = new SuscriptorProyecto();
		suscriptorProyecto.setId(new SuscriptorProyectoId());
		return suscriptorProyecto;
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
		Suscriptor suscriptor = suscriptorHome.getDefinedInstance();
		if (suscriptor != null) {
			getInstance().setSuscriptor(suscriptor);
		}
		Modulo modulo = moduloHome.getDefinedInstance();
		if (modulo != null) {
			getInstance().setModulo(modulo);
		}
	}

	public boolean isWired() {
		if (getInstance().getProyecto() == null)
			return false;
		if (getInstance().getSuscriptor() == null)
			return false;
		return true;
	}

	public SuscriptorProyecto getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
