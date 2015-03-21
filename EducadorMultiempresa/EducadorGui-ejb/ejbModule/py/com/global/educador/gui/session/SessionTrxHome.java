package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import java.math.BigDecimal;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("sessionTrxHome")
public class SessionTrxHome extends EntityHome<SessionTrx> {

	@In(create = true)
	SuscriptorHome suscriptorHome;

	public void setSessionTrxIdSessionTrx(BigDecimal id) {
		setId(id);
	}

	public BigDecimal getSessionTrxIdSessionTrx() {
		return (BigDecimal) getId();
	}

	@Override
	protected SessionTrx createInstance() {
		SessionTrx sessionTrx = new SessionTrx();
		return sessionTrx;
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

	public SessionTrx getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
