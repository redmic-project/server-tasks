package es.redmic.tasks.ingest.model.step.model;

import java.util.Map;

import es.redmic.tasks.ingest.model.status.common.TaskError;
import es.redmic.tasks.ingest.model.status.common.TaskStatus;

public class FailedStep extends BaseStep {
	
	private TaskError error;
	
	private Map<String, Object> parameters;
	
	public FailedStep() {
		super();
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