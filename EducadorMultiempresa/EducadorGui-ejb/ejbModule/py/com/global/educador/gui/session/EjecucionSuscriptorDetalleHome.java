package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("ejecucionSuscriptorDetalleHome")
public class EjecucionSuscriptorDetalleHome extends
		EntityHome<EjecucionSuscriptorDetalle> {

	@In(create = true)
	EvaluacionHome evaluacionHome;
	@In(create = true)
	TipHome tipHome;
	@In(create = true)
	EjecucionSuscriptorHome ejecucionSuscriptorHome;

	public void setEjecucionSuscriptorDetalleIdEjecucionDetalle(BigDecimal id) {
		setId(id);
	}

	public BigDecimal getEjecucionSuscriptorDetalleIdEjecucionDetalle() {
		return (BigDecimal) getId();
	}

	@Override
	protected EjecucionSuscriptorDetalle createInstance() {
		EjecucionSuscriptorDetalle ejecucionSuscriptorDetalle = new EjecucionSuscriptorDetalle();
		return ejecucionSuscriptorDetalle;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Evaluacion evaluacion = evaluacionHome.getDefinedInstance();
		if (evaluacion != null) {
			getInstance().setEvaluacion(evaluacion);
		}
		Tip tip = tipHome.getDefinedInstance();
		if (tip != null) {
			getInstance().setTip(tip);
		}
		EjecucionSuscriptor ejecucionSuscriptor = ejecucionSuscriptorHome
				.getDefinedInstance();
		if (ejecucionSuscriptor != null) {
			getInstance().setEjecucionSuscriptor(ejecucionSuscriptor);
		}
	}

	public boolean isWired() {
		return true;
	}

	public EjecucionSuscriptorDetalle getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<EvaluacionSuscriptor> getEvaluacionSuscriptors() {
		return getInstance() == null ? null
				: new ArrayList<EvaluacionSuscriptor>(getInstance()
						.getEvaluacionSuscriptors());
	}

}
