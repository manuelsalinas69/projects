package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("ejecucionSuscriptorList")
public class EjecucionSuscriptorList extends EntityQuery<EjecucionSuscriptor> {

	private static final String EJBQL = "select ejecucionSuscriptor from EjecucionSuscriptor ejecucionSuscriptor";

	private static final String[] RESTRICTIONS = {
			"lower(ejecucionSuscriptor.estadoEjecucion) like lower(concat(#{ejecucionSuscriptorList.ejecucionSuscriptor.estadoEjecucion},'%'))",
			"lower(ejecucionSuscriptor.motivoCancelacion) like lower(concat(#{ejecucionSuscriptorList.ejecucionSuscriptor.motivoCancelacion},'%'))", };

	private EjecucionSuscriptor ejecucionSuscriptor = new EjecucionSuscriptor();

	public EjecucionSuscriptorList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public EjecucionSuscriptor getEjecucionSuscriptor() {
		return ejecucionSuscriptor;
	}
}
