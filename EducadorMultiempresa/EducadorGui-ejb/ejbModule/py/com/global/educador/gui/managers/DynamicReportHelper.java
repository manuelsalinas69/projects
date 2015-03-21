package py.com.global.educador.gui.managers;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import net.sf.dynamicreports.adhoc.AdhocManager;
import net.sf.dynamicreports.adhoc.configuration.AdhocColumn;
import net.sf.dynamicreports.adhoc.configuration.AdhocConfiguration;
import net.sf.dynamicreports.adhoc.configuration.AdhocFont;
import net.sf.dynamicreports.adhoc.configuration.AdhocReport;
import net.sf.dynamicreports.adhoc.configuration.AdhocStyle;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.Exporters;
import net.sf.dynamicreports.jasper.builder.export.JasperXlsExporterBuilder;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessages;

import py.com.global.educador.gui.entity.ParametroSistema;
import py.com.global.educador.gui.utils.JpaResourceBean;
import py.com.global.educador.gui.utils.UtilsHelper;

@Name("dynamicReportHelper")
@Scope(ScopeType.PAGE)
public class DynamicReportHelper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	private static final Pattern SUBJECT_PATTERN = Pattern
//			.compile(
//					"^select\\s+(\\w+(?:\\s*\\.\\s*\\w+)*?)(?:\\s*,\\s*(\\w+(?:\\s*\\.\\s*\\w+)*?))*?\\s+from",
//					2);

	@In(create=true)
	EntityManager entityManager;
	@In(create=true)
	StatusMessages statusMessages;
	
	String defaultReportFolder;
	String baseReportName="reporte_";
	String reportExtension=".xls";
	Connection conn;
	Statement st;
	Map<String, Integer> columnIndex;

	@Create
	public void init(){
		
		defaultReportFolder=getDefatultReportFolder();
		System.out.println("DEFAULT REPORT FOLDER--> "+defaultReportFolder);
		
	}
	private String getDefatultReportFolder() {
		try {
			return entityManager.find(ParametroSistema.class, "system.gui.reports.dynamic.folder").getValor();
		} catch (Exception e) {
			System.out
					.println("DynamicReportManager.getDefatultReportFolder(): "+e);
		}
		return null;
	}
	

	@Destroy
	public void destroy(){
		try {
			if (conn!=null) {
				conn.close();
			}
		} catch (Exception e) {
			System.out.println("DynamicReportManager.destroy(): "+e);
		}
	}


	

	private String getReportName(){
		SimpleDateFormat sdf = UtilsHelper.getSimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
		return baseReportName+sdf.format(new Date())+reportExtension;
	}


	public void generateReport(String sql){

	
		
		Connection conn=null;
		AdhocConfiguration configuration = new AdhocConfiguration();
		AdhocReport report = new AdhocReport();
		AdhocStyle colStyle= new AdhocStyle();
		AdhocFont font= new AdhocFont();
		font.setBold(true);
		//colStyle.
		colStyle.setBackgroundColor(new Color(33, 123, 233));
		colStyle.setForegroundColor(Color.WHITE);
		colStyle.setFont(font);
		report.setColumnTitleStyle(colStyle);
		AdhocColumn column = new AdhocColumn();
		column.setName("Numero");
		report.addColumn(column);
		
		
		configuration.setReport(report);

		try {
			String reportName=getReportName();
			String fullPath=defaultReportFolder+File.separator+reportName;
			System.out.println("FULL PATH--> "+fullPath);
			JasperXlsExporterBuilder xlsExporter = Exporters.xlsExporter(fullPath)
					.setDetectCellType(true)
					.setIgnorePageMargins(true)
					.setWhitePageBackground(false)
					.setRemoveEmptySpaceBetweenColumns(true);
			JasperReportBuilder reportBuilder = AdhocManager.createReport(configuration.getReport());
			reportBuilder.ignorePageWidth();
			conn=JpaResourceBean.getConnection();
			reportBuilder.setDataSource(sql,conn);
			reportBuilder.toXls(xlsExporter);
			System.out.println("Descargando Archivo...");
			File f= new File(fullPath);
			FileInputStream fis=new FileInputStream(f);
			byte[] data= new byte[(int) f.length()];
			fis.read(data);
			fis.close();
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			response.setHeader("Content-disposition", "attachment" + "; filename=\"" + reportName);
			response.setContentType("application/vnd.ms-excel");
			OutputStream out;
			byte[] xls =data;
			response.setContentLength(xls.length);
			out = response.getOutputStream();
			out.write(xls);
			out.flush();
			out.close();
			f.delete();

			FacesContext.getCurrentInstance().responseComplete();

		} catch (Exception e) {
			System.out.println("DynamicReportManager.generateReport(): "+e);
			e.printStackTrace();
		}
		finally{
			if (conn!=null) {
				try {
					JpaResourceBean.closeConnection(conn);
				} catch (Exception e) {
					
				}
			}
		}



	}


	
	
	/*
	 * GETTERS && SETTERS
	 * */







}
