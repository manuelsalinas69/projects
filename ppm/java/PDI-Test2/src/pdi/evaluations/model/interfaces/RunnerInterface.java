package pdi.evaluations.model.interfaces;

import java.util.HashMap;

import pdi.jpa.model.ExecutionResults;

public interface RunnerInterface extends Runnable {
	
	public void setParams(HashMap<String, Object> params);
	public void setUp();
	public ExecutionResults getExecutionResults();
	public void execute();
	public boolean isRunnig();
	public void stop();
	
}
