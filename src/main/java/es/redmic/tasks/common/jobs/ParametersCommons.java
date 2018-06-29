package es.redmic.tasks.common.jobs;

import javax.validation.constraints.NotNull;

public abstract class ParametersCommons {

	@NotNull
	String taskId;

	@NotNull
	String userId;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
