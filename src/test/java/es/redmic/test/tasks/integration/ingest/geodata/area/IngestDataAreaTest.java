package es.redmic.test.tasks.integration.ingest.geodata.area;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.redmic.db.geodata.area.service.AreaService;
import es.redmic.models.es.maintenance.areas.dto.AreaTypeDTO;
import es.redmic.tasks.ingest.data.document.repository.IngestDataDocumentRepository;
import es.redmic.tasks.ingest.model.geodata.area.dto.RunTaskIngestDataAreaDTO;
import es.redmic.tasks.ingest.model.matching.area.dto.AreaMatching;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemAreaTypeDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemCodeNullableDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemNameDTO;
import es.redmic.test.tasks.integration.ingest.common.IngestBaseTest;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = { "schema.registry.port=18083" })
public class IngestDataAreaTest extends IngestBaseTest {

	@MockBean
	AreaService areaService;

	@Autowired
	IngestDataDocumentRepository repository;

	// @formatter:off
	
	final String WEBSOCKET_URI = "ws://localhost:{port}/api/tasks/socket",
			WEBSOCKET_SEND_RUN_TASK = "/tasks/ingest/area/run",
			WEBSOCKET_SEND_MATCHING = "/tasks/ingest/area/resume/",
			FILE_RESOURCES = "/data/shapefile/area/",
			FILENAME = "areas.zip",
			TASK_NAME = "ingest-data-area",
			ACTIVITY_ID = "activityId";

	// @formatter:on

	String FILENAME_RESULT;

	@Value("${property.path.media_storage.temp.INGEST_DATA}")
	String MEDIASTORAGE_TEMP_INGEST_DATA;

	@Value("${broker.topic.task.ingest.area.run}")
	String INGEST_AREA_RUN_TOPIC;

	@Value("${broker.topic.task.ingest.area.resume}")
	String INGEST_AREA_RESUME_TOPIC;

	@Before
	public void before() throws IOException {

		when(areaService.save(any())).thenReturn(null);
		FILENAME_RESULT = copyResourceToMediaStorage(FILE_RESOURCES, FILENAME, MEDIASTORAGE_TEMP_INGEST_DATA);
	}

	@Override
	@Test
	public void runIngestData_ReturnStatusMsg_IfBrokerIsListen() throws Exception {

		super.runIngestData_ReturnStatusMsg_IfBrokerIsListen();
	}

	@Override
	protected RunTaskIngestDataAreaDTO createRunTaskRequest() {

		RunTaskIngestDataAreaDTO runTask = new RunTaskIngestDataAreaDTO();
		runTask.setTaskName(TASK_NAME);
		runTask.setUserId(USER_ID);
		runTask.getParameters().setFileName(FILENAME_RESULT);
		runTask.getParameters().setActivityId(ACTIVITY_ID);

		return runTask;
	}

	@Override
	protected AreaMatching createMatchingTaskRequest() {
		// [MultiPolygon, SITE_CODE, SITE_NAME, HECTAREAS, AC]
		AreaMatching matching = new AreaMatching();

		ItemAreaTypeDTO areaTypeItem = new ItemAreaTypeDTO();

		AreaTypeDTO areaType = new AreaTypeDTO();
		areaType.setId(8L);
		areaTypeItem.setAreaType(areaType);
		matching.setAreaType(areaTypeItem);

		ItemNameDTO name = new ItemNameDTO();
		name.setColumns(Arrays.asList("SITE_NAME"));
		matching.setName(name);

		ItemCodeNullableDTO code = new ItemCodeNullableDTO();
		code.setColumns(Arrays.asList("SITE_CODE"));
		matching.setCode(code);

		return matching;
	}

	@Override
	protected String getRunTopic() {
		return INGEST_AREA_RUN_TOPIC;
	}

	@Override
	protected String getResumeTopic() {
		return INGEST_AREA_RESUME_TOPIC;
	}
}
