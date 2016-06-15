package py.com.global.educador.gui.entity;

// Generated Aug 22, 2015 11:45:57 AM by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;
import java.lang.Long;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import py.com.global.educador.gui.utils.EntityInterface;

/**
 * Empresa generated by hbm2java
 */
@Entity
@Table(name = "EMPRESA")
public class Empresa extends EntityInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long idEmpresa;
	private String nombre;
	private String direccion;
	private String telefonoPrincipal;
	private String telefonoSecundario;
	private String email;
	private String nombreContacto;
	private String telefonoContacto;
	private String gmapsUbicacion;
	private String estadoRegistro;
	private Date fechaCreacion;
	private String hashId;
	private Set<Usuario> usuarios = new HashSet<Usuario>(0);
	private Set<Proyecto> proyectos = new HashSet<Proyecto>(0);

	public Empresa() {
	}

	public Empresa(Long idEmpresa, String nombre) {
		this.idEmpresa = idEmpresa;
		this.nombre = nombre;
	}

	public Empresa(Long idEmpresa, String nombre, String direccion,
			String telefonoPrincipal, String telefonoSecundario, String email,
			String nombreContacto, String telefonoContacto,
			String gmapsUbicacion, Set<Usuario> usuarios,
			Set<Proyecto> proyectos) {
		this.idEmpresa = idEmpresa;
		this.nombre = nombre;
		this.direccion = direccion;
		this.telefonoPrincipal = telefonoPrincipal;
		this.telefonoSecundario = telefonoSecundario;
		this.email = email;
		this.nombreContacto = nombreContacto;
		this.telefonoContacto = telefonoContacto;
		this.gmapsUbicacion = gmapsUbicacion;
		this.usuarios = usuarios;
		this.proyectos = proyectos;
	}

	@Id
	@Column(name = "ID_EMPRESA", unique = true, nullable = false, precision = 22, scale = 0)
	@SequenceGenerator(name="EMPRESA_ID_SEQ_GENERATOR",sequenceName="EMPRESA_ID_SEQ",allocationSize=0)
	@GeneratedValue(generator="EMPRESA_ID_SEQ_GENERATOR")
	@NotNull
	public Long getIdEmpresa() {
		return this.idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Column(name = "NOMBRE", nullable = false, length = 100)
	@NotNull
	@Size(max = 100)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "DIRECCION", length = 200)
	@Size(max = 200)
	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	@Column(name = "TELEFONO_PRINCIPAL", length = 50)
	@Size(max = 50)
	public String getTelefonoPrincipal() {
		return this.telefonoPrincipal;
	}

	public void setTelefonoPrincipal(String telefonoPrincipal) {
		this.telefonoPrincipal = telefonoPrincipal;
	}

	@Column(name = "TELEFONO_SECUNDARIO", length = 50)
	@Size(max = 50)
	public String getTelefonoSecundario() {
		return this.telefonoSecundario;
	}

	public void setTelefonoSecundario(String telefonoSecundario) {
		this.telefonoSecundario = telefonoSecundario;
	}

	@Column(name = "EMAIL", length = 200)
	@Size(max = 200)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "NOMBRE_CONTACTO", length = 200)
	@Size(max = 200)
	public String getNombreContacto() {
		return this.nombreContacto;
	}

	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}

	@Column(name = "TELEFONO_CONTACTO", length = 50)
	@Size(max = 50)
	public String getTelefonoContacto() {
		return this.telefonoContacto;
	}

	public void setTelefonoContacto(String telefonoContacto) {
		this.telefonoContacto = telefonoContacto;
	}

	@Column(name = "GMAPS_UBICACION", length = 100)
	@Size(max = 100)
	public String getGmapsUbicacion() {
		return this.gmapsUbicacion;
	}

	public void setGmapsUbicacion(String gmapsUbicacion) {
		this.gmapsUbicacion = gmapsUbicacion;
	}

	
	@Column(name="ESTADO_REGISTRO",length=50)
	public String getEstadoRegistro() {
		return estadoRegistro;
	}

	public void setEstadoRegistro(String estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}
	
	
	@Column(name="FECHA_CREACION",length=7)
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	
	
	@Column(name="HASH_ID")
	public String getHashId() {
		return hashId;
	}

	public void setHashId(String hashId) {
		this.hashId = hashId;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "empresa")
	public Set<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Set<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "empresa")
	public Set<Proyecto> getProyectos() {
		return this.proyectos;
	}

	public void setProyectos(Set<Proyecto> proyectos) {
		this.proyectos = proyectos;
	}

	@Override
	@Transient
	public Serializable getId() {
		return getIdEmpresa();
	}

}
