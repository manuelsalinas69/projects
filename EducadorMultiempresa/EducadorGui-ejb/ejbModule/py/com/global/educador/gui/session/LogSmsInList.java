package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import py.com.global.educador.gui.utils.GeneralHelper;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;
import java.util.Date;

@Name("logSmsInList")
public class LogSmsInList extends EntityQuery<LogSmsIn> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select logSmsIn from LogSmsIn logSmsIn";

	private static final String[] RESTRICTIONS = {
			"lower(logSmsIn.numeroDestino) like lower(concat(#{logSmsInList.logSmsIn.numeroDestino},'%'))",
			"lower(logSmsIn.numeroOrigen) like lower(concat(#{logSmsInList.logSmsIn.numeroOrigen},'%'))",
			"lower(logSmsIn.mensaje) like lower(concat('%',#{logSmsInList.logSmsIn.mensaje},'%'))",
			"lower(logSmsIn.jmsMessageId) like lower(concat(#{logSmsInList.logSmsIn.jmsMessageId},'%'))", 
			"logSmsIn.fechaRecepcion >=#{logSmsInList.fechaRecepcionDesde}",
			"logSmsIn.fechaRecepcion <=#{logSmsInList.fechaRecepcionHasta}",};

	private LogSmsIn logSmsIn = new LogSmsIn();
	private Date fechaRecepcionDesde, fechaRecepcionHasta;
	

	public LogSmsInList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
		setOrderColumn("fechaRecepcion");
		setOrderDirection("DESC");
	}

	public LogSmsIn getLogSmsIn() {
		return logSmsIn;
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
