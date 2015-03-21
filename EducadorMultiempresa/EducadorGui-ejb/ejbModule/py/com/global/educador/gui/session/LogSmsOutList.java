package py.com.global.educador.gui.session;

import py.com.global.educador.gui.entity.*;
import py.com.global.educador.gui.utils.GeneralHelper;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;
import java.util.Date;

@Name("logSmsOutList")
public class LogSmsOutList extends EntityQuery<LogSmsOut> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select logSmsOut from LogSmsOut logSmsOut";

	private static final String[] RESTRICTIONS = {
			"lower(logSmsOut.numeroDestino) like lower(concat(#{logSmsOutList.logSmsOut.numeroDestino},'%'))",
			"lower(logSmsOut.numeroOrigen) like lower(concat('%',#{logSmsOutList.logSmsOut.numeroOrigen},'%'))",
			"lower(logSmsOut.mensaje) like lower(concat('%',#{logSmsOutList.logSmsOut.mensaje},'%'))",
			"lower(logSmsOut.estado) like lower(concat(#{logSmsOutList.logSmsOut.estado},'%'))",
			"lower(logSmsOut.respuestaSmsc) like lower(concat('%',#{logSmsOutList.logSmsOut.respuestaSmsc},'%'))",
			"logSmsOut.fechaPeticionEnvio>=#{logSmsOutList.fechaPeticionEnvioDesde}",
			"logSmsOut.fechaPeticionEnvio<=#{logSmsOutList.fechaPeticionEnvioHasta}",
			"logSmsOut.fechaEnvio>=#{logSmsOutList.fechaEnvioDesde}",
			"logSmsOut.fechaEnvio<=#{logSmsOutList.fechaEnvioHasta}",};

	private LogSmsOut logSmsOut = new LogSmsOut();
	Date fechaPeticionEnvioDesde, fechaPeticionEnvioHasta;
	Date fechaEnvioDesde, fechaEnvioHasta;

	public LogSmsOutList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
		setOrderColumn("fechaPeticionEnvio");
		setOrderDirection("DESC");
	}

	public LogSmsOut getLogSmsOut() {
		return logSmsOut;
	}

	public Date getFechaPeticionEnvioDesde() {
		return GeneralHelper.setToLowestHourOfDay(fechaPeticionEnvioDesde);
	}

	public void setFechaPeticionEnvioDesde(Date fechaPeticionEnvioDesde) {
		this.fechaPeticionEnvioDesde = fechaPeticionEnvioDesde;
	}

	public Date getFechaPeticionEnvioHasta() {
		return GeneralHelper.setToHighestHourOfDay(fechaPeticionEnvioHasta);
	}

	public void setFechaPeticionEnvioHasta(Date fechaPeticionEnvioHasta) {
		this.fechaPeticionEnvioHasta = fechaPeticionEnvioHasta;
	}

	public Date getFechaEnvioDesde() {
		return GeneralHelper.setToLowestHourOfDay(fechaEnvioDesde);
	}

	public void setFechaEnvioDesde(Date fechaEnvioDesde) {
		this.fechaEnvioDesde = fechaEnvioDesde;
	}

	public Date getFechaEnvioHasta() {
		return GeneralHelper.setToHighestHourOfDay(fechaEnvioHasta);
	}

	public void setFechaEnvioHasta(Date fechaEnvioHasta) {
		this.fechaEnvioHasta = fechaEnvioHasta;
	}
	
	
}
