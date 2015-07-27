package py.com.global.educador.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;


/**
 * The persistent class for the RESPUESTA database table.
 * 
 */
@Entity
public class Respuesta implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idRespuesta;
	private String contenidoRespuesta;
	private Boolean esRespuestaCorrecta;
	private String estadoRegistro;
	private String ordenRespuesta;
	private Pregunta pregunta;
	private String valorEsperado;
	private Promo promo;

	public Respuesta() {
	}


	@Id
	@SequenceGenerator(name="RESPUESTA_IDRESPUESTA_GENERATOR", sequenceName="RESPUESTA_ID_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RESPUESTA_IDRESPUESTA_GENERATOR")
	@Column(name="ID_RESPUESTA")
	public long getIdRespuesta() {
		return this.idRespuesta;
	}

	public void setIdRespuesta(long idRespuesta) {
		this.idRespuesta = idRespuesta;
	}


	@Column(name="CONTENIDO_RESPUESTA")
	public String getContenidoRespuesta() {
		return this.contenidoRespuesta;
	}

	public void setContenidoRespuesta(String contenidoRespuesta) {
		this.contenidoRespuesta = contenidoRespuesta;
	}


	@Column(name="ES_RESPUESTA_CORRECTA",precision=1,scale=0)
	public Boolean getEsRespuestaCorrecta() {
		return this.esRespuestaCorrecta;
	}

	public void setEsRespuestaCorrecta(Boolean esRespuestaCorrecta) {
		this.esRespuestaCorrecta = esRespuestaCorrecta;
	}


	@Column(name="ESTADO_REGISTRO")
	public String getEstadoRegistro() {
		return this.estadoRegistro;
	}

	public void setEstadoRegistro(String estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}


	@Column(name="ORDEN_RESPUESTA")
	public String getOrdenRespuesta() {
		return this.ordenRespuesta;
	}

	public void setOrdenRespuesta(String ordenRespuesta) {
		this.ordenRespuesta = ordenRespuesta;
	}


	

	//bi-directional many-to-one association to Pregunta
	@ManyToOne
	@JoinColumn(name="ID_PREGUNTA")
	public Pregunta getPregunta() {
		return this.pregunta;
	}

	public void setPregunta(Pregunta pregunta) {
		this.pregunta = pregunta;
	}


	@Column(name="VALOR_ESPERADO")
	public String getValorEsperado() {
		return valorEsperado;
	}


	public void setValorEsperado(String valorEsperado) {
		this.valorEsperado = valorEsperado;
	}

	@ManyToOne
	@JoinColumn(name="ID_PROMO")
	public Promo getPromo() {
		return promo;
	}


	public void setPromo(Promo promo) {
		this.promo = promo;
	}
	
	

	
}