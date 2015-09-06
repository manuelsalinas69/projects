package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("empresaList")
public class EmpresaList extends EntityQuery<Empresa> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select empresa from Empresa empresa";

	private static final String[] RESTRICTIONS = {
			"lower(empresa.nombre) like lower(concat('%',#{empresaList.empresa.nombre},'%'))",
			"lower(empresa.direccion) like lower(concat('%',#{empresaList.empresa.direccion},'%'))",
			"lower(empresa.telefonoPrincipal) like lower(concat('%',#{empresaList.empresa.telefonoPrincipal},'%'))",
			"lower(empresa.telefonoSecundario) like lower(concat('%',#{empresaList.empresa.telefonoSecundario},'%'))",
			"lower(empresa.email) like lower(concat('%',#{empresaList.empresa.email},'%'))",
			"lower(empresa.nombreContacto) like lower(concat('%',#{empresaList.empresa.nombreContacto},'%'))",
			"lower(empresa.telefonoContacto) like lower(concat('%',#{empresaList.empresa.telefonoContacto},'%'))",
			"lower(empresa.gmapsUbicacion) like lower(concat('%',#{empresaList.empresa.gmapsUbicacion},'%'))", };

	private Empresa empresa = new Empresa();

	public EmpresaList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Empresa getEmpresa() {
		return empresa;
	}
}
