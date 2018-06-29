package es.redmic.tasks.ingest.model.intervention.common;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
public abstract class InterventionBase  implements BaseInterventionDescription {
	
	InterventionType type;
	
	public InterventionType getType() {
		return type;
	}

	public void setType(InterventionType type) {
		this.type = type;
	}
}
