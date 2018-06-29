package es.redmic.tasks.ingest.model.step.intervention;

import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import es.redmic.models.es.common.deserializer.CustomDateTimeDeserializer;
import es.redmic.models.es.common.serializer.CustomDateTimeSerializer;
import es.redmic.tasks.ingest.model.intervention.common.InterventionDescriptionBase;
import es.redmic.tasks.ingest.model.status.common.TaskStatus;
import es.redmic.tasks.ingest.model.step.model.BaseStep;

public class WaitingInterventionStep extends BaseStep {

	InterventionDescriptionBase interventionDescription;
	
	ResponseInterventionBase response;
	
	DateTime responseDate;

	public WaitingInterventionStep() {
		setStatus(TaskStatus.WAITING_INTERVENTION);
	}

	public InterventionDescriptionBase getInterventionDescription() {
		return interventionDescription;
	}

	public void setInterventionDescription(InterventionDescriptionBase interventionDescription) {
		this.interventionDescription = interventionDescription;
	}
	
	public ResponseInterventionBase getResponse() {
		return response;
	}

	public void setResponse(ResponseInterventionBase response) {
		this.response = response;
		
		if (responseDate == null) {
			setResponseDate(DateTime.now());
		}
	}
	
	@JsonSerialize(using = CustomDateTimeSerializer.class)	
	public DateTime getResponseDate() {
		return responseDate;
	}

	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	public void setResponseDate(DateTime responseDate) {
		this.responseDate = responseDate;
	}
}
