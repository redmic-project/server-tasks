package es.redmic.tasks.ingest.model.status.common;

public class TaskError {
	
	private String code;
	private String description;
	private String exceptionId;
	
	public TaskError() {
	}
	
	public String getExceptionId() {
		return exceptionId;
	}

	public void setExceptionId(String exceptionId) {
		this.exceptionId = exceptionId;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String codeS) {
		code = codeS;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String descriptionS) {
		description = descriptionS;
	}
}
