package py.com.global.educador.engine.dto;

import java.io.Serializable;
import java.util.Properties;

public class ResponseDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Integer statusCode;
	public String statusDescription;
	public Properties responseBody;
	public ResponseDto(Integer statusCode, String statusDescription,
			Properties responseBody) {
		this.statusCode = statusCode;
		this.statusDescription = statusDescription;
		this.responseBody = responseBody;
	}
	
	
	public Integer getStatusCode() {
		return statusCode;
	}


	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}


	public String getStatusDescription() {
		return statusDescription;
	}


	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}


	public Properties getResponseBody() {
		return responseBody;
	}


	public void setResponseBody(Properties responseBody) {
		this.responseBody = responseBody;
	}


	@Override
	public String toString() {
		return "ReponseDto ["
				+ (statusCode != null ? "statusCode=" + statusCode + ", " : "")
				+ (statusDescription != null ? "statusDescription="
						+ statusDescription + ", " : "")
				+ (responseBody != null ? "responseBody=" + responseBody : "") + "]";
	}
	
	
}
