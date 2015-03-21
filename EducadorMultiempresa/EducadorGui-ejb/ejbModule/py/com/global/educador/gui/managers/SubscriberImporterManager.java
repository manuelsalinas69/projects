package py.com.global.educador.gui.managers;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import py.com.global.educador.gui.configuration.EducadorGui.Constants.SystemParameterKey;
import py.com.global.educador.gui.entity.ParametroSistema;
import py.com.global.educador.gui.entity.Proyecto;
import py.com.global.educador.gui.utils.Reader;

@Name("subscriberImporterManager")
@Scope(ScopeType.PAGE)
public class SubscriberImporterManager implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@In EntityManager entityManager;
	@In StatusMessages statusMessages;
	@In(create=true) SubscriberWorker subscriberWorker;
	Long idProyecto;
	String operacion;
	
	byte[] data;
	String fileName;
	
	@Create
	public void init(){
		
	}
	
	public void fileUploadListener(FileUploadEvent event){
		UploadedFile file= event.getUploadedFile();
		data=file.getData();
		fileName=file.getName();
		
	}
	
	public void processFile(){
		if (idProyecto==null) {
			statusMessages.add(Severity.ERROR,"Debe seleccionar un proyecto");
			return;
		}
		
		if (operacion==null || operacion.trim().isEmpty()) {
			statusMessages.add(Severity.ERROR,"Debe seleccionar una operacion");
			return;
		}
		
		
		if (data==null) {
			statusMessages.add(Severity.ERROR,"No hay datos para procesar");
			return;
		}
		List<String> numberList;
		if (fileName.endsWith("xlsx")) {
			
			numberList=Reader.readXlsx(data, org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC);
		}
		else {
			numberList=Reader.readXlsx(data, org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC);
		}
		String host=getServerHost();
		String port=getServerPort();
		String numeroCorto=getNumeroCorto(idProyecto);
		subscriberWorker.processList(numberList, operacion, numeroCorto, host, port);
		statusMessages.add(Severity.INFO,"Pedido de suscripcion enviando a la plataforma con exito, para ver los resultados del pedido verifique los registros de suscripciones en /Consultas/Registros de Suscripciones");
	}

	
	/*
	 * GETTERS && SETTERS
	 * 
	 * */
	
	
	private String getNumeroCorto(Long idProyecto) {
		try {
			Proyecto p= entityManager.find(Proyecto.class, idProyecto);
			return p.getNumeroCorto();
		} catch (Exception e) {
			System.out.println("SubscriberImporterManager.getNumeroCorto(): "+e);
		}
		return "606";
	}

	private String getServerHost() {
		try {
			return entityManager.find(ParametroSistema.class, SystemParameterKey.SYSTEM_SERVER_HOST).getValor();
		} catch (Exception e) {
			System.out.println("SubscriberImporterManager.getServerHost(): "+e);
		}
		return "localhost";//por defecto se asume que esta en la misma maquina
	}

	private String getServerPort() {
		try {
			return entityManager.find(ParametroSistema.class, SystemParameterKey.SYSTEM_SERVER_PORT).getValor();
		} catch (Exception e) {
			System.out.println("SubscriberImporterManager.getServerPort(): "+e);
		}
		return "8280";
	}

	public Long getIdProyecto() {
		return idProyecto;
	}

	public void setIdProyecto(Long idProyecto) {
		this.idProyecto = idProyecto;
	}

	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}
	

	public boolean isFileDefined(){
		return data!=null && data.length>=0 && fileName!=null && !fileName.trim().isEmpty();
	}
	

}
