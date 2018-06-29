package es.redmic.tasks.ingest.model.step.model;

import java.util.Map;

import es.redmic.tasks.ingest.model.status.common.TaskStatus;

public class RegisteredStep extends BaseStep {
	
	Map<String, Object> parameters;
	
	public RegisteredStep() {
		super();
		setStatus(TaskStatus.REGISTERED);
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}


}
