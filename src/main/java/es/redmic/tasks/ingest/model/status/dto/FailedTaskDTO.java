package es.redmic.tasks.ingest.model.status.dto;

import java.util.Map;

import org.joda.time.DateTime;

import es.redmic.tasks.ingest.model.status.common.TaskError;
import es.redmic.tasks.ingest.model.status.common.TaskStatus;

public class FailedTaskDTO extends UserTaskDTO {

	private TaskError error;

	private Map<String, Object> parameters;

	public FailedTaskDTO() {
		setEndDate(new DateTime());
		setStatus(TaskStatus.FAILED);
	}

	public TaskError getError() {
		return error;
	}

	public void setError(TaskError error) {
		this.error = error;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
}
