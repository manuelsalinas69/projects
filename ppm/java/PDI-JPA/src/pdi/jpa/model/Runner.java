package pdi.jpa.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the RUNNER database table.
 * 
 */
@Entity
@NamedQuery(name="Runner.findAll", query="SELECT r FROM Runner r")
public class Runner implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String class_;
	private String name;
	private List<RunnerParam> runnerParams;

	public Runner() {
	}


	@Id
	@SequenceGenerator(name="RUNNER_ID_GENERATOR", sequenceName="RUNNER_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RUNNER_ID_GENERATOR")
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}


	@Column(name="\"CLASS\"")
	public String getClass_() {
		return this.class_;
	}

	public void setClass_(String class_) {
		this.class_ = class_;
	}


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	//bi-directional many-to-one association to RunnerParam
	@OneToMany(mappedBy="runner")
	public List<RunnerParam> getRunnerParams() {
		return this.runnerParams;
	}

	public void setRunnerParams(List<RunnerParam> runnerParams) {
		this.runnerParams = runnerParams;
	}

	public RunnerParam addRunnerParam(RunnerParam runnerParam) {
		getRunnerParams().add(runnerParam);
		runnerParam.setRunner(this);

		return runnerParam;
	}

	public RunnerParam removeRunnerParam(RunnerParam runnerParam) {
		getRunnerParams().remove(runnerParam);
		runnerParam.setRunner(null);

		return runnerParam;
	}

}