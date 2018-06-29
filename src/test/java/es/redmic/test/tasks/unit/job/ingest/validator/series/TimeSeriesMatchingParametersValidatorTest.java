package es.redmic.test.tasks.unit.job.ingest.validator.series;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.redmic.es.geodata.geofixedstation.service.GeoFixedTimeSeriesESService;
import es.redmic.exception.tasks.ingest.IngestMatchingColumnRequiredException;
import es.redmic.exception.tasks.ingest.IngestMatchingColumnSizeException;
import es.redmic.exception.tasks.ingest.IngestMatchingDataDefinitionException;
import es.redmic.exception.tasks.ingest.MatchingDataBindingException;
import es.redmic.models.es.geojson.common.model.GeoPointData;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemParameterDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ParametersDTO;
import es.redmic.tasks.ingest.model.matching.series.timeseries.dto.TimeSeriesMatching;
import es.redmic.tasks.ingest.series.timeseries.jobs.TimeSeriesMatchingParametersValidator;
import es.redmic.tasks.ingest.series.timeseries.jobs.TimeSeriesParameters;
import es.redmic.test.tasks.utils.mapper.JsonToBeanTestUtil;

@RunWith(MockitoJUnitRunner.class)
public class TimeSeriesMatchingParametersValidatorTest {

	final String PATH_MATCHING = "/data/ingest/dto/matching/timeseries/";
	final String PATH_DATA = "/data/csv/timeseries/";
	@Mock
	ObjectMapper jacksonMapper;

	@Mock
	GeoFixedTimeSeriesESService geoFixedTimeSeriesESService;

	@InjectMocks
	TimeSeriesMatchingParametersValidator validator;

	@Before
	public void before() throws JsonParseException, JsonMappingException, IOException {

		TypeReference<GeoPointData> type = new TypeReference<GeoPointData>() {
		};
		GeoPointData surveyTimeSeries = JsonToBeanTestUtil.getBean("/data/series/geoFixedTimeSeries.json", type);
		when(geoFixedTimeSeriesESService.findById(anyString(), anyString())).thenReturn(surveyTimeSeries);
	}

	@Test
	public void checkConstraints_ReturnSuccess_IfDataIsCorrect() throws Exception {

		TimeSeriesParameters parameters = completeTimeSeriesParameters(PATH_MATCHING + "job-01.json",
				PATH_DATA + "job-01.csv");
		;

		Whitebox.<Boolean>invokeMethod(validator, "checkConstraints", parameters);
	}

	@Test(expected = IngestMatchingColumnSizeException.class)
	public void checkHeader_ThrowIngestMatchinException_IfExistMoreMatchingThanHeaderFields() throws Exception {

		TimeSeriesParameters parameters = completeTimeSeriesParameters(PATH_MATCHING + "job-02.json",
				PATH_DATA + "job-02.csv");
		Whitebox.<Boolean>invokeMethod(validator, "checkHeader", parameters.getTaskId(), parameters.getHeader(),
				parameters.getMatching().getMatchingColumns());
	}

	@SuppressWarnings("serial")
	@Test(expected = IngestMatchingDataDefinitionException.class)
	public void checkDataDefinitionConstraints_ThrowIngestMatchinException_IfDataDefinitionNotExistInSurvey()
			throws Exception {

		TimeSeriesParameters parameters = completeTimeSeriesParameters(PATH_MATCHING + "job-01b.json",
				PATH_DATA + "job-01.csv");

		List<String> dataDefinitionIds = new ArrayList<String>() {
			{

				ParametersDTO parametersMatching = parameters.getMatching().getParameters();
				for (ItemParameterDTO item : parametersMatching.getMatching())
					add(item.getDataDefinitionId().toString());
			}
		};
		Whitebox.<Boolean>invokeMethod(validator, "checkDataDefinitionConstraints", parameters.getTaskId(),
				dataDefinitionIds, parameters.getSurveyId(), parameters.getActivityId());
	}

	@Test(expected = MatchingDataBindingException.class)
	public void checkDataBindingConstraints_ThrowIngestMatchinException_IfParametersIsNotComplete() throws Exception {

		TimeSeriesParameters parameters = completeTimeSeriesParameters(PATH_MATCHING + "job-01c.json",
				PATH_DATA + "job-01.csv");
		parameters.setActivityId(null);
		Whitebox.<Boolean>invokeMethod(validator, "checkDataBindingConstraints", parameters);
	}

	@Test(expected = IngestMatchingColumnRequiredException.class)
	public void checkFileConstraints_ThrowIngestMatchinException_IfInFileNotExistRequiredHeader() throws Exception {

		TimeSeriesParameters parameters = completeTimeSeriesParameters(PATH_MATCHING + "job-01.json",
				PATH_DATA + "job-01.csv");
		parameters.getHeader().remove(1);
		Whitebox.<Boolean>invokeMethod(validator, "checkFileConstraints", parameters.getTaskId(),
				parameters.getHeader(), parameters.getMatching().getMatchingColumns());
	}

	private TimeSeriesParameters completeTimeSeriesParameters(String matchingPath, String csvPath) throws IOException {

		TimeSeriesParameters parameters = new TimeSeriesParameters();
		List<String> header = new ArrayList<String>();
		header.add("Fecha-Hora");
		header.add("Turbidez");

		TimeSeriesMatching matching = (TimeSeriesMatching) JsonToBeanTestUtil.getBean(matchingPath,
				TimeSeriesMatching.class);
		String file = getClass().getResource(csvPath).getFile();
		int index = file.lastIndexOf("/");
		String filename = file.substring(index + 1);

		parameters.setMatching(matching);
		parameters.setFileName(filename);

		parameters.setHeader(header);

		parameters.setActivityId("1");
		parameters.setSurveyId("2");
		parameters.setDelimiter(";");
		parameters.setTaskId("33");
		parameters.setUserId("233");
		return parameters;
	}
}
