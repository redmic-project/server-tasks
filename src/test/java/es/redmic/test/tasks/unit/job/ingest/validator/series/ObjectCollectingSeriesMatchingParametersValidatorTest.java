package es.redmic.test.tasks.unit.job.ingest.validator.series;

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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.redmic.es.geodata.geofixedstation.service.GeoFixedObjectCollectingSeriesESService;
import es.redmic.es.maintenance.objects.service.ObjectTypeESService;
import es.redmic.exception.tasks.ingest.IngestMatchingClassificationException;
import es.redmic.exception.tasks.ingest.IngestMatchingColumnRequiredException;
import es.redmic.exception.tasks.ingest.IngestMatchingColumnSizeException;
import es.redmic.exception.tasks.ingest.IngestMatchingDataDefinitionException;
import es.redmic.exception.tasks.ingest.MatchingDataBindingException;
import es.redmic.models.es.geojson.common.model.GeoLineStringData;
import es.redmic.tasks.ingest.model.matching.series.objectcollectingseries.dto.ObjectCollectingSeriesMatching;
import es.redmic.tasks.ingest.series.objectcollectingseries.jobs.ObjectCollectingSeriesMatchingParametersValidator;
import es.redmic.tasks.ingest.series.objectcollectingseries.jobs.ObjectCollectingSeriesParameters;
import es.redmic.test.tasks.utils.mapper.JsonToBeanTestUtil;

@RunWith(MockitoJUnitRunner.class)
public class ObjectCollectingSeriesMatchingParametersValidatorTest {

	final String PATH_MATCHING = "/data/ingest/dto/matching/objectcollectingseries/";
	final String PATH_DATA = "/data/csv/objectcollectingseries/";

	@Mock
	ObjectMapper jacksonMapper;

	@Mock
	GeoFixedObjectCollectingSeriesESService geoFixedObjectCollectingSeriesESService;

	@Mock
	ObjectTypeESService objectTypeESService;

	@InjectMocks
	ObjectCollectingSeriesMatchingParametersValidator validator;

	@Before
	public void before() throws JsonParseException, JsonMappingException, IOException {

		TypeReference<GeoLineStringData> type = new TypeReference<GeoLineStringData>() {
		};
		GeoLineStringData surveyObjectCollectingSeries = JsonToBeanTestUtil
				.getBean("/data/series/geoFixedObjectCollectingSeries.json", type);
		when(geoFixedObjectCollectingSeriesESService.findById(anyString(), anyString()))
				.thenReturn(surveyObjectCollectingSeries);

		/*-ObjectClassificationDTO classification1 = (ObjectClassificationDTO) JsonToBeanTestUtil
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
		when(objectTypeESService.getObjectClassification("4")).thenReturn(classification4);-*/

		Whitebox.setInternalState(validator, "objectTypeESService", objectTypeESService);
	}

	@Test
	public void checkConstraints_ReturnSuccess_IfDataIsCorrect() throws Exception {

		ObjectCollectingSeriesParameters parameters = completeTimeSeriesParameters(PATH_MATCHING + "job-01.json",
				PATH_DATA + "job-01.csv");

		Whitebox.<Boolean>invokeMethod(validator, "checkConstraints", parameters);
	}

	@Test(expected = IngestMatchingColumnSizeException.class)
	public void checkHeader_ThrowIngestMatchinException_IfExistMoreMatchingThanHeaderFields() throws Exception {

		ObjectCollectingSeriesParameters parameters = completeTimeSeriesParameters(PATH_MATCHING + "job-02.json",
				PATH_DATA + "job-02.csv");
		Whitebox.<Boolean>invokeMethod(validator, "checkHeader", parameters.getTaskId(), parameters.getHeader(),
				parameters.getMatching().getMatchingColumns());
	}

	@SuppressWarnings("serial")
	@Test(expected = IngestMatchingDataDefinitionException.class)
	public void checkDataDefinitionConstraints_ThrowIngestMatchinException_IfDataDefinitionNotExistInSurvey()
			throws Exception {

		ObjectCollectingSeriesParameters parameters = completeTimeSeriesParameters(PATH_MATCHING + "job-01b.json",
				PATH_DATA + "job-01.csv");

		List<String> dataDefinitionIds = new ArrayList<String>() {
			{
				add(parameters.getMatching().getParameter().getDataDefinitionId().toString());
			}
		};

		Whitebox.<Boolean>invokeMethod(validator, "checkDataDefinitionConstraints", parameters.getTaskId(),
				dataDefinitionIds, parameters.getSurveyId(), parameters.getActivityId());
	}

	@Test(expected = MatchingDataBindingException.class)
	public void checkDataBindingConstraints_ThrowIngestMatchinException_IfParametersIsNotComplete() throws Exception {

		ObjectCollectingSeriesParameters parameters = completeTimeSeriesParameters(PATH_MATCHING + "job-01c.json",
				PATH_DATA + "job-01.csv");
		parameters.setActivityId(null);
		Whitebox.<Boolean>invokeMethod(validator, "checkDataBindingConstraints", parameters);
	}

	@Test(expected = IngestMatchingColumnRequiredException.class)
	public void checkFileConstraints_ThrowIngestMatchinException_IfInFileNotExistRequiredHeader() throws Exception {

		ObjectCollectingSeriesParameters parameters = completeTimeSeriesParameters(PATH_MATCHING + "job-01.json",
				PATH_DATA + "job-01.csv");
		parameters.getHeader().remove(2);
		Whitebox.<Boolean>invokeMethod(validator, "checkFileConstraints", parameters.getTaskId(),
				parameters.getHeader(), parameters.getMatching().getMatchingColumns());
	}

	@Test(expected = IngestMatchingClassificationException.class)
	public void checkDataConstraints_ThrowIngestMatchinException_ClassificationIsNotSpecified() throws Exception {

		ObjectCollectingSeriesParameters parameters = completeTimeSeriesParameters(PATH_MATCHING + "job-01d.json",
				PATH_DATA + "job-01.csv");
		Whitebox.<Boolean>invokeMethod(validator, "checkDataConstraints", parameters);
	}

	private ObjectCollectingSeriesParameters completeTimeSeriesParameters(String matchingPath, String csvPath)
			throws IOException {

		ObjectCollectingSeriesParameters parameters = new ObjectCollectingSeriesParameters();

		ObjectCollectingSeriesMatching matching = (ObjectCollectingSeriesMatching) JsonToBeanTestUtil
				.getBean(matchingPath, ObjectCollectingSeriesMatching.class);
		String file = getClass().getResource(csvPath).getFile();
		int index = file.lastIndexOf("/");
		String filename = file.substring(index + 1);

		parameters.setMatching(matching);
		parameters.setFileName(filename);

		List<String> header = new ArrayList<String>();
		header.add("Fecha");
		header.add("TrashType");
		if (!filename.equals("job-02.csv"))
			header.add("TrashOrigin");

		parameters.setHeader(header);

		parameters.setActivityId("1");
		parameters.setSurveyId("2");
		parameters.setDelimiter(";");
		parameters.setTaskId("33");
		parameters.setUserId("233");

		return parameters;
	}
}
