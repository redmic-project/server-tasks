package es.redmic.test.tasks.unit.job.ingest.validator.geodata;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import es.redmic.exception.tasks.ingest.IngestMatchingColumnRequiredException;
import es.redmic.exception.tasks.ingest.IngestMatchingColumnSizeException;
import es.redmic.exception.tasks.ingest.MatchingDataBindingException;
import es.redmic.models.es.administrative.dto.PlatformDTO;
import es.redmic.tasks.ingest.geodata.tracking.jobs.TrackingMatchingParametersValidator;
import es.redmic.tasks.ingest.geodata.tracking.jobs.TrackingParameters;
import es.redmic.tasks.ingest.model.matching.tracking.dto.TrackingMatching;
import es.redmic.test.tasks.utils.mapper.JsonToBeanTestUtil;

@RunWith(MockitoJUnitRunner.class)
public class TrackingMatchingParametersValidatorTest {

	final String PATH_MATCHING = "/data/ingest/dto/matching/tracking/";
	final String PATH_DATA = "/data/csv/tracking/";

	@Mock
	ObjectMapper jacksonMapper;

	@InjectMocks
	TrackingMatchingParametersValidator validator;

	PlatformDTO platformDTO;

	@Before
	public void before() throws IOException {
		platformDTO = (PlatformDTO) JsonToBeanTestUtil.getBean("/data/ingest/dto/item/tracking/platform.json",
				PlatformDTO.class);
	}

	@Test
	public void checkConstraints_ReturnSuccess_IfDataIsCorrect() throws Exception {

		TrackingParameters parameters = completeTimeSeriesParameters(PATH_MATCHING + "job-01.json",
				PATH_DATA + "job-01.csv");
		;
		parameters.getHeader().add("qflag");
		parameters.getHeader().add("vflag");
		Whitebox.<Boolean>invokeMethod(validator, "checkConstraints", parameters);
	}

	@Test(expected = IngestMatchingColumnSizeException.class)
	public void checkHeader_ThrowIngestMatchinException_IfExistMoreMatchingThanHeaderFields() throws Exception {

		TrackingParameters parameters = completeTimeSeriesParameters(PATH_MATCHING + "job-02.json",
				PATH_DATA + "job-02.csv");
		Whitebox.<Boolean>invokeMethod(validator, "checkHeader", parameters.getTaskId(), parameters.getHeader(),
				parameters.getMatching().getMatchingColumns());
	}

	@Test(expected = MatchingDataBindingException.class)
	public void checkDataBindingConstraints_ThrowIngestMatchinException_IfParametersIsNotComplete() throws Exception {

		TrackingParameters parameters = completeTimeSeriesParameters(PATH_MATCHING + "job-01c.json",
				PATH_DATA + "job-01.csv");
		parameters.setActivityId(null);
		Whitebox.<Boolean>invokeMethod(validator, "checkDataBindingConstraints", parameters);
	}

	@Test(expected = IngestMatchingColumnRequiredException.class)
	public void checkFileConstraints_ThrowIngestMatchinException_IfInFileNotExistRequiredHeader() throws Exception {

		TrackingParameters parameters = completeTimeSeriesParameters(PATH_MATCHING + "job-01.json",
				PATH_DATA + "job-01.csv");
		parameters.getHeader().remove(2);
		Whitebox.<Boolean>invokeMethod(validator, "checkFileConstraints", parameters.getTaskId(),
				parameters.getHeader(), parameters.getMatching().getMatchingColumns());
	}

	private TrackingParameters completeTimeSeriesParameters(String matchingPath, String csvPath) throws IOException {

		TrackingParameters parameters = new TrackingParameters();

		TrackingMatching matching = (TrackingMatching) JsonToBeanTestUtil.getBean(matchingPath, TrackingMatching.class);
		String file = getClass().getResource(csvPath).getFile();
		int index = file.lastIndexOf("/");
		String filename = file.substring(index + 1);

		parameters.setMatching(matching);
		parameters.setFileName(filename);

		List<String> header = new ArrayList<String>();
		header.add("Fecha-Hora");
		header.add("longitud");
		header.add("latitud");

		parameters.setHeader(header);

		parameters.setActivityId("1");
		parameters.setPlatformDTO(platformDTO);
		parameters.setDelimiter(";");
		parameters.setTaskId("33");
		parameters.setUserId("233");

		return parameters;
	}
}
