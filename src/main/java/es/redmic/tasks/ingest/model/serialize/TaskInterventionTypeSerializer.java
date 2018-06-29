package es.redmic.tasks.ingest.model.serialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import es.redmic.tasks.ingest.model.intervention.common.InterventionType;

public final class TaskInterventionTypeSerializer extends JsonSerializer<InterventionType> {

	@Override
	public void serialize(InterventionType value, JsonGenerator generator, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		generator.writeString(value.toString());
	}
}