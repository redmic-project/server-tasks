package es.redmic.test.tasks.integration.ingest.geodata.tracking;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

import es.redmic.db.geodata.tracking.platform.service.PlatformTrackingService;
import es.redmic.es.administrative.service.PlatformESService;
import es.redmic.models.es.administrative.dto.PlatformDTO;
import es.redmic.models.es.maintenance.device.dto.DeviceDTO;
import es.redmic.tasks.ingest.geodata.tracking.repository.IngestDataTrackingRepository;
import es.redmic.tasks.ingest.model.geodata.tracking.dto.RunTaskIngestDataTrackingDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemDateDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemDeviceDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemPointGeometryDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemQFlagDTO;
import es.redmic.tasks.ingest.model.matching.tracking.dto.TrackingMatching;
import es.redmic.test.tasks.integration.ingest.common.IngestBaseTest;
import es.redmic.test.tasks.utils.mapper.JsonToBeanTestUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = { "schema.registry.port=18084" })
public class IngestDataTrackingTest extends IngestBaseTest {

	@MockBean
	PlatformTrackingService platformTrackingService;

	@MockBean
	PlatformESService platformESService;

	@Autowired
	IngestDataTrackingRepository repository;

	// @formatter:off

	final String WEBSOCKET_URI = "ws://localhost:{port}/api/tasks/socket",
			WEBSOCKET_SEND_RUN_TASK = "/tasks/ingest/tracking/run",
			WEBSOCKET_SEND_MATCHING = "/tasks/ingest/tracking/resume/",
			CSV_RESOURCES = "/data/csv/tracking/",
			FILENAME_CSV = "job-01.csv",
			TASK_NAME = "ingest-data-tracking",
			ACTIVITY_ID = "activityId",
			ELEMENT_UUID = "qwerty",
			DELIMITER = ";";
	
	// @formatter:on

	String FILENAME_RESULT;

	@Value("${property.path.media_storage.temp.INGEST_DATA}")
	String MEDIASTORAGE_TEMP_INGEST_DATA;

	@Value("${broker.topic.task.ingest.tracking.run}")
	String INGEST_TRACKING_RUN_TOPIC;

	@Value("${broker.topic.task.ingest.tracking.resume}")
	String INGEST_TRACKING_RESUME_TOPIC;

	@Before
	public void before() throws IOException {

		PlatformDTO platform = (PlatformDTO) JsonToBeanTestUtil.getBean("/data/ingest/dto/item/tracking/platform.json",
				PlatformDTO.class);
		when(platformESService.findByUuid(anyString())).thenReturn(platform);

		when(platformTrackingService.save(any())).thenReturn(null);
		FILENAME_RESULT = copyResourceToMediaStorage(CSV_RESOURCES, FILENAME_CSV, MEDIASTORAGE_TEMP_INGEST_DATA);
	}

	@Override
	@Test
	public void runIngestData_ReturnStatusMsg_IfBrokerIsListen() throws Exception {
		super.runIngestData_ReturnStatusMsg_IfBrokerIsListen();
	}

	@Override
	protected RunTaskIngestDataTrackingDTO createRunTaskRequest() {

		RunTaskIngestDataTrackingDTO runTask = new RunTaskIngestDataTrackingDTO();
		runTask.setTaskName(TASK_NAME);
		runTask.getParameters().setFileName(FILENAME_RESULT);
		runTask.getParameters().setDelimiter(";");
		runTask.getParameters().setActivityId("activityId");
		runTask.getParameters().setElementUuid("qwerty");

		return runTask;
	}

	@Override
	protected TrackingMatching createMatchingTaskRequest() {

		TrackingMatching matching = new TrackingMatching();

		ItemDateDTO date = new ItemDateDTO();
		date.setColumns(Arrays.asList("Fecha-Hora"));
		date.setFormat("yyyy-MM-dd HH:mm:ssZ");

		ItemPointGeometryDTO geometry = new ItemPointGeometryDTO();
		geometry.setColumns(Arrays.asList("latitud", "longitud"));

		ItemDeviceDTO device = new ItemDeviceDTO();
		DeviceDTO deviceDTO = new DeviceDTO();
		deviceDTO.setId(19L);
		device.setDevice(deviceDTO);

		ItemQFlagDTO qFlag = new ItemQFlagDTO();
		qFlag.setColumns(Arrays.asList("qflag"));

		matching.setDate(date);
		matching.setPointGeometry(geometry);
		matching.setDevice(device);
		matching.setQFlag(qFlag);

		return matching;
	}

	@Override
	protected String getRunTopic() {
		return INGEST_TRACKING_RUN_TOPIC;
	}

	@Override
	protected String getResumeTopic() {
		return INGEST_TRACKING_RESUME_TOPIC;
	}
}
