package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("evaluacionHome")
public class EvaluacionHome extends EntityHome<Evaluacion> {

	@In(create = true)
	ModuloHome moduloHome;

	public void setEvaluacionIdEvaluacion(BigDecimal id) {
		setId(id);
	}

	public BigDecimal getEvaluacionIdEvaluacion() {
		return (BigDecimal) getId();
	}

	@Override
	protected Evaluacion createInstance() {
		Evaluacion evaluacion = new Evaluacion();
		return evaluacion;
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

	public Evaluacion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<PlanificacionEnvio> getPlanificacionEnvios() {
		return getInstance() == null ? null
				: new ArrayList<PlanificacionEnvio>(getInstance()
						.getPlanificacionEnvios());
	}

	public List<Pregunta> getPreguntas() {
		return getInstance() == null ? null : new ArrayList<Pregunta>(
				getInstance().getPreguntas());
	}

	public List<EjecucionSuscriptorDetalle> getEjecucionSuscriptorDetalles() {
		return getInstance() == null ? null
				: new ArrayList<EjecucionSuscriptorDetalle>(getInstance()
						.getEjecucionSuscriptorDetalles());
	}

	public List<EvaluacionSuscriptor> getEvaluacionSuscriptors() {
		return getInstance() == null ? null
				: new ArrayList<EvaluacionSuscriptor>(getInstance()
						.getEvaluacionSuscriptors());
	}

}
