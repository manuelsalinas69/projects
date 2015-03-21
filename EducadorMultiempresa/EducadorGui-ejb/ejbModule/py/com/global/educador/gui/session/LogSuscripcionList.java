package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import py.com.global.educador.gui.utils.GeneralHelper;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;
import java.util.Date;

@Name("logSuscripcionList")
public class LogSuscripcionList extends EntityQuery<LogSuscripcion> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select logSuscripcion from LogSuscripcion logSuscripcion";

	private static final String[] RESTRICTIONS = {
			"lower(logSuscripcion.mensaje) like lower(concat(#{logSuscripcionList.logSuscripcion.mensaje},'%'))",
			"lower(logSuscripcion.accion) like lower(concat(#{logSuscripcionList.logSuscripcion.accion},'%'))",
			"lower(logSuscripcion.resultado) like lower(concat(#{logSuscripcionList.logSuscripcion.resultado},'%'))",
			"lower(logSuscripcion.numero) like lower(concat(#{logSuscripcionList.logSuscripcion.numero},'%'))",
			"lower(logSuscripcion.proyecto) like lower(concat(#{logSuscripcionList.logSuscripcion.proyecto},'%'))",
			"lower(logSuscripcion.tipoSuscripcion) like lower(concat(#{logSuscripcionList.logSuscripcion.tipoSuscripcion},'%'))", 
			"logSuscripcion.fechaRecepcion>=#{logSuscripcionList.fechaRecepcionDesde}",
			"logSuscripcion.fechaRecepcionHasta<=#{logSuscripcionList.fechaRecepcionHasta}",};

	private LogSuscripcion logSuscripcion = new LogSuscripcion();
	private Date fechaRecepcionDesde, fechaRecepcionHasta;

	public LogSuscripcionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
		setOrderColumn("fechaRecepcion");
		setOrderDirection("DESC");
	}

	public LogSuscripcion getLogSuscripcion() {
		return logSuscripcion;
	}

	public Date getFechaRecepcionDesde() {
		return GeneralHelper.setToLowestHourOfDay(fechaRecepcionDesde);
	}

	public void setFechaRecepcionDesde(Date fechaRecepcionDesde) {
		this.fechaRecepcionDesde = fechaRecepcionDesde;
	}

	public Date getFechaRecepcionHasta() {
		return GeneralHelper.setToHighestHourOfDay(fechaRecepcionHasta);
	}

	public void setFechaRecepcionHasta(Date fechaRecepcionHasta) {
		this.fechaRecepcionHasta = fechaRecepcionHasta;
	}
	
	
}
