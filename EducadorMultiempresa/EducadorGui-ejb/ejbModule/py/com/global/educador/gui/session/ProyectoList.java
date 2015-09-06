package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("proyectoList")
public class ProyectoList extends EntityQuery<Proyecto> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private static final String EJBQL = "select proyecto from Proyecto proyecto WHERE proyecto.estadoRegistro!='ELIMINADO'";
	

	private static final String[] RESTRICTIONS = {
			"lower(proyecto.nombre) like lower(concat(#{proyectoList.proyecto.nombre},'%'))",
			"lower(proyecto.descripcion) like lower(concat(#{proyectoList.proyecto.descripcion},'%'))",
			"lower(proyecto.objetivos) like lower(concat(#{proyectoList.proyecto.objetivos},'%'))",
			"lower(proyecto.usuarioAlta) like lower(concat(#{proyectoList.proyecto.usuarioAlta},'%'))",
			"lower(proyecto.usuarioModificacion) like lower(concat(#{proyectoList.proyecto.usuarioModificacion},'%'))",
			"lower(proyecto.estadoProyecto) like lower(concat(#{proyectoList.proyecto.estadoProyecto},'%'))",
			"lower(proyecto.estadoRegistro) like lower(concat(#{proyectoList.proyecto.estadoRegistro},'%'))",
			"lower(proyecto.numeroCorto) like lower(concat(#{proyectoList.proyecto.numeroCorto},'%'))",
			"proyecto.empresa.idEmpresa=#{proyectoList.idEmpresa}",};

	private Proyecto proyecto = new Proyecto();
	private Long idEmpresa;

	public ProyectoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Proyecto getProyecto() {
		return proyecto;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	
	
}
