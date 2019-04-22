package es.redmic.test.tasks.integration.ingest.series.timeseries;

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
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.type.TypeReference;

import es.redmic.db.series.timeseries.service.TimeSeriesService;
import es.redmic.es.geodata.geofixedstation.service.GeoFixedTimeSeriesESService;
import es.redmic.models.es.geojson.common.model.GeoPointData;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemDateDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemParameterDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ParametersDTO;
import es.redmic.tasks.ingest.model.matching.series.timeseries.dto.TimeSeriesMatching;
import es.redmic.tasks.ingest.model.series.dto.RunTaskIngestDataSeriesDTO;
import es.redmic.tasks.ingest.series.timeseries.repository.IngestTimeSeriesRepository;
import es.redmic.test.tasks.integration.ingest.common.IngestBaseTest;
import es.redmic.test.tasks.utils.mapper.JsonToBeanTestUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = { "schema.registry.port=18086" })
public class IngestDataTimeSeriesTest extends IngestBaseTest {

	@MockBean
	GeoFixedTimeSeriesESService geoFixedTimeSeriesESService;

	@MockBean
	TimeSeriesService timeSeriesService;

	@Autowired
	IngestTimeSeriesRepository repository;

	// @formatter:off

	final String WEBSOCKET_URI = "ws://localhost:{port}/api/tasks/socket",
			WEBSOCKET_SEND_RUN_TASK = "/tasks/ingest/timeseries/run",
			WEBSOCKET_SEND_MATCHING = "/tasks/ingest/timeseries/resume/",
			CSV_RESOURCES = "/data/csv/timeseries/",
			FILENAME_CSV = "job-01.csv",
			TASK_NAME = "ingest-data-timeseries",
			ACTIVITY_ID = "activityId",
			SURVEY_ID = "19",
			DELIMITER = ";";
	
	// @formatter:on

	String FILENAME_RESULT;

	@Value("${property.path.media_storage.temp.INGEST_DATA}")
	String MEDIASTORAGE_TEMP_SERIES;

	@Value("${broker.topic.task.ingest.timeseries.run}")
	String INGEST_TIMESERIES_RUN_TOPIC;

	@Value("${broker.topic.task.ingest.timeseries.resume}")
	String INGEST_TIMESERIES_RESUME_TOPIC;

	@Before
	public void before() throws IOException {

		TypeReference<GeoPointData> type = new TypeReference<GeoPointData>() {
		};
		GeoPointData surveyTimeSeries = JsonToBeanTestUtil.getBean("/data/series/geoFixedTimeSeries.json", type);
		when(geoFixedTimeSeriesESService.findById(anyString(), anyString())).thenReturn(surveyTimeSeries);
		when(timeSeriesService.save(any())).thenReturn(null);
		FILENAME_RESULT = copyResourceToMediaStorage(CSV_RESOURCES, FILENAME_CSV, MEDIASTORAGE_TEMP_SERIES);
	}

	@Override
	@Test
	public void runIngestData_ReturnStatusMsg_IfBrokerIsListen() throws Exception {
		super.runIngestData_ReturnStatusMsg_IfBrokerIsListen();
	}

	@Override
	protected RunTaskIngestDataSeriesDTO createRunTaskRequest() {

		RunTaskIngestDataSeriesDTO runTask = new RunTaskIngestDataSeriesDTO();
		runTask.setTaskName(TASK_NAME);
		runTask.getParameters().setFileName(FILENAME_RESULT);
		runTask.getParameters().setDelimiter(DELIMITER);
		runTask.getParameters().setActivityId(ACTIVITY_ID);
		runTask.getParameters().setSurveyId(SURVEY_ID);

		return runTask;
	}

	@Override
	@SuppressWarnings("serial")
	protected TimeSeriesMatching createMatchingTaskRequest() {

		TimeSeriesMatching matching = new TimeSeriesMatching();

		ItemDateDTO date = new ItemDateDTO();
		date.setColumns(Arrays.asList("Fecha-Hora"));
		date.setFormat("dd/MM/yyyy HH:mm:ss");

		ItemParameterDTO parameter = new ItemParameterDTO();
		parameter.setColumns(Arrays.asList("Turbidez"));
		parameter.setDataDefinitionId(19L);

		ParametersDTO parameters = new ParametersDTO();
		parameters.setMatching(new ArrayList<ItemParameterDTO>() {
			{
				add(parameter);
			}
		});

		matching.setDate(date);
		matching.setParameters(parameters);

		return matching;
	}

	@Override
	protected String getRunTopic() {
		return INGEST_TIMESERIES_RUN_TOPIC;
	}

	@Override
	protected String getResumeTopic() {
		return INGEST_TIMESERIES_RESUME_TOPIC;
	}
}
