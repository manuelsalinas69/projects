package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import java.math.BigDecimal;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("planificacionEnvioHome")
public class PlanificacionEnvioHome extends EntityHome<PlanificacionEnvio> {

	@In(create = true)
	EvaluacionHome evaluacionHome;
	@In(create = true)
	TipHome tipHome;
	@In(create = true)
	ModuloHome moduloHome;

	public void setPlanificacionEnvioIdPlanificacionEnvio(BigDecimal id) {
		setId(id);
	}

	public BigDecimal getPlanificacionEnvioIdPlanificacionEnvio() {
		return (BigDecimal) getId();
	}

	@Override
	protected PlanificacionEnvio createInstance() {
		PlanificacionEnvio planificacionEnvio = new PlanificacionEnvio();
		return planificacionEnvio;
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
		Modulo modulo = moduloHome.getDefinedInstance();
		if (modulo != null) {
			getInstance().setModulo(modulo);
		}
	}

	public boolean isWired() {
		return true;
	}

	public PlanificacionEnvio getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
