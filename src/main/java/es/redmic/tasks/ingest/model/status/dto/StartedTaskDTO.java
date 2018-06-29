package es.redmic.tasks.ingest.model.status.dto;

import org.joda.time.DateTime;

import es.redmic.tasks.ingest.model.status.common.TaskStatus;


public class StartedTaskDTO extends UserTaskDTO {

	public StartedTaskDTO() {
		setStartDate(new DateTime());
		setStatus(TaskStatus.STARTED);
	}
}
