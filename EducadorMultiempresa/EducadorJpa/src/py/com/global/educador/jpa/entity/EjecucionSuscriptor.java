package py.com.global.educador.jpa.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;


/**
 * The persistent class for the EJECUCION_SUSCRIPTOR database table.
 * 
 */
@Entity
@Table(name="EJECUCION_SUSCRIPTOR")
public class EjecucionSuscriptor implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idEjecucionSuscriptor;
	private String estadoEjecucion;
	private Date fechaAlta;
	private Modulo modulo;
	private Suscriptor suscriptor;
	private String motivoCancelacion;
	private Set<EjecucionSuscriptorDetalle> ejecucionSuscriptorDetalles;

	public EjecucionSuscriptor() {
	}


	@Id
	@SequenceGenerator(name="EJECUCION_SUSCRIPTOR_IDEJECUCIONSUSCRIPTOR_GENERATOR", sequenceName="EJECUCION_SUSCRIPTOR_ID_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="EJECUCION_SUSCRIPTOR_IDEJECUCIONSUSCRIPTOR_GENERATOR")
	@Column(name="ID_EJECUCION_SUSCRIPTOR")
	public long getIdEjecucionSuscriptor() {
		return this.idEjecucionSuscriptor;
	}

	public void setIdEjecucionSuscriptor(long idEjecucionSuscriptor) {
		this.idEjecucionSuscriptor = idEjecucionSuscriptor;
	}


	@Column(name="ESTADO_EJECUCION")
	public String getEstadoEjecucion() {
		return this.estadoEjecucion;
	}

	public void setEstadoEjecucion(String estadoEjecucion) {
		this.estadoEjecucion = estadoEjecucion;
	}


	 
	@Column(name="FECHA_ALTA")
	public Date getFechaAlta() {
		return this.fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}


	//bi-directional many-to-one association to Modulo
	@ManyToOne
	@JoinColumn(name="ID_MODULO")
	public Modulo getModulo() {
		return this.modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

	

	//bi-directional many-to-one association to Suscriptor
	@ManyToOne
	@JoinColumn(name="ID_SUSCRIPTOR")
	public Suscriptor getSuscriptor() {
		return this.suscriptor;
	}

	public void setSuscriptor(Suscriptor suscriptor) {
		this.suscriptor = suscriptor;
	}

	
	@Column(name="MOTIVO_CANCELACION")
	public String getMotivoCancelacion() {
		return motivoCancelacion;
	}


	public void setMotivoCancelacion(String motivoCancelacion) {
		this.motivoCancelacion = motivoCancelacion;
	}


	//bi-directional many-to-one association to EjecucionSuscriptorDetalle
	@OneToMany(mappedBy="ejecucionSuscriptor")
	public Set<EjecucionSuscriptorDetalle> getEjecucionSuscriptorDetalles() {
		return this.ejecucionSuscriptorDetalles;
	}

	public void setEjecucionSuscriptorDetalles(Set<EjecucionSuscriptorDetalle> ejecucionSuscriptorDetalles) {
		this.ejecucionSuscriptorDetalles = ejecucionSuscriptorDetalles;
	}

	
	public EjecucionSuscriptorDetalle addEjecucionSuscriptorDetalles(EjecucionSuscriptorDetalle ejecucionSuscriptorDetalles) {
		getEjecucionSuscriptorDetalles().add(ejecucionSuscriptorDetalles);
		ejecucionSuscriptorDetalles.setEjecucionSuscriptor(this);

		return ejecucionSuscriptorDetalles;
	}

	public EjecucionSuscriptorDetalle removeEjecucionSuscriptorDetalles(EjecucionSuscriptorDetalle ejecucionSuscriptorDetalles) {
		getEjecucionSuscriptorDetalles().remove(ejecucionSuscriptorDetalles);
		ejecucionSuscriptorDetalles.setEjecucionSuscriptor(null);

		return ejecucionSuscriptorDetalles;
	}
}