package py.com.global.educador.jpa.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the SUSCRIPTOR_PROYECTO database table.
 * 
 */
@Entity
@Table(name="SUSCRIPTOR_PROYECTO")
public class SuscriptorProyecto implements Serializable {
	private static final long serialVersionUID = 1L;
	private SuscriptorProyectoPK id;
	private String estadoSuscriptorProyecto;
	private String estadoSuscriptorModulo;
	private Proyecto proyecto;
	private Suscriptor suscriptor;
	private Modulo modulo;

	public SuscriptorProyecto() {
	}


	@EmbeddedId
	public SuscriptorProyectoPK getId() {
		return this.id;
	}

	public void setId(SuscriptorProyectoPK id) {
		this.id = id;
	}


	@Column(name="ESTADO_SUSCRIPTOR_PROYECTO")
	public String getEstadoSuscriptorProyecto() {
		return this.estadoSuscriptorProyecto;
	}

	public void setEstadoSuscriptorProyecto(String estadoSuscriptorProyecto) {
		this.estadoSuscriptorProyecto = estadoSuscriptorProyecto;
	}

	
	@Column(name="ESTADO_SUSCRIPTOR_MODULO")
	public String getEstadoSuscriptorModulo() {
		return this.estadoSuscriptorModulo;
	}

	public void setEstadoSuscriptorModulo(String estadoSuscriptorModulo) {
		this.estadoSuscriptorModulo = estadoSuscriptorModulo;
	}

	//bi-directional many-to-one association to Proyecto
	@ManyToOne
	@JoinColumn(name="ID_PROYECTO", updatable=false, insertable=false)
	public Proyecto getProyecto() {
		return this.proyecto;
	}

	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
	}

	
	@ManyToOne
	@JoinColumn(name="ID_MODULO_ACTUAL")
	public Modulo getModulo() {
		return modulo;
	}


	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}


	//bi-directional many-to-one association to Suscriptor
	@ManyToOne
	@JoinColumn(name="ID_SUSCRIPTOR", updatable=false, insertable=false)
	public Suscriptor getSuscriptor() {
		return this.suscriptor;
	}

	public void setSuscriptor(Suscriptor suscriptor) {
		this.suscriptor = suscriptor;
	}

	
}