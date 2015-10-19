package py.com.global.educador.engine.app.managers;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.codec.digest.DigestUtils;

import py.com.global.educador.engine.dto.CredentialsDto;
import py.com.global.educador.engine.enums.EstadoRegistro;
import py.com.global.educador.engine.enums.TipoSuscripcion;
import py.com.global.educador.jpa.entity.Suscriptor;
import py.com.global.educador.jpa.entity.Usuario;

@Stateless
public class CredentialsAppManager {

	@PersistenceContext(unitName="EducadorJpa")
	EntityManager entityManager;
	
	/**
	 * @return id de suscriptor en caso de login exitoso; null de otra forma
	 * */
	@SuppressWarnings("unchecked")
	public CredentialsDto login(String user, String pass){
		try {
			String hql="SELECT u FROM Usuario u WHERE lower(trim(u.usuario))= lower(trim(:user)) and u.contrasena=:pass AND u.estado=:estado";
			Query q= entityManager.createQuery(hql);
			q.setParameter("user", user);
			q.setParameter("pass", DigestUtils.md5(pass));
			q.setParameter("estado", EstadoRegistro.ACTIVO.name());
			List<Usuario> l=q.getResultList();
			if (l.isEmpty()) {
				return null;
			}
			return subscriberFor(l.get(0));
			
		} catch (Exception e) {
			System.out.println("CredentialsAppManager.login(): "+e);
		}
		
		return null;
	}
	
	
	private CredentialsDto subscriberFor(Usuario usuario) {
		Suscriptor s=getSuscriptor(usuario);
		if (s==null) {
			return createSubscriberFor(usuario);
		}
		CredentialsDto dto= new CredentialsDto();
		dto.setIdEmpresa(usuario.getEmpresa().getIdEmpresa());
		dto.setNombreEmpresa(usuario.getEmpresa().getNombre());
		dto.setIdSuscriptor(s.getIdSuscriptor());
		return dto;
	}


	private CredentialsDto createSubscriberFor(Usuario usuario) {
		Suscriptor s= new Suscriptor();
		s.setNumero(usuario.getUsuario());
		s.setFechaAlta(new Date());
		s.setTipoAlta(TipoSuscripcion.APP.name());
		entityManager.persist(s);
		CredentialsDto dto= new CredentialsDto();
		dto.setIdEmpresa(usuario.getEmpresa().getIdEmpresa());
		dto.setNombreEmpresa(usuario.getEmpresa().getNombre());
		dto.setIdSuscriptor(s.getIdSuscriptor());
		return dto;
	}


	@SuppressWarnings("unchecked")
	private Suscriptor getSuscriptor(Usuario usuario) {
		String hql="SELECT s FROM Suscriptor s WHERE lower(trim(s.numero))=lower(trim(:codSuscriptor)) ";
		Query q=entityManager.createQuery(hql);
		q.setParameter("codSuscriptor", usuario.getUsuario());
		List<Suscriptor> l=q.getResultList();
		if (l.isEmpty()) {
			return null;
		}
		return l.get(0);
	}


	public static void main(String[] args) {
		String pass="admin";
		System.out.println("Pass: "+pass+"; Hash: "+DigestUtils.md5Hex(pass));
	}
}
