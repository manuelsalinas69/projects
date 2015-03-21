package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import py.com.global.educador.gui.utils.GeneralHelper;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;
import java.util.Date;

@Name("moduloList")
public class ModuloList extends EntityQuery<Modulo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select modulo from Modulo modulo";

	private static final String[] RESTRICTIONS = {
			"lower(modulo.nombre) like lower(concat('%',#{moduloList.modulo.nombre},'%'))",
			"lower(modulo.descripcion) like lower(concat(#{moduloList.modulo.descripcion},'%'))",
			"lower(modulo.objetivos) like lower(concat(#{moduloList.modulo.objetivos},'%'))",
			"lower(modulo.estadoModulo) like lower(concat(#{moduloList.modulo.estadoModulo},'%'))",
			"lower(modulo.estadoRegistro) like lower(concat(#{moduloList.modulo.estadoRegistro},'%'))",
			"lower(modulo.configuracionEnvioTip) like lower(concat(#{moduloList.modulo.configuracionEnvioTip},'%'))",
			"lower(modulo.tipoModulo) like lower(concat(#{moduloList.modulo.tipoModulo},'%'))",
			"modulo.fechaInicio>=#{moduloList.fechaInicioDesde}",
			"modulo.fechaInicio<=#{moduloList.fechaInicioHasta}",
			"modulo.fechaFin>=#{moduloList.fechaFinDesde}",
			"modulo.fechaFin<=#{moduloList.fechaFinHasta}",
			"modulo.proyecto.idProyecto=#{moduloList.idProyecto}",
			};

	private Modulo modulo = new Modulo();

	
	private Long idProyecto;
	private Date fechaInicioDesde, fechaInicioHasta;
	private Date fechaFinDesde, fechaFinHasta;
	
	
	public ModuloList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Modulo getModulo() {
		return modulo;
	}

	public Long getIdProyecto() {
		return idProyecto;
	}

	public void setIdProyecto(Long idProyecto) {
		this.idProyecto = idProyecto;
	}

	public Date getFechaInicioDesde() {
		return GeneralHelper.setTimeOfDate(fechaInicioDesde, "00:00:00",null);
	}

	public void setFechaInicioDesde(Date fechaInicioDesde) {
		this.fechaInicioDesde = fechaInicioDesde;
	}

	public Date getFechaInicioHasta() {
		return GeneralHelper.setTimeOfDate(fechaInicioHasta, "23:59:59",null);
	}

	public void setFechaInicioHasta(Date fechaInicioHasta) {
		this.fechaInicioHasta = fechaInicioHasta;
	}

	public Date getFechaFinDesde() {
		return GeneralHelper.setTimeOfDate(fechaFinDesde, "00:00:00",null);
		 
	}

	public void setFechaFinDesde(Date fechaFinDesde) {
		this.fechaFinDesde = fechaFinDesde;
	}

	public Date getFechaFinHasta() {
		return GeneralHelper.setTimeOfDate(fechaFinHasta, "23:59:59",null);
	}

	public void setFechaFinHasta(Date fechaFinHasta) {
		this.fechaFinHasta = fechaFinHasta;
	}
	
	
}
