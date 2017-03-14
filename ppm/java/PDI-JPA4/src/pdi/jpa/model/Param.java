package pdi.jpa.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the PARAMS database table.
 * 
 */
@Entity
@Table(name="PARAMS")
@NamedQuery(name="Param.findAll", query="SELECT p FROM Param p")
public class Param implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String value;

	public Param() {
	}


	@Id
	//@SequenceGenerator(name="PARAMS_ID_GENERATOR", sequenceName="PARAMS_SEQ")
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PARAMS_ID_GENERATOR")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}


	@Column(name="\"VALUE\"")
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}