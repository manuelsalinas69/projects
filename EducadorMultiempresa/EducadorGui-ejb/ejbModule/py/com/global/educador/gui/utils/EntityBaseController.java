package py.com.global.educador.gui.utils;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;

import py.com.global.educador.gui.configuration.EducadorGui.Constants.ABMResults;
import py.com.global.educador.gui.dto.ValidationResult;
import py.com.global.educador.gui.enums.ABMActions;


public abstract class EntityBaseController<E> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean hideModalPanel;
	boolean edit;
	Serializable id;
	E entitySelected;
	Class<E> c;
	
	Logger  logger;
	
	public EntityBaseController(Class<E> c) {
		this.c=c;
		logger=Logger.getLogger(c);
	}
		
	public void view(Serializable id){
		select(id);
		edit=false;
	}
	
	public void edit(Serializable id){
		select(id);
		edit=true;
	}
	
	public String remove(Serializable id){
		select(id);
		return remove();		
	}
	
	public void select(Serializable id){
		this.id=id;
		hideModalPanel=false;
		if (id==null) {
			entitySelected=null;
			return;
		}
		entitySelected=getEntityManager().find(c, id);
		
	}
	
	public E getInstance(){
		return entitySelected;
	}
	
	public void setInstance(E instance){
		this.entitySelected=instance;
	}
	
	public void createNew(){
		entitySelected=newInstance();
		id=null;
		edit=true;
		hideModalPanel=false;
	}
	

	
	public E newInstance(){
		try {
			return c.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String update(){
		try {
			this.hideModalPanel=false;
			ValidationResult result=checkData(ABMActions.UPDATE);
			if (!result.isSuccessful()) {
				getStatusMessages().addFromResourceBundle(Severity.ERROR, result.getMessage(),(Object[])result.getParameters());
				return null;
			}
			setCommonData(entitySelected);
			getEntityManager().merge(entitySelected);
			setDetailsData();
			getEntityManager().flush();
			getStatusMessages().add(Severity.INFO,"#{messages.msg_update_successful}");
			this.hideModalPanel=true;
			return ABMResults.UPDATED;
		} 

		catch (Exception e) {
			getStatusMessages().add(Severity.ERROR,"#{messages.msg_unexpected_error}");
			e.printStackTrace();
		}
		return null;
		
	}
	
	public abstract void setDetailsData();

	public String persist(){
		try {
			this.hideModalPanel=false;
			ValidationResult result=checkData(ABMActions.PERSIST);
			if (!result.isSuccessful()) {
				getStatusMessages().addFromResourceBundle(Severity.ERROR, result.getMessage(),(Object[])result.getParameters());
				return null;
			}
			setCommonData(entitySelected);
			getEntityManager().persist(entitySelected);
			setDetailsData();
			getEntityManager().flush();
			getStatusMessages().add(Severity.INFO,"#{messages.msg_persist_successful}");
			this.hideModalPanel=true;
			return ABMResults.PERSISTED;
		} 

		catch (Exception e) {
			getStatusMessages().add(Severity.ERROR,"#{messages.msg_unexpected_error}");
			e.printStackTrace();
		}
		return null;
	}
	
	public String remove(){
		try {
			this.hideModalPanel=false;
			ValidationResult result=checkData(ABMActions.REMOVE);
			if (!result.isSuccessful()) {
				getStatusMessages().addFromResourceBundle(Severity.ERROR, result.getMessage(),(Object[])result.getParameters());
				return null;
			}
			entitySelected=getEntityManager().find(c, id);
			getEntityManager().remove(entitySelected);
			getEntityManager().flush();
			getStatusMessages().add(Severity.INFO,"#{messages.msg_remove_successful}");
			this.hideModalPanel=true;
			return ABMResults.REMOVED;
		} catch (Exception e) {
			getStatusMessages().add(Severity.ERROR,"#{messages.msg_unexpected_error}");
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean isNew(){
		return id==null;
	}
	
	public boolean isManaged(){
		return !isNew();
	}
	
	public abstract ValidationResult checkData(ABMActions action);
	public abstract EntityManager getEntityManager();
	public abstract StatusMessages getStatusMessages();
	public abstract void setCommonData(E instance);
	public abstract boolean canEdit();
	public abstract boolean canRemove();
	
	
	
	/**
	 * GETTERS && SETTERS
	 * */
	
	
	public boolean isHideModalPanel() {
		return hideModalPanel;
	}
	public void setHideModalPanel(boolean hideModalPanel) {
		this.hideModalPanel = hideModalPanel;
	}
	public E getEntitySelected() {
		return entitySelected;
	}
	public void setEntitySelected(E entitySelected) {
		this.entitySelected = entitySelected;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public Serializable getId() {
		return id;
	}

	public void setId(Serializable id) {
		this.id = id;
	}
	
	
	
	
	
}
