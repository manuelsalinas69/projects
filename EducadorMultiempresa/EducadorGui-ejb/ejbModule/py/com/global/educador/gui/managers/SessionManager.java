package py.com.global.educador.gui.managers;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import py.com.global.educador.gui.configuration.EducadorGui.Constants.SystemParameterKey;
import py.com.global.educador.gui.entity.Usuario;
import py.com.global.educador.gui.utils.SystemParametersHelper;

@Name("sessionManager")
public class SessionManager {

	@In(create=true)
	Usuario usuario;
	@In(create=true)
	SystemParametersHelper systemParametersHelper;
	
	
	public Long getLoggedUserCompany(){
		return usuario.getEmpresa()==null?null:usuario.getEmpresa().getIdEmpresa();
	}
	
	public Long getSuperCompanyId(){
		try {
			return Long.parseLong(systemParametersHelper.getParameterValue(SystemParameterKey.SYSTEM_GUI_SUPERCOMPANY_ID));
		} catch (Exception e) {
			System.out.println("SessionManager.getSuperCompanyId(): "+e);
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean userFromSuperCompany(){
		try {
			return getLoggedUserCompany().equals(getSuperCompanyId());
		} catch (Exception e) {
			System.out.println("SessionManager.userFromSuperCompany(): "+e);
			e.printStackTrace();
		}
		
		return false;
	}
	
	
}
