package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("respuestaList")
public class RespuestaList extends EntityQuery<Respuesta> {

	private static final String EJBQL = "select respuesta from Respuesta respuesta";

	private static final String[] RESTRICTIONS = {
			"lower(respuesta.contenidoRespuesta) like lower(concat(#{respuestaList.respuesta.contenidoRespuesta},'%'))",
			"lower(respuesta.ordenRespuesta) like lower(concat(#{respuestaList.respuesta.ordenRespuesta},'%'))",
			"lower(respuesta.estadoRegistro) like lower(concat(#{respuestaList.respuesta.estadoRegistro},'%'))",
			"lower(respuesta.valorEsperado) like lower(concat(#{respuestaList.respuesta.valorEsperado},'%'))", };

	private Respuesta respuesta = new Respuesta();

	public RespuestaList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Respuesta getRespuesta() {
		return respuesta;
	}
}
