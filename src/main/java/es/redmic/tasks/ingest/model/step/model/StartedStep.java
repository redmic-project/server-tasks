package es.redmic.tasks.ingest.model.step.model;

import es.redmic.tasks.ingest.model.status.common.TaskStatus;

public class StartedStep extends BaseStep {

	public StartedStep() {
		super();
		setStatus(TaskStatus.STARTED);
	}
}
