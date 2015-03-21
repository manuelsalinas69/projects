package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import java.math.BigDecimal;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("logClientResponseHome")
public class LogClientResponseHome extends EntityHome<LogClientResponse> {

	@In(create = true)
	SuscriptorHome suscriptorHome;

	public void setLogClientResponseIdLogClientResponse(BigDecimal id) {
		setId(id);
	}

	public BigDecimal getLogClientResponseIdLogClientResponse() {
		return (BigDecimal) getId();
	}

	@Override
	protected LogClientResponse createInstance() {
		LogClientResponse logClientResponse = new LogClientResponse();
		return logClientResponse;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Suscriptor suscriptor = suscriptorHome.getDefinedInstance();
		if (suscriptor != null) {
			getInstance().setSuscriptor(suscriptor);
		}
	}

	public boolean isWired() {
		return true;
	}

	public LogClientResponse getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
