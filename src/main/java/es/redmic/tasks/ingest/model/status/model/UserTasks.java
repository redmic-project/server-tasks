package es.redmic.tasks.ingest.model.status.model;

/*-
 * #%L
 * Tasks
 * %%
 * Copyright (C) 2019 REDMIC Project / Server
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import es.redmic.models.es.common.deserializer.CustomDateTimeDeserializer;
import es.redmic.models.es.common.model.BaseAbstractStringES;
import es.redmic.models.es.common.serializer.CustomDateTimeSerializer;
import es.redmic.tasks.ingest.model.deserialize.TaskStatusDeserializer;
import es.redmic.tasks.ingest.model.serialize.TaskStatusSerializer;
import es.redmic.tasks.ingest.model.status.common.TaskStatus;
import es.redmic.tasks.ingest.model.step.model.BaseStep;

public class UserTasks extends BaseAbstractStringES { // <= taskId

	@JsonSerialize(using = TaskStatusSerializer.class)
	@JsonDeserialize(using = TaskStatusDeserializer.class)
	private TaskStatus status;

	private Integer progress;

	@NotNull
	private Long userId;

	@NotNull
	private String taskType;

	@NotNull
	private String taskName;

	private Object taskResult;

	private DateTime startDate;

	private DateTime endDate;

	private DateTime updated;

	private List<BaseStep> steps;

	public UserTasks() {
		steps = new ArrayList<BaseStep>();
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public Long getUserId() {
		return userId;
	}

	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Object getTaskResult() {
		return taskResult;
	}

	public void setTaskResult(Object taskResult) {
		this.taskResult = taskResult;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public DateTime getStartDate() {
		return startDate;
	}

	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public DateTime getEndDate() {
		return endDate;
	}

	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public DateTime getUpdated() {
		return updated;
	}

	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	public void setUpdated(DateTime updated) {
		this.updated = updated;
	}

	public List<BaseStep> getSteps() {
		return steps;
	}

	public void setSteps(List<BaseStep> steps) {
		this.steps = steps;
	}

	public List<BaseStep> addStep(BaseStep step) {
		steps.add(step);

		return steps;
	}

	public BaseStep firstStep() {
		return getStep(0);
	}

	public BaseStep lastStep() {
		return getStep(steps.size() - 1);
	}

	public BaseStep lastWaitingInterventionStep() {
		for (int i = steps.size() - 1; i >= 0; i--) {
			if (steps.get(i).getStatus() == TaskStatus.WAITING_INTERVENTION)
				return steps.get(i);
		}
		return null;
	}

	@JsonIgnore
	public BaseStep getStep(int pos) {
		BaseStep step = null;

		if ((pos > -1) && (steps.size() > 0) && (pos < steps.size())) {
			step = steps.get(pos);
		}

		return step;
	}

	public void updateStep(BaseStep step) {
		if (steps.size() > 0)
			steps.set(steps.size() - 1, step);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((progress == null) ? 0 : progress.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((taskName == null) ? 0 : taskName.hashCode());
		result = prime * result + ((taskResult == null) ? 0 : taskResult.hashCode());
		result = prime * result + ((taskType == null) ? 0 : taskType.hashCode());
		result = prime * result + ((updated == null) ? 0 : updated.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserTasks other = (UserTasks) obj;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (progress == null) {
			if (other.progress != null)
				return false;
		} else if (!progress.equals(other.progress))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (taskName == null) {
			if (other.taskName != null)
				return false;
		} else if (!taskName.equals(other.taskName))
			return false;
		if (taskResult == null) {
			if (other.taskResult != null)
				return false;
		} else if (!taskResult.equals(other.taskResult))
			return false;
		if (taskType == null) {
			if (other.taskType != null)
				return false;
		} else if (!taskType.equals(other.taskType))
			return false;
		if (updated == null) {
			if (other.updated != null)
				return false;
		} else if (!updated.equals(other.updated))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
}
