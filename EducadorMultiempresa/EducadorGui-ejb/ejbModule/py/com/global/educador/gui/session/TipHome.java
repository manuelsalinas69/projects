package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("tipHome")
public class TipHome extends EntityHome<Tip> {

	@In(create = true)
	ModuloHome moduloHome;

	public void setTipIdTip(BigDecimal id) {
		setId(id);
	}

	public BigDecimal getTipIdTip() {
		return (BigDecimal) getId();
	}

	@Override
	protected Tip createInstance() {
		Tip tip = new Tip();
		return tip;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Modulo modulo = moduloHome.getDefinedInstance();
		if (modulo != null) {
			getInstance().setModulo(modulo);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Tip getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<PlanificacionEnvio> getPlanificacionEnvios() {
		return getInstance() == null ? null
				: new ArrayList<PlanificacionEnvio>(getInstance()
						.getPlanificacionEnvios());
	}

	public List<EjecucionSuscriptorDetalle> getEjecucionSuscriptorDetalles() {
		return getInstance() == null ? null
				: new ArrayList<EjecucionSuscriptorDetalle>(getInstance()
						.getEjecucionSuscriptorDetalles());
	}

}
