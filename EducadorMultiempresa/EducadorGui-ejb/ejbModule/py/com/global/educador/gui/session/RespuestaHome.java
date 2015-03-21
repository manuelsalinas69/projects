package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import java.math.BigDecimal;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("respuestaHome")
public class RespuestaHome extends EntityHome<Respuesta> {

	@In(create = true)
	PreguntaHome preguntaHome;

	public void setRespuestaIdRespuesta(BigDecimal id) {
		setId(id);
	}

	public BigDecimal getRespuestaIdRespuesta() {
		return (BigDecimal) getId();
	}

	@Override
	protected Respuesta createInstance() {
		Respuesta respuesta = new Respuesta();
		return respuesta;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Pregunta pregunta = preguntaHome.getDefinedInstance();
		if (pregunta != null) {
			getInstance().setPregunta(pregunta);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Respuesta getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
