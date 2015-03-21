package py.com.global.educador.gui.managers;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.jasperreports.engine.JRDataSource;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;

import py.com.global.educador.gui.entity.ParametroSistema;
import py.com.global.educador.gui.utils.JpaResourceBean;
import py.com.global.educador.gui.utils.UtilsHelper;

@Name("dynamicReportManager")
@Scope(ScopeType.PAGE)
public class DynamicReportManager implements Serializable {

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
		result= new ArrayList<Object>();
		columns= new ArrayList<String>();
		defaultReportFolder=getDefatultReportFolder();
		columnIndex= new HashMap<String, Integer>();
		System.out.println("DEFAULT REPORT FOLDER--> "+defaultReportFolder);
		createConnection();
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
	private void createConnection() {
		try {
			// conn = entityManager.unwrap(Session.class).connection();
		} catch (Exception e) {
			System.out.println("DynamicReportManager.createConnection(): "+e);
		}

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

	Long idPlantillaReporte;
	String sql;
	List<String> columns;
	List<Object> result;
	boolean singleRow;

	public void executeSql(){
		//boolean useWildCard=false;
		columnIndex.clear();
		if (columns==null) {
			columns= new ArrayList<String>();
		}
		if (result==null) {
			result= new ArrayList<Object>();
		}
		columns.clear();
		result.clear();
		ResultSet rs=null;
		Statement st=null;
		Connection conn=null;
		try {
			if (sql==null) {
				statusMessages.add(Severity.ERROR,"Debe ingresar una consulta a ejecutar");
				return;
			}

			conn=JpaResourceBean.getConnection();
			st=conn.createStatement();
			st.setMaxRows(100);
			rs = st.executeQuery(sql);
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount=metaData.getColumnCount();
			if (columns==null) {
				columns= new ArrayList<String>();
			}
			for (int i = 1; i <= columnCount; i++) {
				columns.add(metaData.getColumnLabel(i));
				columnIndex.put(metaData.getColumnLabel(i), (i-1));
			}
			Object[] row;


			while (rs.next()) {
				row=new Object[columnCount];
				for (int i = 1; i <= columnCount;i++) {
					row[i-1]=rs.getObject(i);
				}
				result.add(row);
			} 




		} catch (Exception e) {
			System.out.println("DynamicReportManager.test(): "+e);
			e.printStackTrace();
		}
		finally{
			try {
				if (rs!=null) {
					rs.close();

				}
				if (st!=null) {
					st.close();
				}
				if (conn!=null) {
					JpaResourceBean.closeConnection(conn);
				}
			} catch (Exception e2) {
				System.out.println("DynamicReportManager.executeSql(): "+e2);
			}
		}
	}


	public void generateReport_2(){
		Connection conn=null;
		try {
			conn=JpaResourceBean.getConnection();
			net.sf.dynamicreports.report.builder.DynamicReports.report().setDataSource(sql, conn);
		} catch (Exception e) {
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

	private String getReportName(){
		SimpleDateFormat sdf = UtilsHelper.getSimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
		return baseReportName+sdf.format(new Date())+reportExtension;
	}


	public void generateReport(){

		if (result==null || result.isEmpty()) {
			return;
		}

		if (columns==null || columns.isEmpty()) {
			return;
		}
		
		
		Connection conn=null;
		AdhocConfiguration configuration = new AdhocConfiguration();
		AdhocReport report = new AdhocReport();
		
		
		configuration.setReport(report);
		AdhocStyle colStyle= new AdhocStyle();
		AdhocFont font= new AdhocFont();
		font.setBold(true);
		//colStyle.
		colStyle.setBackgroundColor(new Color(33, 123, 233));
		colStyle.setForegroundColor(Color.WHITE);
		colStyle.setFont(font);
		report.setColumnTitleStyle(colStyle);

		for (String columnName : columns) {
			AdhocColumn column = new AdhocColumn();
			column.setName(columnName);
			report.addColumn(column);
		}

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

	private JRDataSource createDataSource() {
		String [] c= new String [columns.size()];
		DRDataSource ds= new DRDataSource(columns.toArray(c));
		for (Object r : result) {
			ds.add((Object[])r);
		}

		return ds;
	}
	
	public int getIndexOfColumn(String columnName){
		try {
			return ((Integer)columnIndex.get(columnName)).intValue();
		} catch (Exception e) {
			System.out.println("DynamicReportManager.getIndexOfColumn(): "+e);
		}
		return -1;
	}

	/*
	 * GETTERS && SETTERS
	 * */




	public String getSql() {
		return sql;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<Object> getResult() {
		return result;
	}

	public void setResult(List<Object> result) {
		this.result = result;
	}


	public boolean isSingleRow() {
		return singleRow;
	}


	public void setSingleRow(boolean singleRow) {
		this.singleRow = singleRow;
	}


	public Long getIdPlantillaReporte() {
		return idPlantillaReporte;
	}


	public void setIdPlantillaReporte(Long idPlantillaReporte) {
		this.idPlantillaReporte = idPlantillaReporte;
	}
	public Map<String, Integer> getColumnIndex() {
		return columnIndex;
	}
	public void setColumnIndex(Map<String, Integer> columnIndex) {
		this.columnIndex = columnIndex;
	}




}
