package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("evaluacionList")
public class EvaluacionList extends EntityQuery<Evaluacion> {

	private static final String EJBQL = "select evaluacion from Evaluacion evaluacion";

	private static final String[] RESTRICTIONS = {
			"lower(evaluacion.nombre) like lower(concat(#{evaluacionList.evaluacion.nombre},'%'))",
			"lower(evaluacion.descripcion) like lower(concat(#{evaluacionList.evaluacion.descripcion},'%'))",
			"lower(evaluacion.usuarioAlta) like lower(concat(#{evaluacionList.evaluacion.usuarioAlta},'%'))",
			"lower(evaluacion.usuarioModificacion) like lower(concat(#{evaluacionList.evaluacion.usuarioModificacion},'%'))",
			"lower(evaluacion.configuracionEnvioModulo) like lower(concat(#{evaluacionList.evaluacion.configuracionEnvioModulo},'%'))",
			"lower(evaluacion.estadoRegistro) like lower(concat(#{evaluacionList.evaluacion.estadoRegistro},'%'))",
			"lower(evaluacion.estadoEvaluacion) like lower(concat(#{evaluacionList.evaluacion.estadoEvaluacion},'%'))", };

	private Evaluacion evaluacion = new Evaluacion();

	public EvaluacionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Evaluacion getEvaluacion() {
		return evaluacion;
	}
}
