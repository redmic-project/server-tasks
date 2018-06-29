package es.redmic.tasks.ingest.model.intervention.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import es.redmic.tasks.ingest.model.deserialize.TaskInterventionTypeDeserializer;
import es.redmic.tasks.ingest.model.serialize.TaskInterventionTypeSerializer;

public interface BaseInterventionDescription {

	@JsonSerialize(using = TaskInterventionTypeSerializer.class)
	public InterventionType getType();

	@JsonDeserialize(using = TaskInterventionTypeDeserializer.class)
	public void setType(InterventionType type);
	
}
