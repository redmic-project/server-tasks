package es.redmic.test.tasks.unit.job.ingest.status;

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
