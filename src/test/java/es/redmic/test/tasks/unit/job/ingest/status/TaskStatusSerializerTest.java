package es.redmic.test.tasks.unit.job.ingest.status;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.redmic.tasks.ingest.model.status.common.TaskStatus;
import es.redmic.tasks.ingest.model.status.dto.UserTaskDTO;

public class TaskStatusSerializerTest {

	ObjectMapper jacksonMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	@Test
	public void SerializerTaskStatus() throws IOException {

		UserTaskDTO dto = new UserTaskDTO();
		dto.setStatus(TaskStatus.STARTED);

		String jsonSerialized = jacksonMapper.writeValueAsString(dto);

		assertTrue(jsonSerialized.contains("\"status\":\"started\""));

	}
}
