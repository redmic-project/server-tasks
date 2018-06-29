package es.redmic.test.tasks.integration.ingest.series.objectcollectingseries;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.type.TypeReference;

import es.redmic.db.series.objectcollecting.service.ObjectCollectingSeriesService;
import es.redmic.es.geodata.geofixedstation.service.GeoFixedObjectCollectingSeriesESService;
import es.redmic.es.maintenance.objects.service.ObjectTypeESService;
import es.redmic.models.es.geojson.common.model.GeoLineStringData;
import es.redmic.models.es.maintenance.objects.dto.ObjectClassificationDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ClassificationDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemClassificationDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemDateDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ParameterDTO;
import es.redmic.tasks.ingest.model.matching.series.objectcollectingseries.dto.ObjectCollectingSeriesMatching;
import es.redmic.tasks.ingest.model.series.dto.RunTaskIngestDataSeriesDTO;
import es.redmic.tasks.ingest.series.objectcollectingseries.repository.IngestObjectCollectingSeriesRepository;
import es.redmic.test.tasks.integration.ingest.common.IngestBaseTest;
import es.redmic.test.tasks.utils.mapper.JsonToBeanTestUtil;

@RunWith(SpringJUnit4ClassRunner.class)
public class IngestDataObjectCollectingSeriesTest extends IngestBaseTest {

	@MockBean
	GeoFixedObjectCollectingSeriesESService geoFixedObjectCollectingSeriesESService;

	@MockBean
	ObjectCollectingSeriesService objectCollectingSeriesService;

	@MockBean
	ObjectTypeESService objectTypeESService;

	@SpyBean
	IngestObjectCollectingSeriesRepository repository;

	// @formatter:off
	
	final String WEBSOCKET_URI = "ws://localhost:{port}/api/tasks/socket",
			WEBSOCKET_SEND_RUN_TASK = "/tasks/ingest/objectcollectingseries/run",
			WEBSOCKET_SEND_MATCHING = "/tasks/ingest/objectcollectingseries/resume/",
			CSV_RESOURCES = "/data/csv/objectcollectingseries/",
			FILENAME_CSV = "job-01.csv",
			TASK_NAME = "ingest-data-objectcollectingseries",
			ACTIVITY_ID = "activityId",
			SURVEY_ID = "19",
			DELIMITER = ";";
	
	// @formatter:on

	String FILENAME_RESULT;

	@Value("${property.path.media_storage.temp.INGEST_DATA}")
	String MEDIASTORAGE_TEMP_SERIES;

	@Value("${broker.topic.task.ingest.objectcollectingseries.run}")
	String INGEST_OBJECTCOLLECTINGSERIES_RUN_TOPIC;

	@Value("${broker.topic.task.ingest.objectcollectingseries.resume}")
	String INGEST_OBJECTCOLLECTINGSERIES_RESUME_TOPIC;

	@Before
	public void before() throws IOException {

		TypeReference<GeoLineStringData> type = new TypeReference<GeoLineStringData>() {
		};
		GeoLineStringData surveyObjectCollectingSeries = JsonToBeanTestUtil
				.getBean("/data/series/geoFixedObjectCollectingSeries.json", type);
		when(geoFixedObjectCollectingSeriesESService.findById(anyString(), anyString()))
				.thenReturn(surveyObjectCollectingSeries);
		when(objectCollectingSeriesService.save(any())).thenReturn(null);
		FILENAME_RESULT = copyResourceToMediaStorage(CSV_RESOURCES, FILENAME_CSV, MEDIASTORAGE_TEMP_SERIES);

		ObjectClassificationDTO classification1 = (ObjectClassificationDTO) JsonToBeanTestUtil
				.getBean("/data/ingest/dto/item/objectcollectingseries/category1.json", ObjectClassificationDTO.class);
		when(objectTypeESService.getObjectClassification("1")).thenReturn(classification1);

		ObjectClassificationDTO classification2 = (ObjectClassificationDTO) JsonToBeanTestUtil
				.getBean("/data/ingest/dto/item/objectcollectingseries/category2.json", ObjectClassificationDTO.class);
		when(objectTypeESService.getObjectClassification("2")).thenReturn(classification2);

		ObjectClassificationDTO classification3 = (ObjectClassificationDTO) JsonToBeanTestUtil
				.getBean("/data/ingest/dto/item/objectcollectingseries/category3.json", ObjectClassificationDTO.class);
		when(objectTypeESService.getObjectClassification("3")).thenReturn(classification3);

		ObjectClassificationDTO classification4 = (ObjectClassificationDTO) JsonToBeanTestUtil
				.getBean("/data/ingest/dto/item/objectcollectingseries/category4.json", ObjectClassificationDTO.class);
		when(objectTypeESService.getObjectClassification("4")).thenReturn(classification4);
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
	protected ObjectCollectingSeriesMatching createMatchingTaskRequest() {

		ObjectCollectingSeriesMatching matching = new ObjectCollectingSeriesMatching();

		ItemDateDTO date = new ItemDateDTO();
		date.setColumns(Arrays.asList("Fecha"));
		date.setFormat("dd-MM-yyyy");

		ParameterDTO parameter = new ParameterDTO();
		parameter.setDataDefinitionId(10L);

		ItemClassificationDTO classification1 = new ItemClassificationDTO();
		classification1.setColumns(new ArrayList<String>() {
			{
				add("TrashType");
			}
		});
		classification1.setClassifications(new ArrayList<String>() {
			{
				add("root.1");
				add("root.2");
			}
		});

		ItemClassificationDTO classification2 = new ItemClassificationDTO();
		classification2.setColumns(new ArrayList<String>() {
			{
				add("TrashOrigin");
			}
		});
		classification2.setClassifications(new ArrayList<String>() {
			{
				add("root.3");
				add("root.4");
			}
		});

		ClassificationDTO classifications = new ClassificationDTO();
		classifications.setMatching(new ArrayList<ItemClassificationDTO>() {
			{
				add(classification1);
				add(classification2);
			}
		});

		matching.setDate(date);
		matching.setParameter(parameter);
		matching.setClassifications(classifications);

		return matching;
	}

	@Override
	protected String getRunTopic() {
		return INGEST_OBJECTCOLLECTINGSERIES_RUN_TOPIC;
	}

	@Override
	protected String getResumeTopic() {
		return INGEST_OBJECTCOLLECTINGSERIES_RESUME_TOPIC;
	}
}