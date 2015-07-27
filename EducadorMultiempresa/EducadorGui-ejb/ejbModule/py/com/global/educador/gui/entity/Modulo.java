package py.com.global.educador.gui.entity;

// Generated Aug 21, 2014 6:09:06 PM by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import py.com.global.educador.gui.entity.Usuario;

import py.com.global.educador.gui.utils.EntityInterface;

/**
 * Modulo generated by hbm2java
 */
@Entity
@Table(name = "MODULO" )
public class Modulo extends EntityInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long idModulo;
	private Proyecto proyecto;
	private String nombre;
	private String descripcion;
	private String objetivos;
	private Date fechaInicio;
	private Date fechaFin;
	private String estadoModulo;
	private String estadoRegistro;
	private String configuracionEnvioTip;
	private String tipoModulo;
	private String filtroSuscriptor;
	private Date fechaAlta;
	private Date fechaModificacion;
	private Usuario usuarioModificacion;
	private Usuario usuarioAlta;
	
	
	
	
	private Set<EjecucionSuscriptor> ejecucionSuscriptors = new HashSet<EjecucionSuscriptor>(
			0);
	private Set<PlanificacionEnvio> planificacionEnvios = new HashSet<PlanificacionEnvio>(
			0);
	private Set<SuscriptorProyecto> suscriptorProyectos = new HashSet<SuscriptorProyecto>(
			0);
	private Set<Tip> tips = new HashSet<Tip>(0);
	private Set<Evaluacion> evaluacions = new HashSet<Evaluacion>(0);

	public Modulo() {
	}

	public Modulo(Long idModulo) {
		this.idModulo = idModulo;
	}

	public Modulo(Long idModulo, Proyecto proyecto, String nombre,
			String descripcion, String objetivos, Date fechaInicio,
			Date fechaFin, String estadoModulo, String estadoRegistro,
			String configuracionEnvioTip, String tipoModulo,
			Set<EjecucionSuscriptor> ejecucionSuscriptors,
			Set<PlanificacionEnvio> planificacionEnvios,
			Set<SuscriptorProyecto> suscriptorProyectos, Set<Tip> tips,
			Set<Evaluacion> evaluacions) {
		this.idModulo = idModulo;
		this.proyecto = proyecto;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.objetivos = objetivos;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.estadoModulo = estadoModulo;
		this.estadoRegistro = estadoRegistro;
		this.configuracionEnvioTip = configuracionEnvioTip;
		this.tipoModulo = tipoModulo;
		this.ejecucionSuscriptors = ejecucionSuscriptors;
		this.planificacionEnvios = planificacionEnvios;
		this.suscriptorProyectos = suscriptorProyectos;
		this.tips = tips;
		this.evaluacions = evaluacions;
	}

	@Id
	@SequenceGenerator(name="MODULO_IDMODULO_GENERATOR", sequenceName="MODULO_ID_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MODULO_IDMODULO_GENERATOR")
	@Column(name = "ID_MODULO", unique = true, nullable = false, precision = 22, scale = 0)
	@NotNull
	public Long getIdModulo() {
		return this.idModulo;
	}

	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PROYECTO")
	public Proyecto getProyecto() {
		return this.proyecto;
	}

	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
	}

	@Column(name = "NOMBRE", length = 100)
	@Size(max = 100)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "DESCRIPCION", length = 200)
	@Size(max = 200)
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "OBJETIVOS", length = 500)
	@Size(max = 500)
	public String getObjetivos() {
		return this.objetivos;
	}

	public void setObjetivos(String objetivos) {
		this.objetivos = objetivos;
	}

	 
	@Column(name = "FECHA_INICIO", length = 7)
	public Date getFechaInicio() {
		return this.fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	 
	@Column(name = "FECHA_FIN", length = 7)
	public Date getFechaFin() {
		return this.fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	@Column(name = "ESTADO_MODULO", length = 30)
	@Size(max = 30)
	public String getEstadoModulo() {
		return this.estadoModulo;
	}

	public void setEstadoModulo(String estadoModulo) {
		this.estadoModulo = estadoModulo;
	}

	@Column(name = "ESTADO_REGISTRO", length = 30)
	@Size(max = 30)
	public String getEstadoRegistro() {
		return this.estadoRegistro;
	}

	public void setEstadoRegistro(String estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}

	@Column(name = "CONFIGURACION_ENVIO_TIP", length = 500)
	@Size(max = 500)
	public String getConfiguracionEnvioTip() {
		return this.configuracionEnvioTip;
	}

	public void setConfiguracionEnvioTip(String configuracionEnvioTip) {
		this.configuracionEnvioTip = configuracionEnvioTip;
	}

	@Column(name = "TIPO_MODULO", length = 30)
	@Size(max = 30)
	public String getTipoModulo() {
		return this.tipoModulo;
	}

	public void setTipoModulo(String tipoModulo) {
		this.tipoModulo = tipoModulo;
	}

	@Column(name = "FILTRO_SUSCRIPTOR", length = 200)
	@Size(max = 200)
	public String getFiltroSuscriptor() {
		return filtroSuscriptor;
	}

	public void setFiltroSuscriptor(String filtroSuscriptor) {
		this.filtroSuscriptor = filtroSuscriptor;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_ALTA", length = 7)
	public Date getFechaAlta() {
		return this.fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_MODIFICACION", length = 7)
	public Date getFechaModificacion() {
		return this.fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USUARIO_ALTA")
	public Usuario getUsuarioAlta() {
		return this.usuarioAlta;
	}

	public void setUsuarioAlta(Usuario usuarioAlta) {
		this.usuarioAlta = usuarioAlta;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USUARIO_MODIFICACION")
	public Usuario getUsuarioModificacion() {
		return this.usuarioModificacion;
	}

	public void setUsuarioModificacion(Usuario usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}

	
	
	
	
	
	
	
	
	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modulo")
	public Set<EjecucionSuscriptor> getEjecucionSuscriptors() {
		return this.ejecucionSuscriptors;
	}

	public void setEjecucionSuscriptors(
			Set<EjecucionSuscriptor> ejecucionSuscriptors) {
		this.ejecucionSuscriptors = ejecucionSuscriptors;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modulo")
	public Set<PlanificacionEnvio> getPlanificacionEnvios() {
		return this.planificacionEnvios;
	}

	public void setPlanificacionEnvios(
			Set<PlanificacionEnvio> planificacionEnvios) {
		this.planificacionEnvios = planificacionEnvios;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modulo")
	public Set<SuscriptorProyecto> getSuscriptorProyectos() {
		return this.suscriptorProyectos;
	}

	public void setSuscriptorProyectos(
			Set<SuscriptorProyecto> suscriptorProyectos) {
		this.suscriptorProyectos = suscriptorProyectos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modulo")
	public Set<Tip> getTips() {
		return this.tips;
	}

	public void setTips(Set<Tip> tips) {
		this.tips = tips;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modulo")
	public Set<Evaluacion> getEvaluacions() {
		return this.evaluacions;
	}

	public void setEvaluacions(Set<Evaluacion> evaluacions) {
		this.evaluacions = evaluacions;
	}

	@Override
	@Transient
	public Serializable getId() {
		return getIdModulo();
	}

}