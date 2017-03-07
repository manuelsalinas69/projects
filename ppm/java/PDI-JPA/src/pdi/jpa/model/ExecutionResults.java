package pdi.jpa.model;

import java.util.HashMap;

public class ExecutionResults {
	
	HashMap<String, Object> params;
	HashMap<String, Object> results;
	
	
	
	public ExecutionResults() {
		this.params= new HashMap<String,Object>();
		this.results= new HashMap<String,Object>();
	}
	
	public void putParam(String paramName, Object paramValue){
		params.put(paramName, paramValue);
	}
	
	public Object getParam(String paramName){
		return params.get(paramName);
	}
	
	public void putResult(String resultName, Object resultValue){
		results.put(resultName, resultValue);
	}
	
	public Object getResult(String resultName){
		return results.get(resultName);
	}
	
	public HashMap<String, Object> getParams() {
		return params;
	}
	

}
