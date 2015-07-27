package py.com.global.educador.gui.entity;

// Generated Aug 21, 2014 6:09:06 PM by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;
import java.lang.Long;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import py.com.global.educador.gui.utils.EntityInterface;

/**
 * Respuesta generated by hbm2java
 */
@Entity
@Table(name = "RESPUESTA" )
public class Respuesta extends EntityInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long idRespuesta;
	private Pregunta pregunta;
	private String contenidoRespuesta;
	private String ordenRespuesta;
	private Boolean esRespuestaCorrecta;
	private String estadoRegistro;
	private String valorEsperado;

	public Respuesta() {
	}

	public Respuesta(Long idRespuesta) {
		this.idRespuesta = idRespuesta;
	}

	public Respuesta(Long idRespuesta, Pregunta pregunta,
			String contenidoRespuesta, String ordenRespuesta,
			Boolean esRespuestaCorrecta, String estadoRegistro,
			String valorEsperado) {
		this.idRespuesta = idRespuesta;
		this.pregunta = pregunta;
		this.contenidoRespuesta = contenidoRespuesta;
		this.ordenRespuesta = ordenRespuesta;
		this.esRespuestaCorrecta = esRespuestaCorrecta;
		this.estadoRegistro = estadoRegistro;
		this.valorEsperado = valorEsperado;
	}

	@Id
	@Column(name = "ID_RESPUESTA", unique = true, nullable = false, precision = 22, scale = 0)
	@SequenceGenerator(name="RESPUESTA_IDRESPUESTA_GENERATOR", sequenceName="RESPUESTA_ID_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RESPUESTA_IDRESPUESTA_GENERATOR")
	@NotNull
	public Long getIdRespuesta() {
		return this.idRespuesta;
	}

	public void setIdRespuesta(Long idRespuesta) {
		this.idRespuesta = idRespuesta;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PREGUNTA")
	public Pregunta getPregunta() {
		return this.pregunta;
	}

	public void setPregunta(Pregunta pregunta) {
		this.pregunta = pregunta;
	}

	@Column(name = "CONTENIDO_RESPUESTA", length = 200)
	@Size(max = 200)
	public String getContenidoRespuesta() {
		return this.contenidoRespuesta;
	}

	public void setContenidoRespuesta(String contenidoRespuesta) {
		this.contenidoRespuesta = contenidoRespuesta;
	}

	@Column(name = "ORDEN_RESPUESTA", length = 30)
	@Size(max = 30)
	public String getOrdenRespuesta() {
		return this.ordenRespuesta;
	}

	public void setOrdenRespuesta(String ordenRespuesta) {
		this.ordenRespuesta = ordenRespuesta;
	}

	@Column(name = "ES_RESPUESTA_CORRECTA", precision = 1, scale = 0)
	public Boolean getEsRespuestaCorrecta() {
		return this.esRespuestaCorrecta;
	}

	public void setEsRespuestaCorrecta(Boolean esRespuestaCorrecta) {
		this.esRespuestaCorrecta = esRespuestaCorrecta;
	}

	@Column(name = "ESTADO_REGISTRO", length = 30)
	@Size(max = 30)
	public String getEstadoRegistro() {
		return this.estadoRegistro;
	}

	public void setEstadoRegistro(String estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}

	@Column(name = "VALOR_ESPERADO", length = 160)
	@Size(max = 160)
	public String getValorEsperado() {
		return this.valorEsperado;
	}

	public void setValorEsperado(String valorEsperado) {
		this.valorEsperado = valorEsperado;
	}

	@Override
	@Transient
	public Serializable getId() {
		return getIdRespuesta();
	}

}