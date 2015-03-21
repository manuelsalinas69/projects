package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("preguntaList")
public class PreguntaList extends EntityQuery<Pregunta> {

	private static final String EJBQL = "select pregunta from Pregunta pregunta";

	private static final String[] RESTRICTIONS = {
			"lower(pregunta.contenidoPregunta) like lower(concat(#{preguntaList.pregunta.contenidoPregunta},'%'))",
			"lower(pregunta.usuarioAlta) like lower(concat(#{preguntaList.pregunta.usuarioAlta},'%'))",
			"lower(pregunta.usuarioModificacion) like lower(concat(#{preguntaList.pregunta.usuarioModificacion},'%'))",
			"lower(pregunta.estadoRegistro) like lower(concat(#{preguntaList.pregunta.estadoRegistro},'%'))", };

	private Pregunta pregunta = new Pregunta();

	public PreguntaList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Pregunta getPregunta() {
		return pregunta;
	}
}
