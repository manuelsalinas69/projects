package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import java.math.BigDecimal;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("logSmsInHome")
public class LogSmsInHome extends EntityHome<LogSmsIn> {

	public void setLogSmsInIdLogSmsIn(BigDecimal id) {
		setId(id);
	}

	public BigDecimal getLogSmsInIdLogSmsIn() {
		return (BigDecimal) getId();
	}

	@Override
	protected LogSmsIn createInstance() {
		LogSmsIn logSmsIn = new LogSmsIn();
		return logSmsIn;
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

	public LogSmsIn getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
