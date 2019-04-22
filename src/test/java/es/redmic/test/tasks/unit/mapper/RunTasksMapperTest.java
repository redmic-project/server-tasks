package es.redmic.test.tasks.unit.mapper;

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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import es.redmic.tasks.ingest.model.status.dto.StartedTaskDTO;
import es.redmic.tasks.ingest.model.status.dto.UserTaskDTO;
import es.redmic.tasks.ingest.model.status.model.UserTasks;
import es.redmic.test.tasks.utils.mapper.JsonToBeanTestUtil;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@RunWith(MockitoJUnitRunner.class)
public class RunTasksMapperTest {

	MapperFactory factory = new DefaultMapperFactory.Builder().build();

	@Before
	public void setupTest() throws IOException {

		// @formatter:off
		
		factory.classMap(UserTasks.class, UserTaskDTO.class)
			.byDefault()
				.register();
		
		factory.getConverterFactory()
			.registerConverter(new PassThroughConverter(org.joda.time.DateTime.class));

		// @formatter:on
	}

	@Test
	public void mapperStartingTaskDTOToUserTasksReturnOK()
			throws JsonParseException, JsonMappingException, IOException {
		String dtoInPath = "/data/tasks/status/dto/StartedTask.json";
		String modelInPath = "/data/tasks/status/model/StartedTask.json";

		StartedTaskDTO dtoIn = (StartedTaskDTO) JsonToBeanTestUtil.getBean(dtoInPath, StartedTaskDTO.class);
		UserTasks modelIn = (UserTasks) JsonToBeanTestUtil.getBean(modelInPath, UserTasks.class);
		UserTasks modelOut = factory.getMapperFacade().map(dtoIn, UserTasks.class);

		assertTrue(modelOut.equals(modelIn));
	}
}
