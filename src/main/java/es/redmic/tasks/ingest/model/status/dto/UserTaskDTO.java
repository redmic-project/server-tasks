package es.redmic.tasks.ingest.model.status.dto;

import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import es.redmic.models.es.common.deserializer.CustomDateTimeDeserializer;
import es.redmic.models.es.common.dto.DTOStringImplement;
import es.redmic.models.es.common.serializer.CustomDateTimeSerializer;
import es.redmic.tasks.ingest.model.deserialize.TaskStatusDeserializer;
import es.redmic.tasks.ingest.model.intervention.matching.RequestUserInterventionMatchingTaskDTO;
import es.redmic.tasks.ingest.model.serialize.TaskStatusSerializer;
import es.redmic.tasks.ingest.model.status.common.TaskStatus;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "status")
@JsonSubTypes({ @Type(value = StartedTaskDTO.class, name = TaskStatus.Constants.STARTED),
		@Type(value = CompletedTaskDTO.class, name = TaskStatus.Constants.COMPLETED),
		@Type(value = FailedTaskDTO.class, name = TaskStatus.Constants.FAILED),
		@Type(value = RegisteredTaskDTO.class, name = TaskStatus.Constants.REGISTERED),
		@Type(value = RemovedTaskDTO.class, name = TaskStatus.Constants.REMOVED),
		@Type(value = RunningTaskDTO.class, name = TaskStatus.Constants.RUNNING),
		@Type(value = RequestUserInterventionMatchingTaskDTO.class, name = TaskStatus.Constants.WAITING_INTERVENTION) })
public class UserTaskDTO extends DTOStringImplement {

	@NotNull
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

	private DateTime startDate;

	// TODO Quitar y colocar solo en los DTO necesarios, ignorar "setter", ya
	// que esto no se va a setear desde la API
	private DateTime endDate;

	private DateTime updated;

	public UserTaskDTO() {
		super();
		setUpdated(new DateTime());
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	public Long getUserId() {
		return userId;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((progress == null) ? 0 : progress.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((taskName == null) ? 0 : taskName.hashCode());
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
		UserTaskDTO other = (UserTaskDTO) obj;
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
