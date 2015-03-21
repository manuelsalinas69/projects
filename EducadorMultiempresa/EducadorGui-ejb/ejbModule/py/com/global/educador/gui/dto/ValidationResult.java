package py.com.global.educador.gui.dto;

import java.io.Serializable;
import java.util.Arrays;

public class ValidationResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean successful;
	private String message;
	private String[] parameters;
	public ValidationResult(boolean successful, String message, String ... parameters) {
		this.successful = successful;
		this.message = message;
		this.parameters=parameters;
	}
	
	

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
	public String[] getParameters() {
		return parameters;
	}



	public void setParameters(String[] parameters) {
		this.parameters = parameters;
	}



	@Override
	public String toString() {
		return "ValidationResult [successful="
				+ successful
				+ ", "
				+ (message != null ? "message=" + message + ", " : "")
				+ (parameters != null ? "parameters="
						+ Arrays.toString(parameters) : "") + "]";
	}



	
	
	
	
	
}
