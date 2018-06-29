package es.redmic.test.tasks.unit.mapper;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

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
