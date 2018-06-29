package es.redmic.tasks.ingest.model.common.dto;

import javax.validation.constraints.NotNull;

public abstract class TaskParametersBase extends TaskParametersCommon {

	@NotNull
	String fileName;

	@NotNull
	String activityId;

	String delimiter = ",";

	public TaskParametersBase() {
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
}