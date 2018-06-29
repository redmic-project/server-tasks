package es.redmic.tasks.ingest.model.status.dto;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import es.redmic.tasks.ingest.model.status.common.TaskStatus;

public class CompletedTaskDTO extends UserTaskDTO {

	@JsonInclude(Include.NON_NULL)
	private Object taskResult;

	public CompletedTaskDTO() {
		setEndDate(new DateTime());
		setStatus(TaskStatus.COMPLETED);
	}

	public Object getTaskResult() {
		return taskResult;
	}

	public void setTaskResult(Object result) {
		this.taskResult = result;
	}
}
