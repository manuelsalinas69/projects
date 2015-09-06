package py.com.global.educador.gui.entity;

// Generated Sep 14, 2014 11:48:15 AM by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import py.com.global.educador.gui.utils.EntityInterface;

/**
 * Usuario generated by hbm2java
 */
@Entity
@Table(name = "USUARIO")
public class Usuario extends EntityInterface  {

	private String usuario;
	private String contrasena;
	private String nombre;
	private String estado;
	private Date fechaRegistro;
	private Empresa empresa;
	private Boolean rolAdminSistema;
	private Boolean rolAdminProyectos;
	private Boolean rolConsultas;
	

	
	private Set<Pregunta> preguntasForUsuarioModificacion = new HashSet<Pregunta>(
			0);
	private Set<Tip> tipsForUsuarioAlta = new HashSet<Tip>(0);
	private Set<Proyecto> proyectosForUsuarioAlta = new HashSet<Proyecto>(0);
	private Set<Pregunta> preguntasForUsuarioAlta = new HashSet<Pregunta>(0);
	private Set<Tip> tipsForUsuarioModificacion = new HashSet<Tip>(0);
	private Set<Evaluacion> evaluacionsForUsuarioModificacion = new HashSet<Evaluacion>(
			0);
	private Set<Proyecto> proyectosForUsuarioModificacion = new HashSet<Proyecto>(
			0);
	private Set<Modulo> modulosForUsuarioAlta = new HashSet<Modulo>(0);
	private Set<Evaluacion> evaluacionsForUsuarioAlta = new HashSet<Evaluacion>(
			0);
	private Set<Modulo> modulosForUsuarioModificacion = new HashSet<Modulo>(0);

	public Usuario() {
	}

	public Usuario(String usuario) {
		this.usuario = usuario;
	}

	public Usuario(String usuario, String contrasena, String nombre,
			String estado, Date fechaRegistro, Boolean rolAdminSistema,
			Boolean rolAdminProyectos, Boolean rolConsultas,
			Set<Pregunta> preguntasForUsuarioModificacion,
			Set<Tip> tipsForUsuarioAlta, Set<Proyecto> proyectosForUsuarioAlta,
			Set<Pregunta> preguntasForUsuarioAlta,
			Set<Tip> tipsForUsuarioModificacion,
			Set<Evaluacion> evaluacionsForUsuarioModificacion,
			Set<Proyecto> proyectosForUsuarioModificacion,
			Set<Modulo> modulosForUsuarioAlta,
			Set<Evaluacion> evaluacionsForUsuarioAlta,
			Set<Modulo> modulosForUsuarioModificacion) {
		this.usuario = usuario;
		this.contrasena = contrasena;
		this.nombre = nombre;
		this.estado = estado;
		this.fechaRegistro = fechaRegistro;
		this.rolAdminSistema = rolAdminSistema;
		this.rolAdminProyectos = rolAdminProyectos;
		this.rolConsultas = rolConsultas;
		this.preguntasForUsuarioModificacion = preguntasForUsuarioModificacion;
		this.tipsForUsuarioAlta = tipsForUsuarioAlta;
		this.proyectosForUsuarioAlta = proyectosForUsuarioAlta;
		this.preguntasForUsuarioAlta = preguntasForUsuarioAlta;
		this.tipsForUsuarioModificacion = tipsForUsuarioModificacion;
		this.evaluacionsForUsuarioModificacion = evaluacionsForUsuarioModificacion;
		this.proyectosForUsuarioModificacion = proyectosForUsuarioModificacion;
		this.modulosForUsuarioAlta = modulosForUsuarioAlta;
		this.evaluacionsForUsuarioAlta = evaluacionsForUsuarioAlta;
		this.modulosForUsuarioModificacion = modulosForUsuarioModificacion;
	}

	@Id
	@Column(name = "USUARIO", unique = true, nullable = false, length = 30)
	@NotNull
	@Size(max = 30)
	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	@Column(name = "CONTRASENA", length = 200)
	@Size(max = 200)
	public String getContrasena() {
		return this.contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	@Column(name = "NOMBRE", length = 200)
	@Size(max = 200)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "ESTADO", length = 10)
	@Size(max = 10)
	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_REGISTRO", length = 7)
	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	@Column(name = "ROL_ADMIN_SISTEMA", precision = 1, scale = 0)
	public Boolean getRolAdminSistema() {
		return this.rolAdminSistema;
	}

	public void setRolAdminSistema(Boolean rolAdminSistema) {
		this.rolAdminSistema = rolAdminSistema;
	}

	@Column(name = "ROL_ADMIN_PROYECTOS", precision = 1, scale = 0)
	public Boolean getRolAdminProyectos() {
		return this.rolAdminProyectos;
	}

	public void setRolAdminProyectos(Boolean rolAdminProyectos) {
		this.rolAdminProyectos = rolAdminProyectos;
	}

	@Column(name = "ROL_CONSULTAS", precision = 1, scale = 0)
	public Boolean getRolConsultas() {
		return this.rolConsultas;
	}

	public void setRolConsultas(Boolean rolConsultas) {
		this.rolConsultas = rolConsultas;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_EMPRESA")
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioModificacion")
	public Set<Pregunta> getPreguntasForUsuarioModificacion() {
		return this.preguntasForUsuarioModificacion;
	}

	public void setPreguntasForUsuarioModificacion(
			Set<Pregunta> preguntasForUsuarioModificacion) {
		this.preguntasForUsuarioModificacion = preguntasForUsuarioModificacion;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioAlta")
	public Set<Tip> getTipsForUsuarioAlta() {
		return this.tipsForUsuarioAlta;
	}

	public void setTipsForUsuarioAlta(Set<Tip> tipsForUsuarioAlta) {
		this.tipsForUsuarioAlta = tipsForUsuarioAlta;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioAlta")
	public Set<Proyecto> getProyectosForUsuarioAlta() {
		return this.proyectosForUsuarioAlta;
	}

	public void setProyectosForUsuarioAlta(Set<Proyecto> proyectosForUsuarioAlta) {
		this.proyectosForUsuarioAlta = proyectosForUsuarioAlta;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioAlta")
	public Set<Pregunta> getPreguntasForUsuarioAlta() {
		return this.preguntasForUsuarioAlta;
	}

	public void setPreguntasForUsuarioAlta(Set<Pregunta> preguntasForUsuarioAlta) {
		this.preguntasForUsuarioAlta = preguntasForUsuarioAlta;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioModificacion")
	public Set<Tip> getTipsForUsuarioModificacion() {
		return this.tipsForUsuarioModificacion;
	}

	public void setTipsForUsuarioModificacion(
			Set<Tip> tipsForUsuarioModificacion) {
		this.tipsForUsuarioModificacion = tipsForUsuarioModificacion;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioModificacion")
	public Set<Evaluacion> getEvaluacionsForUsuarioModificacion() {
		return this.evaluacionsForUsuarioModificacion;
	}

	public void setEvaluacionsForUsuarioModificacion(
			Set<Evaluacion> evaluacionsForUsuarioModificacion) {
		this.evaluacionsForUsuarioModificacion = evaluacionsForUsuarioModificacion;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioModificacion")
	public Set<Proyecto> getProyectosForUsuarioModificacion() {
		return this.proyectosForUsuarioModificacion;
	}

	public void setProyectosForUsuarioModificacion(
			Set<Proyecto> proyectosForUsuarioModificacion) {
		this.proyectosForUsuarioModificacion = proyectosForUsuarioModificacion;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioAlta")
	public Set<Modulo> getModulosForUsuarioAlta() {
		return this.modulosForUsuarioAlta;
	}

	public void setModulosForUsuarioAlta(Set<Modulo> modulosForUsuarioAlta) {
		this.modulosForUsuarioAlta = modulosForUsuarioAlta;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioAlta")
	public Set<Evaluacion> getEvaluacionsForUsuarioAlta() {
		return this.evaluacionsForUsuarioAlta;
	}

	public void setEvaluacionsForUsuarioAlta(
			Set<Evaluacion> evaluacionsForUsuarioAlta) {
		this.evaluacionsForUsuarioAlta = evaluacionsForUsuarioAlta;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioModificacion")
	public Set<Modulo> getModulosForUsuarioModificacion() {
		return this.modulosForUsuarioModificacion;
	}

	public void setModulosForUsuarioModificacion(
			Set<Modulo> modulosForUsuarioModificacion) {
		this.modulosForUsuarioModificacion = modulosForUsuarioModificacion;
	}

	@Override
	@Transient
	public Serializable getId() {
		return getUsuario();
	}
	


	
	
}
