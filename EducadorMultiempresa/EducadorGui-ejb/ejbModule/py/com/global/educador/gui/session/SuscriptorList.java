package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("suscriptorList")
public class SuscriptorList extends EntityQuery<Suscriptor> {

	private static final String EJBQL = "select suscriptor from Suscriptor suscriptor";

	private static final String[] RESTRICTIONS = {
			"lower(suscriptor.numero) like lower(concat(#{suscriptorList.suscriptor.numero},'%'))",
			"lower(suscriptor.tipoAlta) like lower(concat(#{suscriptorList.suscriptor.tipoAlta},'%'))", };

	private Suscriptor suscriptor = new Suscriptor();

	public SuscriptorList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Suscriptor getSuscriptor() {
		return suscriptor;
	}
}
