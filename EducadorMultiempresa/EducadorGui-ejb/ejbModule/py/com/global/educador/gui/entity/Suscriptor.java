package py.com.global.educador.gui.entity;

// Generated Aug 21, 2014 6:09:06 PM by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import py.com.global.educador.gui.utils.EntityInterface;

/**
 * Suscriptor generated by hbm2java
 */
@Entity
@Table(name = "SUSCRIPTOR" )
public class Suscriptor extends EntityInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long idSuscriptor;
	private String numero;
	private Date fechaAlta;
	private String tipoAlta;
	private Set<EjecucionSuscriptor> ejecucionSuscriptors = new HashSet<EjecucionSuscriptor>(
			0);
	private Set<SessionTrx> sessionTrxes = new HashSet<SessionTrx>(0);
	private Set<EvaluacionSuscriptor> evaluacionSuscriptors = new HashSet<EvaluacionSuscriptor>(
			0);
	private Set<SuscriptorProyecto> suscriptorProyectos = new HashSet<SuscriptorProyecto>(
			0);
	private Set<LogClientResponse> logClientResponses = new HashSet<LogClientResponse>(
			0);

	public Suscriptor() {
	}

	public Suscriptor(Long idSuscriptor) {
		this.idSuscriptor = idSuscriptor;
	}

	public Suscriptor(Long idSuscriptor, String numero, Date fechaAlta,
			String tipoAlta, Set<EjecucionSuscriptor> ejecucionSuscriptors,
			Set<SessionTrx> sessionTrxes,
			Set<EvaluacionSuscriptor> evaluacionSuscriptors,
			Set<SuscriptorProyecto> suscriptorProyectos,
			Set<LogClientResponse> logClientResponses) {
		this.idSuscriptor = idSuscriptor;
		this.numero = numero;
		this.fechaAlta = fechaAlta;
		this.tipoAlta = tipoAlta;
		this.ejecucionSuscriptors = ejecucionSuscriptors;
		this.sessionTrxes = sessionTrxes;
		this.evaluacionSuscriptors = evaluacionSuscriptors;
		this.suscriptorProyectos = suscriptorProyectos;
		this.logClientResponses = logClientResponses;
	}

	@Id
	@Column(name = "ID_SUSCRIPTOR", unique = true, nullable = false, precision = 22, scale = 0)
	@NotNull
	public Long getIdSuscriptor() {
		return this.idSuscriptor;
	}

	public void setIdSuscriptor(Long idSuscriptor) {
		this.idSuscriptor = idSuscriptor;
	}

	@Column(name = "NUMERO", length = 30)
	@Size(max = 30)
	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	 
	@Column(name = "FECHA_ALTA", length = 7)
	public Date getFechaAlta() {
		return this.fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	@Column(name = "TIPO_ALTA", length = 30)
	@Size(max = 30)
	public String getTipoAlta() {
		return this.tipoAlta;
	}

	public void setTipoAlta(String tipoAlta) {
		this.tipoAlta = tipoAlta;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "suscriptor")
	public Set<EjecucionSuscriptor> getEjecucionSuscriptors() {
		return this.ejecucionSuscriptors;
	}

	public void setEjecucionSuscriptors(
			Set<EjecucionSuscriptor> ejecucionSuscriptors) {
		this.ejecucionSuscriptors = ejecucionSuscriptors;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "suscriptor")
	public Set<SessionTrx> getSessionTrxes() {
		return this.sessionTrxes;
	}

	public void setSessionTrxes(Set<SessionTrx> sessionTrxes) {
		this.sessionTrxes = sessionTrxes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "suscriptor")
	public Set<EvaluacionSuscriptor> getEvaluacionSuscriptors() {
		return this.evaluacionSuscriptors;
	}

	public void setEvaluacionSuscriptors(
			Set<EvaluacionSuscriptor> evaluacionSuscriptors) {
		this.evaluacionSuscriptors = evaluacionSuscriptors;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "suscriptor")
	public Set<SuscriptorProyecto> getSuscriptorProyectos() {
		return this.suscriptorProyectos;
	}

	public void setSuscriptorProyectos(
			Set<SuscriptorProyecto> suscriptorProyectos) {
		this.suscriptorProyectos = suscriptorProyectos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "suscriptor")
	public Set<LogClientResponse> getLogClientResponses() {
		return this.logClientResponses;
	}

	public void setLogClientResponses(Set<LogClientResponse> logClientResponses) {
		this.logClientResponses = logClientResponses;
	}

	@Override
	@Transient
	public Serializable getId() {
		return getIdSuscriptor();
	}

}
