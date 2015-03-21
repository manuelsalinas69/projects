package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import java.math.BigDecimal;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("logSmsOutHome")
public class LogSmsOutHome extends EntityHome<LogSmsOut> {

	public void setLogSmsOutIdLogSmsOut(BigDecimal id) {
		setId(id);
	}

	public BigDecimal getLogSmsOutIdLogSmsOut() {
		return (BigDecimal) getId();
	}

	@Override
	protected LogSmsOut createInstance() {
		LogSmsOut logSmsOut = new LogSmsOut();
		return logSmsOut;
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

	public LogSmsOut getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
