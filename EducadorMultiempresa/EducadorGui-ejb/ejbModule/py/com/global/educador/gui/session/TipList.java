package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("tipList")
public class TipList extends EntityQuery<Tip> {

	private static final String EJBQL = "select tip from Tip tip";

	private static final String[] RESTRICTIONS = {
			"lower(tip.contenido) like lower(concat(#{tipList.tip.contenido},'%'))",
			"lower(tip.usuarioAlta) like lower(concat(#{tipList.tip.usuarioAlta},'%'))",
			"lower(tip.usuarioModificacion) like lower(concat(#{tipList.tip.usuarioModificacion},'%'))",
			"lower(tip.estadoRegistro) like lower(concat(#{tipList.tip.estadoRegistro},'%'))", };

	private Tip tip = new Tip();

	public TipList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Tip getTip() {
		return tip;
	}
}
