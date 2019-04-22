package es.redmic.tasks.ingest.model.step.intervention;

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
