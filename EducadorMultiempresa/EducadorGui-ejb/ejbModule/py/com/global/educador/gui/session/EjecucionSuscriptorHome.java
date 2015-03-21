package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("ejecucionSuscriptorHome")
public class EjecucionSuscriptorHome extends EntityHome<EjecucionSuscriptor> {

	@In(create = true)
	SuscriptorHome suscriptorHome;
	@In(create = true)
	ModuloHome moduloHome;

	public void setEjecucionSuscriptorIdEjecucionSuscriptor(BigDecimal id) {
		setId(id);
	}

	public BigDecimal getEjecucionSuscriptorIdEjecucionSuscriptor() {
		return (BigDecimal) getId();
	}

	@Override
	protected EjecucionSuscriptor createInstance() {
		EjecucionSuscriptor ejecucionSuscriptor = new EjecucionSuscriptor();
		return ejecucionSuscriptor;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
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
		return true;
	}

	public EjecucionSuscriptor getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<EjecucionSuscriptorDetalle> getEjecucionSuscriptorDetalles() {
		return getInstance() == null ? null
				: new ArrayList<EjecucionSuscriptorDetalle>(getInstance()
						.getEjecucionSuscriptorDetalles());
	}

}
