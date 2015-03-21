package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("ejecucionSuscriptorDetalleList")
public class EjecucionSuscriptorDetalleList extends
		EntityQuery<EjecucionSuscriptorDetalle> {

	private static final String EJBQL = "select ejecucionSuscriptorDetalle from EjecucionSuscriptorDetalle ejecucionSuscriptorDetalle";

	private static final String[] RESTRICTIONS = {
			"lower(ejecucionSuscriptorDetalle.estadoEjecucion) like lower(concat(#{ejecucionSuscriptorDetalleList.ejecucionSuscriptorDetalle.estadoEjecucion},'%'))",
			"lower(ejecucionSuscriptorDetalle.enviar) like lower(concat(#{ejecucionSuscriptorDetalleList.ejecucionSuscriptorDetalle.enviar},'%'))",
			"lower(ejecucionSuscriptorDetalle.respuestaSmsc) like lower(concat(#{ejecucionSuscriptorDetalleList.ejecucionSuscriptorDetalle.respuestaSmsc},'%'))", };

	private EjecucionSuscriptorDetalle ejecucionSuscriptorDetalle = new EjecucionSuscriptorDetalle();

	public EjecucionSuscriptorDetalleList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public EjecucionSuscriptorDetalle getEjecucionSuscriptorDetalle() {
		return ejecucionSuscriptorDetalle;
	}
}
