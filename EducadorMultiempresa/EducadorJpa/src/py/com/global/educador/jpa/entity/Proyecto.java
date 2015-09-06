package py.com.global.educador.jpa.entity;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.Set;


/**
 * The persistent class for the PROYECTO database table.
 * 
 */
@Entity
public class Proyecto implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idProyecto;
	private String descripcion;
	private String estadoProyecto;
	private String estadoRegistro;
	private Date fechaAlta;
	private Date fechaFin;
	private Date fechaInicio;
	private Date fechaModificacion;
	private String nombre;
	private String numeroCorto;
	private String objetivos;
	private String usuarioAlta;
	private String usuarioModificacion;
	private Set<Modulo> modulos;
	private Promo promo;
	private Empresa empresa;
	private Set<SuscriptorProyecto> suscriptorProyectos;

	public Proyecto() {
	}


	@Id
	@SequenceGenerator(name="PROYECTO_IDPROYECTO_GENERATOR", sequenceName="PROYECTO_ID_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PROYECTO_IDPROYECTO_GENERATOR")
	@Column(name="ID_PROYECTO")
	public long getIdProyecto() {
		return this.idProyecto;
	}

	public void setIdProyecto(long idProyecto) {
		this.idProyecto = idProyecto;
	}


	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	@Column(name="ESTADO_PROYECTO")
	public String getEstadoProyecto() {
		return this.estadoProyecto;
	}

	public void setEstadoProyecto(String estadoProyecto) {
		this.estadoProyecto = estadoProyecto;
	}


	@Column(name="ESTADO_REGISTRO")
	public String getEstadoRegistro() {
		return this.estadoRegistro;
	}

	public void setEstadoRegistro(String estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}


	 
	@Column(name="FECHA_ALTA")
	public Date getFechaAlta() {
		return this.fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}


	 
	@Column(name="FECHA_FIN")
	public Date getFechaFin() {
		return this.fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}


	 
	@Column(name="FECHA_INICIO")
	public Date getFechaInicio() {
		return this.fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}


	 
	@Column(name="FECHA_MODIFICACION")
	public Date getFechaModificacion() {
		return this.fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}


	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	@Column(name="NUMERO_CORTO")
	public String getNumeroCorto() {
		return this.numeroCorto;
	}

	public void setNumeroCorto(String numeroCorto) {
		this.numeroCorto = numeroCorto;
	}


	public String getObjetivos() {
		return this.objetivos;
	}

	public void setObjetivos(String objetivos) {
		this.objetivos = objetivos;
	}


	@Column(name="USUARIO_ALTA")
	public String getUsuarioAlta() {
		return this.usuarioAlta;
	}

	public void setUsuarioAlta(String usuarioAlta) {
		this.usuarioAlta = usuarioAlta;
	}


	@Column(name="USUARIO_MODIFICACION")
	public String getUsuarioModificacion() {
		return this.usuarioModificacion;
	}

	public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}

	
	@ManyToOne
	@JoinColumn(name="ID_PROMO")
	public Promo getPromo() {
		return promo;
	}


	public void setPromo(Promo promo) {
		this.promo = promo;
	}

	
	@ManyToOne
	@JoinColumn(name="ID_EMPRESA")
	public Empresa getEmpresa() {
		return empresa;
	}


	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}


	//bi-directional many-to-one association to Modulo
	@OneToMany(mappedBy="proyecto")
	public Set<Modulo> getModulos() {
		return this.modulos;
	}

	public void setModulos(Set<Modulo> modulos) {
		this.modulos = modulos;
	}

	
	public Modulo addModulos(Modulo modulos) {
		getModulos().add(modulos);
		modulos.setProyecto(this);

		return modulos;
	}

	public Modulo removeModulos(Modulo modulos) {
		getModulos().remove(modulos);
		modulos.setProyecto(null);

		return modulos;
	}
	
	

	//bi-directional many-to-one association to SuscriptorProyecto
	@OneToMany(mappedBy="proyecto")
	public Set<SuscriptorProyecto> getSuscriptorProyectos() {
		return this.suscriptorProyectos;
	}

	public void setSuscriptorProyectos(Set<SuscriptorProyecto> suscriptorProyectos) {
		this.suscriptorProyectos = suscriptorProyectos;
	}

	
	public SuscriptorProyecto addSuscriptorProyectos(SuscriptorProyecto suscriptorProyectos) {
		getSuscriptorProyectos().add(suscriptorProyectos);
		suscriptorProyectos.setProyecto(this);

		return suscriptorProyectos;
	}

	public SuscriptorProyecto removeSuscriptorProyectos(SuscriptorProyecto suscriptorProyectos) {
		getSuscriptorProyectos().remove(suscriptorProyectos);
		suscriptorProyectos.setProyecto(null);

		return suscriptorProyectos;
	}
}