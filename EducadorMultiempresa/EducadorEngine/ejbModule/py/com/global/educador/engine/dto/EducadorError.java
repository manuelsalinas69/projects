package py.com.global.educador.engine.dto;

public class EducadorError {

	private String code;
	private String description;
	
	public EducadorError() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EducadorError(String code, String description) {
		super();
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "EducadorError [code=" + code + ", description=" + description
				+ "]";
	}
	
	
	
}

