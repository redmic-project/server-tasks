package es.redmic.tasks.ingest.model.step.model;

import es.redmic.tasks.ingest.model.status.common.TaskStatus;

public class CompletedStep extends BaseStep {
	
	public CompletedStep() {
		super();
		setStatus(TaskStatus.COMPLETED);
	}
}