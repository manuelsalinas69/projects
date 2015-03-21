package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("evaluacionSuscriptorList")
public class EvaluacionSuscriptorList extends EntityQuery<EvaluacionSuscriptor> {

	private static final String EJBQL = "select evaluacionSuscriptor from EvaluacionSuscriptor evaluacionSuscriptor";

	private static final String[] RESTRICTIONS = {
			"lower(evaluacionSuscriptor.estadoEvaluacion) like lower(concat(#{evaluacionSuscriptorList.evaluacionSuscriptor.estadoEvaluacion},'%'))",
			"lower(evaluacionSuscriptor.respuestaSenderSmsc) like lower(concat(#{evaluacionSuscriptorList.evaluacionSuscriptor.respuestaSenderSmsc},'%'))",
			"lower(evaluacionSuscriptor.estadoEnvio) like lower(concat(#{evaluacionSuscriptorList.evaluacionSuscriptor.estadoEnvio},'%'))", };

	private EvaluacionSuscriptor evaluacionSuscriptor = new EvaluacionSuscriptor();

	public EvaluacionSuscriptorList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public EvaluacionSuscriptor getEvaluacionSuscriptor() {
		return evaluacionSuscriptor;
	}
}
