package py.com.global.educador.gui.managers;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
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
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

import py.com.global.educador.gui.entity.ParametroSistema;
import py.com.global.educador.gui.entity.ReporteDetalles;
import py.com.global.educador.gui.utils.EntityInterface;
import py.com.global.educador.gui.utils.EnumAppHelper;
import py.com.global.educador.gui.utils.GeneralHelper;
import py.com.global.educador.gui.utils.UtilsHelper;

@Name("dynamicEntityReportHelper")
@Scope(ScopeType.PAGE)
public class DynamicEntityReportHelper implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	@In(create=true)
	EntityManager entityManager;
	@In(create=true)
	StatusMessages statusMessages;
	
	
	@Logger
	Log log;

	String defaultReportFolder;
	String baseReportName="reporte_";
	String reportExtension=".xls";
	
	@Create
	public void init(){
		defaultReportFolder=getDefatultReportFolder();

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

	public<T extends EntityQuery<EntityInterface>> void excelReport(String view, T entityQuery){
		if (view==null || view.trim().isEmpty()) {
			log.error("El nombre de la vista es nulo");
			return;
		}
		try {
			//entityQuery.
			
			List<ReporteDetalles> confDetail=getViewCondigurationDetails(view);
			if (confDetail==null || confDetail.isEmpty()) {
				log.error("No se encontraron detalles de configuracion de reporte para la vista ["+view+"]");
				return;
			}
			
			List<String> columns=getColumnsConfig(confDetail);
			List<Object[]> result=getResultConfig(confDetail, entityQuery);
			generateReport(result, columns);
		} catch (Exception e) {
			log.error(e);
		}

	}
	
	public<T extends EntityInterface> void excelReportFromList(String view, List<T> entityList){
		if (view==null || view.trim().isEmpty()) {
			log.error("El nombre de la vista es nulo");
			return;
		}
		if (entityList==null || entityList.isEmpty()) {
			log.error("La lista pasada para la vista ["+view+"] es vacia o nula");
			return;
		}
		try {
			
			
			List<ReporteDetalles> confDetail=getViewCondigurationDetails(view);
			if (confDetail==null || confDetail.isEmpty()) {
				log.error("No se encontraron detalles de configuracion de reporte para la vista ["+view+"]");
				return;
			}
			
			List<String> columns=getColumnsConfig(confDetail);
			List<Object[]> result=getResultConfig(confDetail, entityList);
			generateReport(result, columns);
		} catch (Exception e) {
			log.error(e);
		}

	}

	private List<String> getColumnsConfig(List<ReporteDetalles> confDetail) {
		List<String> l= new ArrayList<String>();
		for (ReporteDetalles rd : confDetail) {
			l.add(rd.getNombreCampo());
		}
		return l;
	}

	private<T extends EntityQuery<EntityInterface>> List<Object[]> getResultConfig(List<ReporteDetalles> _rd, T entityQuery){
//		Object[] _row;
//		List<Object[]> r= new ArrayList<Object[]>();
		entityQuery.setFirstResult(0);
		entityQuery.setMaxResults(null);
		List<EntityInterface> rl=entityQuery.getResultList();
//		for (EntityInterface ei : rl) {
//			_row=getArrayRow(ei,_rd);
//			r.add(_row);
//		}
		return getResultConfig(_rd, rl);
	}
	private<T extends EntityInterface> List<Object[]> getResultConfig(List<ReporteDetalles> _rd, List<T> list){
		Object[] _row;
		List<Object[]> r= new ArrayList<Object[]>();
		
		for (EntityInterface ei : list) {
			_row=getArrayRow(ei,_rd);
			r.add(_row);
		}
		return r;
	}
	
	private Object[] getArrayRow(EntityInterface ei, List<ReporteDetalles> _rd) {
		Object[] _row= new Object[_rd.size()];
		for (int i = 0; i < _rd.size(); i++) {
			try {
				String v=_rd.get(i).getValor().trim();
				if (v.startsWith("<enum>")) {
					String [] params=v.split(",");
					
					_row[i]=EnumAppHelper.valueOfEnum(params[1].trim(), params[2].trim(), GeneralHelper.getObjetValue(ei, params[3].trim()));;
				}
				else{
					_row[i]=GeneralHelper.getObjetValue(ei, v);
				}
			} catch (Exception e) {
				System.out.println("DynamicEntityReportHelper.getArrayRow(): "+e);
			}
			
		}
		return _row;
	}

	@SuppressWarnings("unchecked")
	private List<ReporteDetalles> getViewCondigurationDetails(String vista) {
		
		String hql="SELECT _rd FROM ReporteDetalles _rd WHERE _rd.vista= :vista ORDER BY _rd.orden";
		
		Query q= entityManager.createQuery(hql);
		
		q.setParameter("vista", vista);
		return q.getResultList();
	}

	private String getReportName(){
		SimpleDateFormat sdf = UtilsHelper.getSimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
		return baseReportName+sdf.format(new Date())+reportExtension;
	}

	public void generateReport(List<Object[]>result,List<String> columns){

		if (result==null || result.isEmpty()) {
			return;
		}

		if (columns==null || columns.isEmpty()) {
			return;
		}
		AdhocConfiguration configuration = new AdhocConfiguration();
		//configuration.
		//configuration.
		AdhocReport report = new AdhocReport();
		//report.setHighlightDetailEvenRows(true);
//		AdhocPage page= new AdhocPage();
//		page.setWidth(columns.size()*150);
	
//		report.setPage(page);
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
			//column.setWidth(150);
			report.addColumn(column);
		}

		try {
			String reportName=getReportName();
			String fullPath=defaultReportFolder+File.separator+reportName;
			log.debug("FULL PATH--> "+fullPath);
			JasperXlsExporterBuilder xlsExporter = Exporters.xlsExporter(fullPath)
					.setDetectCellType(true)
					.setIgnorePageMargins(true)
					.setWhitePageBackground(false)
					.setRemoveEmptySpaceBetweenColumns(true);
			JasperReportBuilder reportBuilder = AdhocManager.createReport(configuration.getReport());
			reportBuilder.ignorePageWidth();
			reportBuilder.setDataSource(createDataSource(result,columns));
			reportBuilder.toXls(xlsExporter);
			log.debug("Descargando Archivo");
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
			log.debug("DynamicReportManager.generateReport(): "+e);
			e.printStackTrace();
		}



	}

	private JRDataSource createDataSource(List<Object[]> result, List<String> columns) {
		String [] c= new String [columns.size()];
		DRDataSource ds= new DRDataSource(columns.toArray(c));
		for (Object[] r : result) {
			
				ds.add((Object[])r);
			}

		return ds;
	}

}
