package es.redmic.tasks.ingest.model.step.model;

import es.redmic.tasks.ingest.model.status.common.TaskStatus;

public class RunningStep extends BaseStep {
	
	public RunningStep() {
		super();
		setStatus(TaskStatus.RUNNING);
	}
}