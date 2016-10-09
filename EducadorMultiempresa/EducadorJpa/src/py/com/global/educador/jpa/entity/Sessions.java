package py.com.global.educador.jpa.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Entity implementation class for Entity: Sessions
 *
 */
@Entity
public class Sessions implements Serializable {

	   
	
	private String idSession;
	private String userName;
	private Long idSuscriptor;
	private Date creationDate;
	private Date lastUpdate;
	private Long idEmpresa;
	private String nombreEmpresa;
	private static final long serialVersionUID = 1L;

	public Sessions() {
		super();
	}
	
	

	


	






	public Sessions(String idSession, String userName, Long idSuscriptor, Date creationDate, Date lastUpdate,
			Long idEmpresa, String nombreEmpresa) {
		super();
		this.idSession = idSession;
		this.userName = userName;
		this.idSuscriptor = idSuscriptor;
		this.creationDate = creationDate;
		this.lastUpdate = lastUpdate;
		this.idEmpresa = idEmpresa;
		this.nombreEmpresa = nombreEmpresa;
	}













	@Id
	@Column(name="ID_SESSION")
	public String getIdSession() {
		return idSession;
	}

	public void setIdSession(String idSession) {
		this.idSession = idSession;
	}
	@Column(name="ID_SUSCRIPTOR")
	public Long getIdSuscriptor() {
		return idSuscriptor;
	}

	public void setIdSuscriptor(Long idSuscriptor) {
		this.idSuscriptor = idSuscriptor;
	}
	@Column(name="CREATION_DATE")
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	@Column(name="LAST_UPDATE")
	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	@Column(name="USERNAME")
	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column(name="ID_EMPRESA")
	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	@Column(name="NOMBRE_EMPRESA")
	public String getNombreEmpresa() {
		return nombreEmpresa;
	}

	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}   
	
   
}
