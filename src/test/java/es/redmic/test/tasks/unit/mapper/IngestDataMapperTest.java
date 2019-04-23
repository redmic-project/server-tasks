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

import es.redmic.tasks.ingest.common.mapper.InterventionMatchingESMapper;
import es.redmic.tasks.ingest.model.intervention.matching.InterventionMatching;
import es.redmic.tasks.ingest.model.intervention.matching.RequestUserInterventionMatchingTaskDTO;
import es.redmic.tasks.ingest.model.status.model.UserTasks;
import es.redmic.test.tasks.utils.mapper.JsonToBeanTestUtil;
import es.redmic.test.tasks.utils.mapper.OrikaScanBeanTest;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;

@RunWith(MockitoJUnitRunner.class)
public class IngestDataMapperTest {

	OrikaScanBeanTest orikaMapper = new OrikaScanBeanTest();

	@Before
	public void setupTest() throws IOException {
		InterventionMatchingESMapper mapper = new InterventionMatchingESMapper();

		orikaMapper.addConverter(new PassThroughConverter(InterventionMatching.class));
		orikaMapper.addMapper(mapper);
	}

	@Test
	public void mapperStartingTaskDTOToUserTasksReturnOK()
			throws JsonParseException, JsonMappingException, IOException {
		String modelInPath = "/data/task/model/Intervention.json";

		UserTasks model = (UserTasks) JsonToBeanTestUtil.getBean(modelInPath, UserTasks.class);
		RequestUserInterventionMatchingTaskDTO dto = orikaMapper.getMapperFacade().map(model,
				RequestUserInterventionMatchingTaskDTO.class);

		assertTrue(dto.getInterventionDescription() != null);
		assertTrue(dto.getInterventionDescription().getData().size() == 1);
		assertTrue(dto.getInterventionDescription().getData().get(0).containsKey("date"));
		assertTrue(dto.getInterventionDescription().getData().get(0).containsKey("value"));
	}

}
