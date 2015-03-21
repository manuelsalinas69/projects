package py.com.global.educador.gui.session;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import py.com.global.educador.gui.entity.ParametroProyecto;
import py.com.global.educador.gui.entity.ParametroProyectoId;

@Name("parametroProyectoList")
public class ParametroProyectoList extends EntityQuery<ParametroProyecto> {

	private static final String EJBQL = "select parametroProyecto from ParametroProyecto parametroProyecto";

	private static final String[] RESTRICTIONS = {
			"lower(parametroProyecto.id.parametro) like lower(concat(#{parametroProyectoList.parametroProyecto.id.parametro},'%'))",
			"lower(parametroProyecto.valor) like lower(concat(#{parametroProyectoList.parametroProyecto.valor},'%'))",
			"lower(parametroProyecto.descripcion) like lower(concat(#{parametroProyectoList.parametroProyecto.descripcion},'%'))", };

	private ParametroProyecto parametroProyecto;

	public ParametroProyectoList() {
		parametroProyecto = new ParametroProyecto();
		parametroProyecto.setId(new ParametroProyectoId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public ParametroProyecto getParametroProyecto() {
		return parametroProyecto;
	}
}
