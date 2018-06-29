package es.redmic.tasks.ingest.model.deserialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import es.redmic.tasks.ingest.model.status.common.TaskStatus;

public final class TaskStatusDeserializer extends JsonDeserializer<TaskStatus>{

	@Override
	public TaskStatus deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		
		String jsonValue = parser.getText();
		
		return TaskStatus.fromString(jsonValue);
	}
}
