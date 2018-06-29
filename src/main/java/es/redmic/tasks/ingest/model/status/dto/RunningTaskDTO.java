package es.redmic.tasks.ingest.model.status.dto;

import es.redmic.tasks.ingest.model.status.common.TaskStatus;

public class RunningTaskDTO extends UserTaskDTO {

	public RunningTaskDTO() {
		super();
		setStatus(TaskStatus.RUNNING);
	}

}
