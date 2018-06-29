package es.redmic.test.tasks.unit.job.ingest.status;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import es.redmic.tasks.ingest.model.deserialize.TaskStatusDeserializer;
import es.redmic.tasks.ingest.model.status.common.TaskStatus;

public class TaskStatusDeserializerTest {

	ObjectMapper jacksonMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	@Test
	public void DeserializeTaskStatus() throws IOException {
		String json = "{\"status\":\"started\"}";
		
		TestStatusTask dto = jacksonMapper.readValue(json, TestStatusTask.class);
		
		assertTrue(dto.getStatus() == TaskStatus.STARTED);

	}
	
	public static class TestStatusTask {
		
		@JsonDeserialize(using=TaskStatusDeserializer.class)
		TaskStatus status;

		public TaskStatus getStatus() {
			return status;
		}

		public void setStatus(TaskStatus status) {
			this.status = status;
		}
	}
}
