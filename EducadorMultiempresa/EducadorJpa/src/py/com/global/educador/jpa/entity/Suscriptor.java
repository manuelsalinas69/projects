package py.com.global.educador.jpa.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;


/**
 * The persistent class for the SUSCRIPTOR database table.
 * 
 */
@Entity
public class Suscriptor implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idSuscriptor;
	private Date fechaAlta;
	private String numero;
	private String tipoAlta;
	private Set<EjecucionSuscriptor> ejecucionSuscriptors;
	private Set<SessionTrx> sessionTrxs;
	private Set<SuscriptorProyecto> suscriptorProyectos;

	public Suscriptor() {
	}


	@Id
	@SequenceGenerator(name="SUSCRIPTOR_IDSUSCRIPTOR_GENERATOR", sequenceName="SUSCRIPTOR_ID_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SUSCRIPTOR_IDSUSCRIPTOR_GENERATOR")
	@Column(name="ID_SUSCRIPTOR")
	public long getIdSuscriptor() {
		return this.idSuscriptor;
	}

	public void setIdSuscriptor(long idSuscriptor) {
		this.idSuscriptor = idSuscriptor;
	}


	 
	@Column(name="FECHA_ALTA")
	public Date getFechaAlta() {
		return this.fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}


	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}


	@Column(name="TIPO_ALTA")
	public String getTipoAlta() {
		return this.tipoAlta;
	}

	public void setTipoAlta(String tipoAlta) {
		this.tipoAlta = tipoAlta;
	}


	//bi-directional many-to-one association to EjecucionSuscriptor
	@OneToMany(mappedBy="suscriptor")
	public Set<EjecucionSuscriptor> getEjecucionSuscriptors() {
		return this.ejecucionSuscriptors;
	}

	public void setEjecucionSuscriptors(Set<EjecucionSuscriptor> ejecucionSuscriptors) {
		this.ejecucionSuscriptors = ejecucionSuscriptors;
	}

	
	public EjecucionSuscriptor addEjecucionSuscriptors(EjecucionSuscriptor ejecucionSuscriptors) {
		getEjecucionSuscriptors().add(ejecucionSuscriptors);
		ejecucionSuscriptors.setSuscriptor(this);

		return ejecucionSuscriptors;
	}

	public EjecucionSuscriptor removeEjecucionSuscriptors(EjecucionSuscriptor ejecucionSuscriptors) {
		getEjecucionSuscriptors().remove(ejecucionSuscriptors);
		ejecucionSuscriptors.setSuscriptor(null);

		return ejecucionSuscriptors;
	}

	
	
	

	//bi-directional many-to-one association to SessionTrx
	@OneToMany(mappedBy="suscriptor")
	public Set<SessionTrx> getSessionTrxs() {
		return this.sessionTrxs;
	}

	public void setSessionTrxs(Set<SessionTrx> sessionTrxs) {
		this.sessionTrxs = sessionTrxs;
	}

	
	public SessionTrx addSessionTrxs(SessionTrx sessionTrxs) {
		getSessionTrxs().add(sessionTrxs);
		sessionTrxs.setSuscriptor(this);

		return sessionTrxs;
	}

	public SessionTrx removeSessionTrxs(SessionTrx sessionTrxs) {
		getSessionTrxs().remove(sessionTrxs);
		sessionTrxs.setSuscriptor(null);

		return sessionTrxs;
	}

	//bi-directional many-to-one association to SuscriptorProyecto
	@OneToMany(mappedBy="suscriptor")
	public Set<SuscriptorProyecto> getSuscriptorProyectos() {
		return this.suscriptorProyectos;
	}

	public void setSuscriptorProyectos(Set<SuscriptorProyecto> suscriptorProyectos) {
		this.suscriptorProyectos = suscriptorProyectos;
	}

	
	public SuscriptorProyecto addSuscriptorProyectos(SuscriptorProyecto suscriptorProyectos) {
		getSuscriptorProyectos().add(suscriptorProyectos);
		suscriptorProyectos.setSuscriptor(this);

		return suscriptorProyectos;
	}

	public SuscriptorProyecto removeSuscriptorProyectos(SuscriptorProyecto suscriptorProyectos) {
		getSuscriptorProyectos().remove(suscriptorProyectos);
		suscriptorProyectos.setSuscriptor(null);

		return suscriptorProyectos;
	}
}