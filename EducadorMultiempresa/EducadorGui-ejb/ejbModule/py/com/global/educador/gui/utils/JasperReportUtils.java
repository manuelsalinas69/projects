package py.com.global.educador.gui.utils;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.PrinterName;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataSourceProvider;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.log4j.Logger;

import py.com.global.educador.gui.entity.Usuario;




public class JasperReportUtils {

	private static Logger log = Logger.getLogger(JasperReportUtils.class);
	private static String ruta = "";
	private static SimpleDateFormat sdf;

	public JasperReportUtils() {
		ruta = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
	}

	// *** Implementaciones Mias ***
	/**
	 * Manda un printjob a la cola de la impresora
	 * 
	 * @Param jasperPrint El reporte compilado y llenado.
	 * @Param printerName El nombre de la impresora que va a imprimir.
	 */
	public static void imprimirReporte(JasperPrint jasperPrint, String printerName) throws JRException {
		if (printerName != null && printerName.length() > 0) {
			// create the print service exporter so that we can print to a named
			// printer
			JRPrintServiceExporter exporter = new JRPrintServiceExporter();
			// set the report to print
			exporter.setParameter(JRPrintServiceExporterParameter.JASPER_PRINT, jasperPrint);

			// tell the exporter to print 1 copy on A4 paper
			PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
			aset.add(new Copies(1));
			aset.add(new JobName("Documento SIGBE", null));
			// aset.add(MediaSizeName.ISO_A4);
			exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, aset);

			// let the exporter know which printer we want to print on
			PrintServiceAttributeSet serviceAttributeSet = new HashPrintServiceAttributeSet();
			serviceAttributeSet.add(new PrinterName(printerName, null));

			exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, serviceAttributeSet);

			// print it
			exporter.exportReport();
		} else {
			imprimirReporte(jasperPrint);
		}
	}

	/**
	 * Imprime una reporte con un dialogo de impresion de por medio
	 * 
	 * @Param jasperPrint El reporte compilado y llenado.
	 */
	public static void imprimirReporte(JasperPrint jasperPrint) throws JRException {
		net.sf.jasperreports.engine.JasperPrintManager.printReport(jasperPrint, true);
	}

	/**
	 * Llena un reporte compilado con los datos del Map de parametros
	 * 
	 * @Param reportFile El archivo compilado *.jasper.
	 * @Param parameters Map con parametros necesarios para el llenado del reporte.
	 * @Return El reporte compilado y llenado listo para mostrar o imprimir.
	 */
	@SuppressWarnings("rawtypes")
	public static JasperPrint llenarReporte(File reportFile, Map parameters, Connection conn) throws JRException {
		// agrega el EntityManager al los parametros
		// parameters.put(JRJpaQueryExecuterFactory.PARAMETER_JPA_ENTITY_MANAGER,
		// JpaResourceBean.getInstance().getEMF().createEntityManager());
		// cargar el archivo compilado en un objeto JasperReport
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportFile);
		// llenar y devolver el JasperPrint
		// return JasperFillManager.fillReport(jasperReport,
		return JasperFillManager.fillReport(jasperReport, parameters, conn);
	}

	public static JasperPrint llenarReporteDs(File reportFile, Map parameters, JRDataSource listaDs) throws JRException {
		// agrega el EntityManager al los parametros
		// parameters.put(JRJpaQueryExecuterFactory.PARAMETER_JPA_ENTITY_MANAGER,
		// JpaResourceBean.getInstance().getEMF().createEntityManager());
		// cargar el archivo compilado en un objeto JasperReport
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportFile);
		// llenar y devolver el JasperPrint
		// return JasperFillManager.fillReport(jasperReport,
		//List<Map<String, ?>> l= new ArrayList<Map<String,?>>(listaDs);
		return JasperFillManager.fillReport(jasperReport, parameters, listaDs);
	}

	public static byte[] getPDF(String reportPath, Map parameters, Connection conn) throws JRException, MalformedURLException {
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		File file = new File(servletContext.getRealPath(ruta + "/reports/jasper/" + reportPath));
		return JasperExportManager.exportReportToPdf(llenarReporte(file, parameters, conn));
	}

	public static String getHTML(String reportPath, Map parameters, Connection conn) throws JRException, MalformedURLException {
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		File file = new File(servletContext.getRealPath(ruta + "/reports/jasper/" + reportPath));
		JRHtmlExporter htmlExporter = new JRHtmlExporter();
		htmlExporter.setParameter(JRExporterParameter.JASPER_PRINT, llenarReporte(file, parameters, conn));
		htmlExporter.setParameter(JRHtmlExporterParameter.CHARACTER_ENCODING, "ISO-8859-1");
		StringBuffer htmlStream = new StringBuffer("");
		htmlExporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER, htmlStream);
		htmlExporter.exportReport();
		return htmlStream.toString();
	}

//	public static String getHTML(String reportPath, Map parameters, List<Map<String, Object>> listaDs) throws JRException, MalformedURLException {
//		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
//		JRHtmlExporter htmlExporter = new JRHtmlExporter();
//		File file = new File(servletContext.getRealPath(ruta + "/reports/jasper/" + reportPath));
//		htmlExporter.setParameter(JRExporterParameter.JASPER_PRINT, llenarReporte(file, parameters, listaDs));
//		htmlExporter.setParameter(JRHtmlExporterParameter.CHARACTER_ENCODING, "ISO-8859-1");
//		htmlExporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, false);
//		StringBuffer htmlStream = new StringBuffer("");
//		htmlExporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER, htmlStream);
//		htmlExporter.exportReport();
//		return htmlStream.toString();
//	}

	public static String getHTML(String reportPath, Map parameters) throws JRException, MalformedURLException {
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		JRHtmlExporter htmlExporter = new JRHtmlExporter();
		File file = new File(servletContext.getRealPath(ruta + "/reports/jasper/" + reportPath));
		htmlExporter.setParameter(JRExporterParameter.JASPER_PRINT, JasperFillManager.fillReport((JasperReport) JRLoader.loadObject(file), parameters, new JREmptyDataSource()));
		htmlExporter.setParameter(JRHtmlExporterParameter.CHARACTER_ENCODING, "ISO-8859-1");
		StringBuffer htmlStream = new StringBuffer("");
		htmlExporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER, htmlStream);
		htmlExporter.exportReport();
		return htmlStream.toString();
	}

	public static byte[] getRTF(String reportPath, Map parameters, Connection conn) throws JRException, MalformedURLException {
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		File file = new File(servletContext.getRealPath(ruta + "/reports/jasper/" + reportPath));
		JRRtfExporter rtfExporter = new JRRtfExporter();
		rtfExporter.setParameter(JRExporterParameter.JASPER_PRINT, llenarReporte(file, parameters, conn));
		ByteArrayOutputStream rtfStream = new ByteArrayOutputStream();
		rtfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, rtfStream);
		rtfExporter.exportReport();
		return rtfStream.toByteArray();
	}

	public static byte[] getPDF(JasperPrint jasperPrint) throws JRException, MalformedURLException {
		@SuppressWarnings("unused")
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		return JasperExportManager.exportReportToPdf(jasperPrint);
	}

	public static String getXML(String reportPath, Map parameters, Connection conn) throws JRException {
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		File file = new File(servletContext.getRealPath(ruta + "/reports/jasper/" + reportPath));
		return JasperExportManager.exportReportToXml(llenarReporte(file, parameters, conn));
	}

	public static byte[] getXLS(String reportPath, Map parameters, Connection conn) throws JRException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		File file = new File(servletContext.getRealPath(ruta + "/reports/jasper/" + reportPath));
		JRXlsExporter exporterXLS = new JRXlsExporter();
		exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, llenarReporte(file, parameters, conn));
		exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
		exporterXLS.exportReport();
		return byteArrayOutputStream.toByteArray();
	}

	public static byte[] getXLS(String reportPath, Map parameters, JRDataSource listaDs) throws JRException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		File file = new File(servletContext.getRealPath(ruta + "/reports/jasper/" + reportPath));
		JRXlsExporter exporterXLS = new JRXlsExporter();
		exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, llenarReporteDs(file, parameters, listaDs));
		exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
		exporterXLS.exportReport();
		return byteArrayOutputStream.toByteArray();
	}

	/**
	 * Devuelve el reporte al browser en pdf, indicar nombre, y si se muestra o se baja
	 **/
	public static void respondPDF(String reportName, Map parameters, boolean inline, Connection conn, Usuario usuario) {
		// auditarReportes(reportName, "IMPRIMIR_PDF", usuario);
		sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		String disp = (inline) ? "inline" : "attachment";
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.setHeader("Content-disposition", disp + "; filename=\"" + reportName + "_" + sdf.format(new Date()) + ".pdf\"");
		response.setContentType("application/pdf");
		OutputStream out;
		try {
			byte[] pdf = JasperReportUtils.getPDF(reportName + ".jasper", parameters, conn);
			response.setContentLength(pdf.length);
			out = response.getOutputStream();
			out.write(pdf);
			out.flush();
			out.close();
			FacesContext.getCurrentInstance().responseComplete();
		} catch (JRException ex) {
			log.debug(ex.getMessage());
		} catch (IOException e) {
			log.debug(e.getMessage());
		}
	}

	/**
	 * Devuelve el reporte al browser en rtf, indicar nombre, y si se muestra o se baja
	 **/
	public static void respondRTF(String reportName, Map parameters, boolean inline, Connection conn, Usuario usuario) {
		sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		String disp = (inline) ? "inline" : "attachment";
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.setHeader("Content-disposition", disp + "; filename=\"" + reportName + "_" + sdf.format(new Date()) + ".rtf\"");
		response.setContentType("application/rtf");
		OutputStream out;
		try {
			byte[] rtf = JasperReportUtils.getRTF(reportName + ".jasper", parameters, conn);
			response.setContentLength(rtf.length);
			out = response.getOutputStream();
			out.write(rtf);
			out.flush();
			out.close();
			FacesContext.getCurrentInstance().responseComplete();
		} catch (JRException ex) {
			log.debug(ex.getMessage());
		} catch (IOException e) {
			log.debug(e.getMessage());
		}
	}

//	public static void respondXLS(String reportName, Map parameters, Connection conn, Usuario usuario) {
//		// auditarReportes(reportName, "IMPRIMIR_EXCEL", usuario);
//		sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
//		String disp = "attachment";
//		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//		response.setHeader("Content-disposition", disp + "; filename=\"" + reportName + "_" + sdf.format(new Date()) + ".xls\"");
//		response.setContentType("application/vnd.ms-excel");
//		OutputStream out;
//		try {
//			byte[] xls = JasperReportUtils.getXLS(reportName + ".jasper", parameters, conn);
//			response.setContentLength(xls.length);
//			out = response.getOutputStream();
//			out.write(xls);
//			out.flush();
//			out.close();
//			FacesContext.getCurrentInstance().responseComplete();
//		} catch (JRException ex) {
//			log.debug(ex.getMessage());
//		} catch (IOException e) {
//			log.debug(e.getMessage());
//		}
//	}

	public static void respondXLS(String reportName, Map parameters, JRDataSource listaDs, Usuario usuario) {
		// auditarReportes(reportName, "IMPRIMIR_EXCEL", usuario);
		sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		String disp = "attachment";
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.setHeader("Content-disposition", disp + "; filename=\"" + reportName + "_" + sdf.format(new Date()) + ".xls\"");
		response.setContentType("application/vnd.ms-excel");
		OutputStream out;
		try {
			byte[] xls = JasperReportUtils.getXLS(reportName + ".jasper", parameters, listaDs);
			response.setContentLength(xls.length);
			out = response.getOutputStream();
			out.write(xls);
			out.flush();
			out.close();
			FacesContext.getCurrentInstance().responseComplete();
		} catch (JRException ex) {
			log.debug(ex.getMessage());
		} catch (IOException e) {
			log.debug(e.getMessage());
		}
	}

	/** Guarda el reporte en pdf, indicar nombre, parametros y realPath **/
	public static void guardarPDF(String reportName, Map parameters, String realPath, Connection conn, Usuario usuario) {
		try {
			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			File file = new File(servletContext.getRealPath(ruta + "/reports/" + reportName + "_" + sdf.format(new Date()) + ".jasper"));
			JasperExportManager.exportReportToPdfFile(llenarReporte(file, parameters, conn), realPath);
		} catch (JRException ex) {
			log.debug(ex.getMessage());
		}
	}

	/**
	 * Devuelve el reporte al browser en pdf a partir de un JasperPrint, indicar nombre, y si se muestra o se baja
	 **/
	public static void respondPDF(JasperPrint jasperPrint, String reportName, boolean inline, Usuario usuario) {
		// auditarReportes(reportName, "IMPRIMIR_PDF", usuario);
		sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		String disp = (inline) ? "inline" : "attachment";
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.setHeader("Content-disposition", disp + "; filename=\"" + reportName + "_" + sdf.format(new Date()) + ".pdf\"");
		response.setContentType("application/pdf");
		OutputStream out;
		try {
			byte[] pdf = JasperReportUtils.getPDF(jasperPrint);
			response.setContentLength(pdf.length);
			out = response.getOutputStream();
			out.write(pdf);
			out.flush();
			out.close();
			FacesContext.getCurrentInstance().responseComplete();
		} catch (JRException ex) {
			log.debug(ex.getMessage());
		} catch (IOException e) {
			log.debug(e.getMessage());
		}
	}

	

	
	
	
	/**
	 * Devuelve un List de byte (byte[]) recibiendo una lista de Map<String,Object>
	 * @param nombreReporte
	 * @param listaDS
	 * @param parametros
	 * @return
	 */
	public static byte[] generarReporte(String nombreReporte, Collection<Map<String, Object>> listaDS, HashMap parametros) {
		byte[] reporte = null;
		try {
			// Locale locale = Locale.getDefault(); //new Locale("en", "US");
			// parametros.put(JRParameter.REPORT_LOCALE, locale);
			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			List<Map<String, ?>> l= new ArrayList<Map<String,?>>(listaDS);
			JasperPrint jp = JasperFillManager
					.fillReport(servletContext.getRealPath(ruta + "/reports/jasper/" + nombreReporte), parametros, new JRMapCollectionDataSource(l));
			if (jp.getPages().size() > 0) {
				reporte = JasperExportManager.exportReportToPdf(jp);
			}
		} catch (Exception e) {
			log.debug("generarReporte: ", e);
		}
		return reporte;
	}
	
	/**
	 * Devuelve un List de byte (byte[]) recibiendo un DataSource del tipo JRMapCollectionDataSource
	 * @param nombreReporte
	 * @param listaDS
	 * @param parametros
	 * @return byte[]
	 */
	public static byte[] generarReporte(String nombreReporte, JRMapCollectionDataSource listaDS, HashMap parametros) {
		byte[] reporte = null;
		try {
			// Locale locale = Locale.getDefault(); //new Locale("en", "US");
			// parametros.put(JRParameter.REPORT_LOCALE, locale);
			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			JasperPrint jp = JasperFillManager
					.fillReport(servletContext.getRealPath(ruta + "/reports/jasper/" + nombreReporte), parametros, listaDS);
			if (jp.getPages().size() > 0) {
				reporte = JasperExportManager.exportReportToPdf(jp);
			}
		} catch (Exception e) {
			log.debug("generarReporte: ", e);
		}
		return reporte;
	}

	/**
	 * Genera el PDF recibiendo una lista de Map<String,Object>
	 * @param reportName
	 * @param inline
	 * @param listaDS
	 * @param parametros
	 */
	public static void respondPDF(String reportName, boolean inline, List<Map<String, Object>> listaDS, HashMap parametros) {
		sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		String disp = (inline) ? "inline" : "attachment";
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.setHeader("Content-disposition", disp + "; filename=\"" + reportName + "_" + sdf.format(new Date()) + ".pdf\"");
		response.setContentType("application/pdf");
		OutputStream out;
		try {
			byte[] pdf = JasperReportUtils.generarReporte(reportName + ".jasper", listaDS, parametros);
			response.setContentLength(pdf.length);
			out = response.getOutputStream();
			out.write(pdf);
			out.flush();
			out.close();
			FacesContext.getCurrentInstance().responseComplete();
		} catch (IOException e) {
			log.debug(e.getMessage());
		}
	}
	
	/**
	 * Genera el PDF recibiendo un DataSource del tipo JRMapCollectionDataSource
	 * @param reportName
	 * @param inline
	 * @param listaDS
	 * @param parametros
	 */
	public static void respondPDF(String reportName, boolean inline, JRMapCollectionDataSource listaDS, HashMap parametros) {
		sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		String disp = (inline) ? "inline" : "attachment";
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.setHeader("Content-disposition", disp + "; filename=\"" + reportName + "_" + sdf.format(new Date()) + ".pdf\"");
		response.setContentType("application/pdf");
		OutputStream out;
		try {
			byte[] pdf = JasperReportUtils.generarReporte(reportName + ".jasper", listaDS, parametros);
			response.setContentLength(pdf.length);
			out = response.getOutputStream();
			out.write(pdf);
			out.flush();
			out.close();
			FacesContext.getCurrentInstance().responseComplete();
		} catch (IOException e) {
			log.debug(e.getMessage());
		}
	}

	public static void respondPDF(String reportName, boolean inline,
			Collection listaDS, HashMap<String, Object> parametros) {
		sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		String disp = (inline) ? "inline" : "attachment";
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.setHeader("Content-disposition", disp + "; filename=\"" + reportName + "_" + sdf.format(new Date()) + ".pdf\"");
		response.setContentType("application/pdf");
		OutputStream out;
		try {
			byte[] pdf = JasperReportUtils.generarReporte(reportName + ".jasper",  listaDS, parametros, null);
			response.setContentLength(pdf.length);
			out = response.getOutputStream();
			out.write(pdf);
			out.flush();
			out.close();
			FacesContext.getCurrentInstance().responseComplete();
		} catch (IOException e) {
			log.debug(e.getMessage());
		}
		
	}

	private static byte[] generarReporte(String nombreReporte,
			Collection listaDS, HashMap<String, Object> parametros, String option) {
		byte[] reporte = null;
		try {
			// Locale locale = Locale.getDefault(); //new Locale("en", "US");
			// parametros.put(JRParameter.REPORT_LOCALE, locale);
			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			JasperPrint jp = JasperFillManager
					.fillReport(servletContext.getRealPath(ruta + "/reports/jasper/" + nombreReporte), parametros, new JRMapCollectionDataSource(listaDS));;
			if (jp.getPages().size() > 0) {
				reporte = JasperExportManager.exportReportToPdf(jp);
			}
		} catch (Exception e) {
			log.debug("generarReporte: ", e);
		}
		return reporte;
	}


	
	public static JRDataSource convertReportData(Object value) throws IllegalArgumentException {
		if (value instanceof JRDataSource) {
			return (JRDataSource) value;
		}
		else if (value instanceof Collection) {
			return new JRBeanCollectionDataSource((Collection) value);
		}
		else if (value instanceof Object[]) {
			return new JRBeanArrayDataSource((Object[]) value);
		}
		else if (value instanceof JRDataSourceProvider) {
			return null;
		}
		else {
			throw new IllegalArgumentException("Value [" + value + "] cannot be converted to a JRDataSource");
		}
	}
	
	
	private static byte[] generarReporte(String nombreReporte,
			JRDataSource ds, HashMap<String, Object> parametros, String option) {
		byte[] reporte = null;
		try {
			// Locale locale = Locale.getDefault(); //new Locale("en", "US");
			// parametros.put(JRParameter.REPORT_LOCALE, locale);
			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			
			JasperPrint jp = JasperFillManager
					.fillReport(servletContext.getRealPath("/reports/jasper/" + nombreReporte), parametros, ds);
			if (jp.getPages().size() > 0) {
				reporte = JasperExportManager.exportReportToPdf(jp);
			}
		} catch (Exception e) {
			log.error("generarReporte: ", e);
		}
		return reporte;
	}
	
	public static void respondPDF(String reportName, boolean inline,
			JRDataSource ds, HashMap<String, Object> parametros) {
		sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		String disp = (inline) ? "inline" : "attachment";
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.setHeader("Content-disposition", disp + "; filename=\"" + reportName + "_" + sdf.format(new Date()) + ".pdf\"");
		response.setContentType("application/pdf");
		OutputStream out;
		try {
			byte[] pdf = JasperReportUtils.generarReporte(reportName + ".jasper",  ds, parametros, null);
			response.setContentLength(pdf.length);
			out = response.getOutputStream();
			out.write(pdf);
			out.flush();
			out.close();
			FacesContext.getCurrentInstance().responseComplete();
		} catch (IOException e) {
			log.debug(e.getMessage());
		}
		
	}
	
}
