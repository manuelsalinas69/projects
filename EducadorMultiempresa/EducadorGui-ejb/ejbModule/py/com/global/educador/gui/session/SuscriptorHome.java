package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("suscriptorHome")
public class SuscriptorHome extends EntityHome<Suscriptor> {

	public void setSuscriptorIdSuscriptor(BigDecimal id) {
		setId(id);
	}

	public BigDecimal getSuscriptorIdSuscriptor() {
		return (BigDecimal) getId();
	}

	@Override
	protected Suscriptor createInstance() {
		Suscriptor suscriptor = new Suscriptor();
		return suscriptor;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public Suscriptor getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<EjecucionSuscriptor> getEjecucionSuscriptors() {
		return getInstance() == null ? null
				: new ArrayList<EjecucionSuscriptor>(getInstance()
						.getEjecucionSuscriptors());
	}

	public List<SessionTrx> getSessionTrxes() {
		return getInstance() == null ? null : new ArrayList<SessionTrx>(
				getInstance().getSessionTrxes());
	}

	public List<EvaluacionSuscriptor> getEvaluacionSuscriptors() {
		return getInstance() == null ? null
				: new ArrayList<EvaluacionSuscriptor>(getInstance()
						.getEvaluacionSuscriptors());
	}

	public List<SuscriptorProyecto> getSuscriptorProyectos() {
		return getInstance() == null ? null
				: new ArrayList<SuscriptorProyecto>(getInstance()
						.getSuscriptorProyectos());
	}

	public List<LogClientResponse> getLogClientResponses() {
		return getInstance() == null ? null : new ArrayList<LogClientResponse>(
				getInstance().getLogClientResponses());
	}

}
