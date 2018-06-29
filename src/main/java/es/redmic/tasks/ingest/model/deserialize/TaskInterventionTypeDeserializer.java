package es.redmic.tasks.ingest.model.deserialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import es.redmic.tasks.ingest.model.intervention.common.InterventionType;

public final class TaskInterventionTypeDeserializer extends JsonDeserializer<InterventionType>{

	@Override
	public InterventionType deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		
		String jsonValue = parser.getText();
		
		return InterventionType.fromString(jsonValue);
	}
}
