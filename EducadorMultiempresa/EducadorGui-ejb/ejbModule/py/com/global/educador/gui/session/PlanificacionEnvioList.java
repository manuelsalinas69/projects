package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("planificacionEnvioList")
public class PlanificacionEnvioList extends EntityQuery<PlanificacionEnvio> {

	private static final String EJBQL = "select planificacionEnvio from PlanificacionEnvio planificacionEnvio";

	private static final String[] RESTRICTIONS = { "lower(planificacionEnvio.enviar) like lower(concat(#{planificacionEnvioList.planificacionEnvio.enviar},'%'))", };

	private PlanificacionEnvio planificacionEnvio = new PlanificacionEnvio();

	public PlanificacionEnvioList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public PlanificacionEnvio getPlanificacionEnvio() {
		return planificacionEnvio;
	}
}
