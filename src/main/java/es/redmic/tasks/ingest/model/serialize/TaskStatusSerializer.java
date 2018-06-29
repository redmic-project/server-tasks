package es.redmic.tasks.ingest.model.serialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import es.redmic.tasks.ingest.model.status.common.TaskStatus;

public final class TaskStatusSerializer extends JsonSerializer<TaskStatus> {

	@Override
	public void serialize(TaskStatus value, JsonGenerator generator, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		generator.writeString(value.toString());
	}
}