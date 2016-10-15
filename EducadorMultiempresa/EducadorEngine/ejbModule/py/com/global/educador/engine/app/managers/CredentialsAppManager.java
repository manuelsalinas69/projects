package py.com.global.educador.engine.app.managers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import py.com.global.educador.engine.dto.CredentialsDto;
import py.com.global.educador.engine.enums.EstadoRegistro;
import py.com.global.educador.engine.enums.EstadoSesion;
import py.com.global.educador.engine.enums.TipoSuscripcion;
import py.com.global.educador.jpa.entity.Sessions;
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
			q.setParameter("pass", DigestUtils.md5Hex(pass));
			q.setParameter("estado", EstadoRegistro.ACTIVO.name());
			List<Usuario> l=q.getResultList();
			if (l.isEmpty()) {
				return null;
			}
			return subscriberFor(l.get(0));
			
		} catch (Exception e) {
			System.out.println("CredentialsAppManager.login(): "+e);
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean logout(String sid){
		if (sid==null) {
			return false;
		}
		Sessions s= entityManager.find(Sessions.class, sid);
		if (s==null) {
			return true;
		}
		entityManager.remove(s);
		return true;
	}
	
	
	private CredentialsDto subscriberFor(Usuario usuario) {
		Suscriptor s=getSuscriptor(usuario);
		if (s==null) {
			return createSubscriberFor(usuario);
		}
		return createCredentialsFor(s.getIdSuscriptor(), usuario.getContrasena(), usuario.getEmpresa().getIdEmpresa(),
				usuario.getEmpresa().getNombre(), usuario.getUsuario(), true);

	}


	private String createSessionTokenFor(long idSuscriptor, String pass) {
		Random generator = new Random();
		String nonceNew = String.valueOf(generator.nextInt(999999999));
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		Date d = new Date();
		String create=df.format(d);
		
		return createToken(nonceNew, create, pass);
	}


	private CredentialsDto createSubscriberFor(Usuario usuario) {
		Suscriptor s= new Suscriptor();
		s.setNumero(usuario.getUsuario());
		s.setFechaAlta(new Date());
		s.setTipoAlta(TipoSuscripcion.APP.name());
		entityManager.persist(s);
		return createCredentialsFor(s.getIdSuscriptor(), usuario.getContrasena(), usuario.getEmpresa().getIdEmpresa(),
				usuario.getEmpresa().getNombre(), usuario.getUsuario(), true);
	}
	
	private CredentialsDto createCredentialsFor(Long idSuscriptor,String pass, Long idEmpresa, String nombreEmpresa, String userName, boolean sessionRequired){
		CredentialsDto dto= new CredentialsDto();
		dto.setIdEmpresa(idEmpresa);
		dto.setNombreEmpresa(nombreEmpresa);
		dto.setIdSuscriptor(idSuscriptor);
		if (sessionRequired) {
			String token=createSessionTokenFor(idSuscriptor,pass);
			Date d= new Date();
			Sessions session= new Sessions(token, userName,idSuscriptor,d,d, idEmpresa, nombreEmpresa);
			dto.setSessionToken(token);
			entityManager.persist(session);
		}
		
		
		return dto;
	}
	
	private String createToken(String nonce, String created, String password) {
		String encoded = null;
		try {
			//String pass = hexEncode(nonce) + created + password;
			//System.out.println("CALCULTE PASS DIGEST- CREATED: "+created);
			String pass = nonce + created + password;//OK PARA PasswordDigest
			//String pass = nonce;
			MessageDigest md = MessageDigest.getInstance("SHA1");
			md.update(pass.getBytes());
			byte[] encodedPassword = md.digest();
			encoded = new String(Base64.encodeBase64(encodedPassword));
			//encoded = Base64.encodeBase64String("2540".getBytes());
		} catch (NoSuchAlgorithmException ex) {
			//Logger.getLogger(HeaderHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return encoded;
	}

	public Properties checkSessionId(String sessionId){
		Properties p= new Properties();
		
		Sessions s= entityManager.find(Sessions.class, sessionId);
		if (s==null || s.getLastUpdate()==null || isExpiredSessionId(s)) {
			p.put("status", EstadoSesion.EXPIRADO.name());
			return p;
		}
		p.put("status", EstadoSesion.ACTIVO.name());
		p.put("success", Boolean.TRUE);
		p.put("idSuscriptor", s.getIdSuscriptor());
		p.put("idEmpresa", s.getIdEmpresa());
		p.put("nombreEmpresa", s.getNombreEmpresa());
		p.put("sessionId", s.getIdSession());
		
		return p;
		
	}
	
	public boolean updateSessionId(String sessionId, Date lastUpdate){
		Sessions s=entityManager.find(Sessions.class, sessionId);
		if (s==null) {
			return false;
		}
		if (lastUpdate==null) {
			lastUpdate= new Date();
		}
		return updateSessionId(s, lastUpdate);
	}

	private boolean updateSessionId(Sessions s, Date lastUpdate) {
		s.setLastUpdate(lastUpdate);
		entityManager.merge(s);
		return true;
	}


	private boolean isExpiredSessionId(Sessions s) {
		if (s==null) {
			return true;
		}
		Date now= new Date();
		Date lastUpdate=s.getLastUpdate();
		long maxIdleTime=getMaxIdleTime();
//		try {
//			Thread.sleep(3000);
//		} catch (Exception e) {
//		}
		return (now.getTime()-lastUpdate.getTime())>=maxIdleTime;
	}
	private long getMaxIdleTime() {
		return (1000)*60*5;//5 minutos seg
	}


	public boolean isExpiredSessionId(String sid) {
		Sessions s= entityManager.find(Sessions.class, sid);
		if (s==null || s.getLastUpdate()==null || isExpiredSessionId(s)) return false;
		return isExpiredSessionId(s);
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
