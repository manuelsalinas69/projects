package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("usuarioList")
public class UsuarioList extends EntityQuery<Usuario> {

	private static final String EJBQL = "SELECT usuario FROM Usuario usuario WHERE usuario.estado!='ELIMINADO'";
	private static final String EJBQL_2 = "SELECT usuario FROM Usuario usuario ";

	private static final String[] RESTRICTIONS = {
			"lower(usuario.usuario) like lower(concat(#{usuarioList.usuario.usuario},'%'))",
			"lower(usuario.contrasena) like lower(concat(#{usuarioList.usuario.contrasena},'%'))",
			"lower(usuario.nombre) like lower(concat(#{usuarioList.usuario.nombre},'%'))",
			"lower(usuario.estado) like lower(concat(#{usuarioList.usuario.estado},'%'))", };

	private Usuario usuario = new Usuario();

	public UsuarioList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Usuario getUsuario() {
		return usuario;
	}
}
