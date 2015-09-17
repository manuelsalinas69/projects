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
	public Properties data;
	public ResponseDto(Integer statusCode, String statusDescription,
			Properties data) {
		this.statusCode = statusCode;
		this.statusDescription = statusDescription;
		this.data = data;
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


	public Properties getData() {
		return data;
	}


	public void setData(Properties data) {
		this.data = data;
	}


	@Override
	public String toString() {
		return "ReponseDto ["
				+ (statusCode != null ? "statusCode=" + statusCode + ", " : "")
				+ (statusDescription != null ? "statusDescription="
						+ statusDescription + ", " : "")
				+ (data != null ? "data=" + data : "") + "]";
	}
	
	
}
