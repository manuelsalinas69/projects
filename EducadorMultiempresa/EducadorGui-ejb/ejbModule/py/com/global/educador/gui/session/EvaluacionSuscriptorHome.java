package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import java.math.BigDecimal;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("evaluacionSuscriptorHome")
public class EvaluacionSuscriptorHome extends EntityHome<EvaluacionSuscriptor> {

	@In(create = true)
	EvaluacionHome evaluacionHome;
	@In(create = true)
	SuscriptorHome suscriptorHome;
	@In(create = true)
	PreguntaHome preguntaHome;
	@In(create = true)
	EjecucionSuscriptorDetalleHome ejecucionSuscriptorDetalleHome;

	public void setEvaluacionSuscriptorIdEvaluacionSuscriptor(BigDecimal id) {
		setId(id);
	}

	public BigDecimal getEvaluacionSuscriptorIdEvaluacionSuscriptor() {
		return (BigDecimal) getId();
	}

	@Override
	protected EvaluacionSuscriptor createInstance() {
		EvaluacionSuscriptor evaluacionSuscriptor = new EvaluacionSuscriptor();
		return evaluacionSuscriptor;
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
		Suscriptor suscriptor = suscriptorHome.getDefinedInstance();
		if (suscriptor != null) {
			getInstance().setSuscriptor(suscriptor);
		}
		Pregunta pregunta = preguntaHome.getDefinedInstance();
		if (pregunta != null) {
			getInstance().setPregunta(pregunta);
		}
		EjecucionSuscriptorDetalle ejecucionSuscriptorDetalle = ejecucionSuscriptorDetalleHome
				.getDefinedInstance();
		if (ejecucionSuscriptorDetalle != null) {
			getInstance().setEjecucionSuscriptorDetalle(
					ejecucionSuscriptorDetalle);
		}
	}

	public boolean isWired() {
		return true;
	}

	public EvaluacionSuscriptor getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
