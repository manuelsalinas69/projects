package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("parametroSistemaHome")
public class ParametroSistemaHome extends EntityHome<ParametroSistema> {

	public void setParametroSistemaParametro(String id) {
		setId(id);
	}

	public String getParametroSistemaParametro() {
		return (String) getId();
	}

	@Override
	protected ParametroSistema createInstance() {
		ParametroSistema parametroSistema = new ParametroSistema();
		return parametroSistema;
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

	public ParametroSistema getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
