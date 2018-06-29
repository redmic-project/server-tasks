package es.redmic.tasks.ingest.model.common.dto;

public abstract class BaseActionTaskDTO {

	String userId;

	String taskName;

	public BaseActionTaskDTO() {

	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
