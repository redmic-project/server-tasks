package es.redmic.tasks.ingest.model.status.dto;

import org.joda.time.DateTime;

import es.redmic.tasks.ingest.model.status.common.TaskStatus;

public class RegisteredTaskDTO extends UserTaskDTO {

	public RegisteredTaskDTO() {
		super();
		setEndDate(new DateTime());
		setStatus(TaskStatus.REGISTERED);
	}

}
