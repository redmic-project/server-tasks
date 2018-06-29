package es.redmic.tasks.ingest.common.jobs;

import javax.validation.constraints.NotNull;

public abstract class Parameters extends ParametersBase {

	@NotNull
	String activityId;

	public Parameters() {

	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
}