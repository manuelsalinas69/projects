package py.com.global.educador.gui.utils;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import py.com.global.educador.gui.enums.EstadoRegistro;
import py.com.global.educador.gui.session.EmpresaList;
import py.com.global.educador.gui.session.ModuloList;
import py.com.global.educador.gui.session.ProyectoList;

@Name("selectItemsHelper")
@Scope(ScopeType.PAGE)
public class SelectItemsHelper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*Cantidad de dias por mes MONTH[0]=ENER0 */
	int[] MONTH={31,28,31,30,31,30,31,31,30,31,30,31};

	@In(create=true)
	EnumAppHelper enumAppHelper;

	static Logger loggerS=Logger.getLogger(SelectItemsHelper.class);

	@In(create=true)
	EntityManager entityManager;

	
	

	@Factory(value="daysOfMonthSelectItem",scope=ScopeType.APPLICATION)
	public List<SelectItem> daysOfMonthSelectItem(){
		List<SelectItem> l= new ArrayList<SelectItem>();
		for (int i = 1; i <= 30; i++) {
			l.add(new SelectItem(new Integer(i), i+""));

		}
		return l;
	}
	
	
	
	@Factory(value="minuteSelectItems", scope=ScopeType.APPLICATION)
	public List<SelectItem> minuteSelectItems(){
		List<SelectItem> l=null;
		if (l==null) {
			l= createArrayOfValues(0, 59);
		}
		return l;


	}

	@Factory(value="hourSelectItems", scope=ScopeType.APPLICATION)
	public List<SelectItem> hourSelectItems(){
		List<SelectItem> l=null;
		if (l==null) {
			l= createArrayOfValues(0, 23);
		}
		return l;


	}

	@Factory(value="dayOfMonthSelectItems", scope=ScopeType.APPLICATION)
	public List<SelectItem> dayOfMonthSelectItems(){
		List<SelectItem> l=null;
		if (l==null) {
			l= createArrayOfValues(1, 31);
		}
		return l;
	}

	@Factory(value="booleanSelectItems", scope=ScopeType.APPLICATION)
	public List<SelectItem> booleanSelectItems(){
		List<SelectItem> l=new ArrayList<SelectItem>();
		l.add(new SelectItem(null, "Seleccione..."));
		l.add(new SelectItem(Boolean.TRUE, "SI"));
		l.add(new SelectItem(Boolean.FALSE, "NO"));
		return l;


	}
	
	/*
	 * METODOS para logica de negocio
	 * **/
	@Factory(value="modulosSelectItems",scope=ScopeType.PAGE)
	public List<SelectItem> modulosSelectItems(){
		ModuloList list= (ModuloList) Component.getInstance(ModuloList.class);
		list.setMaxResults(null);
		list.setOrderColumn("idModulo");
		list.setOrderDirection("desc");
		return buildSelectItemListManyFieldsLabel(list.getResultList(), "<nombre>", true, false);
	}
	
	@Factory(value="proyectoSelectItems",scope=ScopeType.PAGE)
	public List<SelectItem> proyectoSelectItems(){
		ProyectoList list= (ProyectoList) Component.getInstance(ProyectoList.class);
		list.setMaxResults(null);
		list.setOrderColumn("nombre");
		return buildSelectItemListManyFieldsLabel(list.getResultList(), "<nombre>", true, false);
	}
	
	@Factory(value="empresaSelectItems",scope=ScopeType.PAGE)
	public List<SelectItem> empresaSelectItems(){
		EmpresaList list= (EmpresaList) Component.getInstance(EmpresaList.class);
		list.setMaxResults(null);
		list.setOrderColumn("nombre");
		return buildSelectItemListManyFieldsLabel(list.getResultList(), "<nombre>", true, false);
	}
	
	@Factory(value="empresaHashSelectItems",scope=ScopeType.PAGE)
	public List<SelectItem> empresaHashSelectItems(){
		EmpresaList list= (EmpresaList) Component.getInstance(EmpresaList.class);
		list.setMaxResults(null);
		list.setOrderColumn("nombre");
		return buildSelectItemListManyFieldsLabel(list.getResultList(),"hashId", "<nombre>", true, false);
	}
	
	
	
	
	/*
	 * METODOS PARA MULTIEMPRESA
	 * **/
	
	public List<SelectItem> modulosByProyectoSelectItems(Long idProyecto){
		if (idProyecto==null) {
			return emptySelectItemList();
		}
		ModuloList list= (ModuloList) Component.getInstance(ModuloList.class);
		list.setMaxResults(null);
		list.setIdProyecto(idProyecto);
		list.setOrderColumn("idModulo");
		list.setOrderDirection("desc");
		return buildSelectItemListManyFieldsLabel(list.getResultList(), "<nombre>", true, false);
	}
	
	public List<SelectItem> proyectoByEmpresaSelectItems(Long idEmpresa){
		if (idEmpresa==null) {
			return emptySelectItemList();
		}
		ProyectoList list= (ProyectoList) Component.getInstance(ProyectoList.class);
		list.setMaxResults(null);
		list.setOrderColumn("nombre");
		list.setIdEmpresa(idEmpresa);
		return buildSelectItemListManyFieldsLabel(list.getResultList(), "<nombre>", true, false);
	}
	
	public List<SelectItem> proyectoByEmpresaSelectItems(String hashId){
		if (hashId==null || hashId.trim().isEmpty()) {
			return emptySelectItemList();
		}
		ProyectoList list= (ProyectoList) Component.getInstance(ProyectoList.class);
		list.setMaxResults(null);
		list.setOrderColumn("nombre");
		list.setIx(hashId);
		return buildSelectItemListManyFieldsLabel(list.getResultList(), "<nombre>", true, false);
	}
	
	
	
	@Factory(value="estadosRegistroSelectItems",scope=ScopeType.APPLICATION)
	public List<SelectItem> estadosRegistroSelectItems(){
		List<SelectItem>l= new ArrayList<SelectItem>();
		l.add(new SelectItem(null, "Seleccione..."));
		l.add(new SelectItem(EstadoRegistro.ACTIVO.name(), EstadoRegistro.ACTIVO.toString()));
		l.add(new SelectItem(EstadoRegistro.INACTIVO.name(), EstadoRegistro.INACTIVO.toString()));
		return l;
	}
	
	public List<SelectItem> emptySelectItemList(){
		List<SelectItem>l= new ArrayList<SelectItem>();
		l.add(new SelectItem(null, "Seleccione..."));
		return l;
	}
	

	/**
	 * METODOS DE CREACION
	 **/
	
	
	private List<SelectItem> createArrayOfValues(Integer start, Integer end){
		int size=end-start+1;
		List<SelectItem> l= new ArrayList<SelectItem>();
		for (int i = 0; i < size; i++) {
			l.add(new SelectItem(i));
		}
		return l;
	}

	public static List<SelectItem> buildSelectItemList(
			List<? extends EntityInterface> resultList, String displayName,
			boolean includeSelectOption, boolean doOrder) {
		List<SelectItem> l = new ArrayList<SelectItem>();
		try {
			if (includeSelectOption) {
				l.add(new SelectItem(null, "Seleccione..."));
			}

			for (EntityInterface entityInterface : resultList) {
				Object value = getObjetValue(entityInterface, displayName);
				l.add(new SelectItem(entityInterface.getId(), value.toString()));
				;
				;
			}
			if (doOrder) {
				Collections.sort(l, new SelectItemsComparator(true));
			}

			return l;
		} catch (Exception e) {
			loggerS.debug("GeneralHelper.buildSelectItemList(): " + e);
		}
		return l;

	}

	public static List<String> getLabels(String input){
		List<String> l= new ArrayList<String>();
		while (input.contains("<") || input.contains(">")) {
			String value= input.substring(input.indexOf("<")+1,input.indexOf(">"));
			input=input.substring(input.indexOf(">")+1);
			l.add(value);

		}
		return l;
	}

	public static List<SelectItem> buildSelectItemListManyFieldsLabel(
			List<? extends EntityInterface> resultList, String displayName,
			boolean includeSelectOption, boolean doOrder) {
		List<SelectItem> l = new ArrayList<SelectItem>();

		try {
			if (includeSelectOption) {
				l.add(new SelectItem(null, "Seleccione..."));
			}

			List<String> lLabels=getLabels(displayName);

			for (EntityInterface entityInterface : resultList) {
				String toDisplay=displayName;
				for (String label : lLabels) {
					Object value = getObjetValue(entityInterface, label);
					toDisplay=toDisplay.replace("<"+label+">", value.toString());
				}

				l.add(new SelectItem(entityInterface.getId(), toDisplay));

			}
			if (doOrder) {
				Collections.sort(l, new SelectItemsComparator(true));
			}

			return l;
		} catch (Exception e) {
			loggerS.debug("GeneralHelper.buildSelectItemList(): " + e);
		}
		return l;

	}

	public static List<SelectItem> buildSelectItemListManyFieldsLabel(
			List<? extends EntityInterface> resultList,String itemValuePath, String displayName,
			boolean includeSelectOption, boolean doOrder) {
		List<SelectItem> l = new ArrayList<SelectItem>();

		try {
			if (includeSelectOption) {
				l.add(new SelectItem(null, "Seleccione..."));
			}

			List<String> lLabels=getLabels(displayName);

			for (EntityInterface entityInterface : resultList) {
				String toDisplay=displayName;
				for (String label : lLabels) {
					Object value = getObjetValue(entityInterface, label);
					toDisplay=toDisplay.replace("<"+label+">", value.toString());
				}

				l.add(new SelectItem(getObjetValue(entityInterface, itemValuePath), toDisplay));

			}
			if (doOrder) {
				Collections.sort(l, new SelectItemsComparator(true));
			}

			return l;
		} catch (Exception e) {
			loggerS.debug("GeneralHelper.buildSelectItemList(): " + e);
		}
		return l;

	}

	public static List<SelectItem> buildSelectItemList(List<? extends EntityInterface> resultList, String displayName,
			boolean includeSelectOption, boolean doOrder, String nullLabelValue) {
		List<SelectItem> l = new ArrayList<SelectItem>();
		try {
			if (includeSelectOption) {
				l.add(new SelectItem(null, nullLabelValue));
			}

			for (EntityInterface entityInterface : resultList) {
				Object value = getObjetValue(entityInterface, displayName);
				l.add(new SelectItem(entityInterface.getId(), value.toString()));
				;
			}
			if (doOrder) {
				Collections.sort(l, new SelectItemsComparator(true));
			}

			return l;
		} catch (Exception e) {
			loggerS.debug("GeneralHelper.buildSelectItemList(): " + e);
		}
		return l;

	}

	public static Object getObjetValue(EntityInterface entityInterface,
			String valuePath) {
		String values[] = valuePath.split("\\.");
		if (values.length == 1) {
			return entityInterface.getProperties().get(valuePath);
		}
		String lastProperty = values[0];

		Object objValue = entityInterface.getProperties().get(lastProperty);
		if (objValue instanceof EntityInterface) {
			return getObjetValue((EntityInterface) objValue,
					valuePath.substring(valuePath.lastIndexOf(".") + 1));
		}
		if (objValue instanceof EntityInterfaceId) {
			return ((EntityInterfaceId) objValue).getProperties().get(valuePath.substring(valuePath.lastIndexOf(".") + 1));
		}
		if (objValue != null) {
			return objValue.toString();
		}
		return null;
	}



	public static void main(String[] args) {
		List<String> l= new ArrayList<String>();
		String input="<element.attr> - <element.attr2>";
		while (input.contains("<") || input.contains(">")) {
			String value= input.substring(input.indexOf("<")+1,input.indexOf(">"));
			input=input.substring(input.indexOf(">")+1);
			l.add(value);

		}
		for (String values : l) {
			loggerS.debug("values--> "+values);
		}

	}



}
