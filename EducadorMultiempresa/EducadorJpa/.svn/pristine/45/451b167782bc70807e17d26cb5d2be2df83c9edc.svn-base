package py.com.global.educador.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;



@Entity
@Table(name = "PARAMETRO_SISTEMA" )
public class ParametroSistema implements Serializable {

	private static final long serialVersionUID = 1L;
	private String parametro;
	private String valor;
	private String descripcion;
	private Boolean editable;
	private Boolean visible;

	public ParametroSistema() {
	}

	public ParametroSistema(String parametro, String valor) {
		this.parametro = parametro;
		this.valor = valor;
	}

	public ParametroSistema(String parametro, String valor, String descripcion,
			Boolean editable, Boolean visible) {
		this.parametro = parametro;
		this.valor = valor;
		this.descripcion = descripcion;
		this.editable = editable;
		this.visible = visible;
	}

	@Id
	@Column(name = "PARAMETRO", unique = true, nullable = false, length = 300)
	@NotNull
	@Length(max = 300)
	public String getParametro() {
		return this.parametro;
	}

	public void setParametro(String parametro) {
		this.parametro = parametro;
	}

	@Column(name = "VALOR", length = 500)
	@NotNull
	@Length(max = 500)
	public String getValor() {
		return this.valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	@Column(name = "DESCRIPCION", length = 500)
	@Length(max = 500)
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "EDITABLE", precision = 1, scale = 0)
	public Boolean getEditable() {
		return this.editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	@Column(name = "VISIBLE", precision = 1, scale = 0)
	public Boolean getVisible() {
		return this.visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}


}
