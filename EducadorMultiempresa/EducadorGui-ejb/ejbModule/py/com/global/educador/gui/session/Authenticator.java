package py.com.global.educador.gui.session;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;

import py.com.global.educador.gui.entity.Usuario;
import py.com.global.educador.gui.enums.EstadoRegistro;
import py.com.global.educador.gui.utils.GeneralHelper;

@Name("authenticator")
public class Authenticator
{
    @Logger private Log log;

    @In Identity identity;
    @In Credentials credentials;
    
    @In EntityManager entityManager;

    public boolean authenticate()
    {
        log.info("authenticating {0}", credentials.getUsername());
        //write your authentication logic here,
        try {
			return loginDB(credentials.getUsername(), credentials.getPassword(), true);
		} catch (Exception e) {
			log.error("authenticate",e);
		}
        return false;
    }
    
    public boolean loginDB(String user, String pass, boolean requirePass){
    	Usuario u=getUsuario(user, pass, requirePass);
    	return u!=null;
    }
    
    public Usuario getUsuario(String user, String pass, boolean requirePass){
    	String hql="SELECT u FROM Usuario u WHERE u.usuario=:nombre AND u.estado= :estado";
    	
    	if (requirePass) {
			hql+=" AND u.contrasena=:pass";
		}
    	
    	Query q=entityManager.createQuery(hql);
    	q.setParameter("nombre", user);
    	q.setParameter("estado", EstadoRegistro.ACTIVO.name());
    	if (requirePass) {
			q.setParameter("pass", GeneralHelper.MD5(pass));
		}
    	
    	try {
			Usuario u=(Usuario) q.getSingleResult();
			return u;
		} catch (Exception e) {
			System.out.println("Authenticator.getUsuario(): "+e);
		}
    	
    	return null;
    	
    }
    
    @Factory(value="usuario",scope=ScopeType.SESSION,autoCreate=true)
    public Usuario getLoggedUsuario(){
    	return getUsuario(credentials.getUsername(), credentials.getPassword(), false);
    }

}
