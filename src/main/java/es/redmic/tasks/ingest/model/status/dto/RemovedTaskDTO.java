package es.redmic.tasks.ingest.model.status.dto;

import es.redmic.tasks.ingest.model.status.common.TaskStatus;

public class RemovedTaskDTO extends UserTaskDTO {

	public RemovedTaskDTO() {
		super();
		setStatus(TaskStatus.REMOVED);
	}

}
