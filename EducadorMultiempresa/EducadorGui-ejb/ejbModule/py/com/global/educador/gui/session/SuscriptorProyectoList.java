package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import py.com.global.educador.gui.utils.GeneralHelper;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;
import java.util.Date;

@Name("suscriptorProyectoList")
public class SuscriptorProyectoList extends EntityQuery<SuscriptorProyecto> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select suscriptorProyecto from SuscriptorProyecto suscriptorProyecto";

	private static final String[] RESTRICTIONS = {
			"lower(suscriptorProyecto.estadoSuscriptorProyecto) like lower(concat(#{suscriptorProyectoList.suscriptorProyecto.estadoSuscriptorProyecto},'%'))",
			"lower(suscriptorProyecto.estadoSuscriptorModulo) like lower(concat(#{suscriptorProyectoList.suscriptorProyecto.estadoSuscriptorModulo},'%'))",
			"lower(suscriptorProyecto.suscriptor.numero) like lower(concat('%',#{suscriptorProyectoList.numeroSuscriptor},'%'))",
			"suscriptorProyecto.id.idProyecto=#{suscriptorProyectoList.idProyecto}",
			"suscriptorProyecto.modulo.idModulo=#{suscriptorProyectoList.idModuloActual}",
			"suscriptorProyecto.suscriptor.tipoAlta=#{suscriptorProyectoList.tipoAlta}",
			"suscriptorProyecto.suscriptor.fechaAlta>=#{suscriptorProyectoList.fechaAltaDesde}",
			"suscriptorProyecto.suscriptor.fechaAlta<=#{suscriptorProyectoList.fechaAltaHasta}",
	};

	private SuscriptorProyecto suscriptorProyecto;

	String numeroSuscriptor;
	Long idProyecto;
	Long idModuloActual;
	Date fechaAltaDesde;
	Date fechaAltaHasta;
	String tipoAlta;
	
	
	public SuscriptorProyectoList() {
		suscriptorProyecto = new SuscriptorProyecto();
		suscriptorProyecto.setId(new SuscriptorProyectoId());
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
		setOrderColumn("suscriptorProyecto.suscriptor.fechaAlta");
		setOrderDirection("DESC");
	}

	public SuscriptorProyecto getSuscriptorProyecto() {
		return suscriptorProyecto;
	}

	public String getNumeroSuscriptor() {
		return numeroSuscriptor;
	}

	public void setNumeroSuscriptor(String numeroSuscriptor) {
		this.numeroSuscriptor = numeroSuscriptor;
	}

	public Long getIdProyecto() {
		return idProyecto;
	}

	public void setIdProyecto(Long idProyecto) {
		this.idProyecto = idProyecto;
	}

	public Long getIdModuloActual() {
		return idModuloActual;
	}

	public void setIdModuloActual(Long idModuloActual) {
		this.idModuloActual = idModuloActual;
	}

	public Date getFechaAltaDesde() {
		return GeneralHelper.setToLowestHourOfDay(fechaAltaDesde);
	}

	public void setFechaAltaDesde(Date fechaAltaDesde) {
		this.fechaAltaDesde = fechaAltaDesde;
	}

	public Date getFechaAltaHasta() {
		return GeneralHelper.setToHighestHourOfDay(fechaAltaHasta);
		
	}

	public void setFechaAltaHasta(Date fechaAltaHasta) {
		this.fechaAltaHasta = fechaAltaHasta;
	}

	public String getTipoAlta() {
		return tipoAlta;
	}

	public void setTipoAlta(String tipoAlta) {
		this.tipoAlta = tipoAlta;
	}
	
	
	
}
