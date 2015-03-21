package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("logClientResponseList")
public class LogClientResponseList extends EntityQuery<LogClientResponse> {

	private static final String EJBQL = "select logClientResponse from LogClientResponse logClientResponse";

	private static final String[] RESTRICTIONS = {
			"lower(logClientResponse.mensaje) like lower(concat(#{logClientResponseList.logClientResponse.mensaje},'%'))",
			"lower(logClientResponse.numero) like lower(concat(#{logClientResponseList.logClientResponse.numero},'%'))",
			"lower(logClientResponse.numeroCorto) like lower(concat(#{logClientResponseList.logClientResponse.numeroCorto},'%'))", };

	private LogClientResponse logClientResponse = new LogClientResponse();

	public LogClientResponseList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public LogClientResponse getLogClientResponse() {
		return logClientResponse;
	}
}
