package es.redmic.test.tasks.unit.job.ingest.validator.geodata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.redmic.exception.tasks.ingest.IngestMatchingColumnSizeException;
import es.redmic.exception.tasks.ingest.MatchingDataBindingException;
import es.redmic.tasks.ingest.geodata.area.jobs.AreaMatchingParametersValidator;
import es.redmic.tasks.ingest.geodata.area.jobs.AreaParameters;
import es.redmic.tasks.ingest.model.matching.area.dto.AreaMatching;
import es.redmic.test.tasks.utils.mapper.JsonToBeanTestUtil;

@RunWith(MockitoJUnitRunner.class)
public class AreaMatchingParametersValidatorTest {

	final String PATH_MATCHING = "/data/ingest/dto/matching/area/";

	@Mock
	ObjectMapper jacksonMapper;

	@InjectMocks
	AreaMatchingParametersValidator validator;

	@Test
	public void checkConstraints_ReturnSuccess_IfDataIsCorrect() throws Exception {

		AreaParameters parameters = completeTimeSeriesParameters(PATH_MATCHING + "job-01.json");

		Whitebox.<Boolean>invokeMethod(validator, "checkConstraints", parameters);
	}

	@Test(expected = IngestMatchingColumnSizeException.class)
	public void checkHeader_ThrowIngestMatchinException_IfExistMoreMatchingThanHeaderFields() throws Exception {

		AreaParameters parameters = completeTimeSeriesParameters(PATH_MATCHING + "job-01.json");
		parameters.getHeader().remove(0);
		Whitebox.<Boolean>invokeMethod(validator, "checkHeader", parameters.getTaskId(), parameters.getHeader(),
				parameters.getMatching().getMatchingColumns());
	}

	@Test(expected = MatchingDataBindingException.class)
	public void checkDataBindingConstraints_ThrowIngestMatchinException_IfParametersIsNotComplete() throws Exception {

		AreaParameters parameters = completeTimeSeriesParameters(PATH_MATCHING + "job-01c.json");

		Whitebox.<Boolean>invokeMethod(validator, "checkDataBindingConstraints", parameters);
	}

	private AreaParameters completeTimeSeriesParameters(String matchingPath) throws IOException {

		AreaParameters parameters = new AreaParameters();

		AreaMatching matching = (AreaMatching) JsonToBeanTestUtil.getBean(matchingPath, AreaMatching.class);

		parameters.setMatching(matching);
		parameters.setFileName("file");

		List<String> header = new ArrayList<String>();
		header.add("SITE_NAME");
		header.add("SITE_CODE");
		header.add("SITE_DESCRIPTION");
		header.add("SITE_REMARK");

		parameters.setHeader(header);

		parameters.setActivityId("1");
		parameters.setTaskId("33");
		parameters.setUserId("233");

		return parameters;
	}
}
