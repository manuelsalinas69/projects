package pdi.jpa.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the RUNNER_PARAM database table.
 * 
 */
@Entity
@Table(name="RUNNER_PARAM")
@NamedQuery(name="RunnerParam.findAll", query="SELECT r FROM RunnerParam r")
public class RunnerParam implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String paramName;
	private String paramValue;
	private Runner runner;

	public RunnerParam() {
	}


	@Id
	@SequenceGenerator(name="RUNNER_PARAM_ID_GENERATOR", sequenceName="RUNNER_PARAM_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RUNNER_PARAM_ID_GENERATOR")
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}


	@Column(name="PARAM_NAME")
	public String getParamName() {
		return this.paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}


	@Column(name="PARAM_VALUE")
	public String getParamValue() {
		return this.paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}


	//bi-directional many-to-one association to Runner
	@ManyToOne
	public Runner getRunner() {
		return this.runner;
	}

	public void setRunner(Runner runner) {
		this.runner = runner;
	}

}