package py.com.global.educador.gui.managers;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.SelectItem;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import py.com.global.educador.gui.utils.SelectItemsHelper;

@Name("selectorManager")
@Scope(ScopeType.PAGE)
public class SelectorManager implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In(create=true)
	SelectItemsHelper selectItemsHelper;
	
	List<SelectItem> proyectos;
	List<SelectItem> modulos;
	Long idEmpresa;
	String hashId;
	Long idProyecto;
	
	@Create
	public void init(){
		proyectos=selectItemsHelper.emptySelectItemList();
		modulos=selectItemsHelper.emptySelectItemList();
	}
	
	public void populateProyectoList(String hashId){
		if (hashId==null || hashId.trim().isEmpty()) {
			proyectos=selectItemsHelper.emptySelectItemList();
		}
		proyectos=selectItemsHelper.proyectoByEmpresaSelectItems(hashId);
	}
	
	public void populateProyectoListByEmpresaId(Long idEmpresa){
		if (idEmpresa==null) {
			proyectos=selectItemsHelper.emptySelectItemList();
		}
		proyectos=selectItemsHelper.proyectoByEmpresaSelectItems(idEmpresa);
	}
	
	public void populateModuloList(Long idProyecto){
		if (idProyecto==null) {
			modulos=selectItemsHelper.emptySelectItemList();
		}
		modulos=selectItemsHelper.modulosByProyectoSelectItems(idProyecto);
	}
	
	public List<SelectItem> getProyectos() {
		return proyectos;
	}
	public void setProyectos(List<SelectItem> proyectos) {
		this.proyectos = proyectos;
	}
	public List<SelectItem> getModulos() {
		return modulos;
	}
	public void setModulos(List<SelectItem> modulos) {
		this.modulos = modulos;
	}
	public Long getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	public String getHashId() {
		return hashId;
	}
	public void setHashId(String hashId) {
		this.hashId = hashId;
	}
	public Long getIdProyecto() {
		return idProyecto;
	}
	public void setIdProyecto(Long idProyecto) {
		this.idProyecto = idProyecto;
	}
	
	

}
