package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("preguntaHome")
public class PreguntaHome extends EntityHome<Pregunta> {

	@In(create = true)
	EvaluacionHome evaluacionHome;

	public void setPreguntaIdPregunta(BigDecimal id) {
		setId(id);
	}

	public BigDecimal getPreguntaIdPregunta() {
		return (BigDecimal) getId();
	}

	@Override
	protected Pregunta createInstance() {
		Pregunta pregunta = new Pregunta();
		return pregunta;
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
	}

	public boolean isWired() {
		return true;
	}

	public Pregunta getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<EvaluacionSuscriptor> getEvaluacionSuscriptors() {
		return getInstance() == null ? null
				: new ArrayList<EvaluacionSuscriptor>(getInstance()
						.getEvaluacionSuscriptors());
	}

	public List<Respuesta> getRespuestas() {
		return getInstance() == null ? null : new ArrayList<Respuesta>(
				getInstance().getRespuestas());
	}

}
