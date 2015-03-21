package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("sessionTrxList")
public class SessionTrxList extends EntityQuery<SessionTrx> {

	private static final String EJBQL = "select sessionTrx from SessionTrx sessionTrx";

	private static final String[] RESTRICTIONS = {
			"lower(sessionTrx.numero) like lower(concat(#{sessionTrxList.sessionTrx.numero},'%'))",
			"lower(sessionTrx.numeroCorto) like lower(concat(#{sessionTrxList.sessionTrx.numeroCorto},'%'))", };

	private SessionTrx sessionTrx = new SessionTrx();

	public SessionTrxList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public SessionTrx getSessionTrx() {
		return sessionTrx;
	}
}
