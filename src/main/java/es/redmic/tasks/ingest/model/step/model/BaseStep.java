package es.redmic.tasks.ingest.model.step.model;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import es.redmic.models.es.common.deserializer.CustomDateTimeDeserializer;
import es.redmic.models.es.common.serializer.CustomDateTimeSerializer;
import es.redmic.tasks.ingest.model.deserialize.TaskStatusDeserializer;
import es.redmic.tasks.ingest.model.serialize.TaskStatusSerializer;
import es.redmic.tasks.ingest.model.status.common.TaskStatus;
import es.redmic.tasks.ingest.model.step.intervention.WaitingInterventionStep;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "status")
@JsonSubTypes({ 
	@Type(value = RegisteredStep.class, name = TaskStatus.Constants.REGISTERED),
	@Type(value = StartedStep.class, name = TaskStatus.Constants.STARTED),
	@Type(value = WaitingInterventionStep.class, name = TaskStatus.Constants.WAITING_INTERVENTION),
	@Type(value = RunningStep.class, name = TaskStatus.Constants.RUNNING),
	@Type(value = CompletedStep.class, name = TaskStatus.Constants.COMPLETED),
	@Type(value = FailedStep.class, name = TaskStatus.Constants.FAILED)
 })
public class BaseStep {

	TaskStatus status;
	
	DateTime date;

	public BaseStep() {
		setStatus(TaskStatus.ENQUEUED);
		setDate(new DateTime());
	}
	
	@JsonSerialize(using = TaskStatusSerializer.class)
	public TaskStatus getStatus() {
		return status;
	}
	
	@JsonDeserialize(using = TaskStatusDeserializer.class)
	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public DateTime getDate() {
		return date;
	}
	
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	public void setDate(DateTime date) {
		this.date = date;
	}

}
