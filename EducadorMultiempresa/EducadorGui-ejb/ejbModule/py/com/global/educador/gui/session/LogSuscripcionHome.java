package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import java.math.BigDecimal;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("logSuscripcionHome")
public class LogSuscripcionHome extends EntityHome<LogSuscripcion> {

	public void setLogSuscripcionIdLogSuscripcion(BigDecimal id) {
		setId(id);
	}

	public BigDecimal getLogSuscripcionIdLogSuscripcion() {
		return (BigDecimal) getId();
	}

	@Override
	protected LogSuscripcion createInstance() {
		LogSuscripcion logSuscripcion = new LogSuscripcion();
		return logSuscripcion;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public LogSuscripcion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
