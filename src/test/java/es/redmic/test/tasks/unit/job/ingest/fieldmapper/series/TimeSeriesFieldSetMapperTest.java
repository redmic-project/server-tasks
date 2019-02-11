package es.redmic.test.tasks.unit.job.ingest.fieldmapper.series;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.batch.item.file.transform.FieldSet;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.redmic.models.es.series.timeseries.dto.TimeSeriesDTO;
import es.redmic.tasks.ingest.model.matching.series.timeseries.dto.TimeSeriesMatching;
import es.redmic.tasks.ingest.series.timeseries.jobs.TimeSeriesFieldSetMapper;
import es.redmic.tasks.ingest.series.timeseries.jobs.TimeSeriesParameters;

@RunWith(MockitoJUnitRunner.class)
public class TimeSeriesFieldSetMapperTest {

	private static final String DATE = "Date";
	private static final String DEPTH = "Depth";
	private static final String PAR_TEMP = "Temp";
	private static final String PAR_SP = "SpCond";
	private static final String QFLAG = "Quality";
	private static final String VFLAG = "Validation";
	private static final String REMARKS = "Notas";

	private static final String V_DATE = "2004-12-13T21:39:45.618-08:00";
	private static final String V_DEPTH = "23.61";
	private static final String V_PAR_TEMP = "54.969";
	private static final String V_PAR_SP = "0.258";
	private static final String V_VFLAG = "A";
	private static final String V_QFLAG = "3";
	private static final String V_REMARKS = "Notas";

	private ObjectMapper jacksonMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
			false);

	protected FieldSet createFieldSet() {
		String[] columnNames = new String[] { DATE, DEPTH, PAR_TEMP, PAR_SP, QFLAG, VFLAG, REMARKS },
				tokens = new String[] { V_DATE, V_DEPTH, V_PAR_TEMP, V_PAR_SP, V_QFLAG, V_VFLAG, V_REMARKS };

		return new DefaultFieldSet(tokens, columnNames);
	}

	private TimeSeriesMatching readListResourceJson(String path) throws IOException {

		InputStream resource = getClass().getResource(path).openStream();

		return jacksonMapper.readValue(resource, TimeSeriesMatching.class);
	}

	protected TimeSeriesParameters createConfig() throws IOException {

		TimeSeriesParameters parameters = new TimeSeriesParameters();
		parameters.setActivityId("activityId");
		parameters.setSurveyId("19");
		parameters.setMatching(readListResourceJson("/data/ingest/dto/item/timeseries/multiparametrica-config.json"));
		return parameters;
	}

	protected TimeSeriesDTO expectedDto(String parName) {
		TimeSeriesDTO dto = new TimeSeriesDTO();
		dto.setDate(new DateTime(V_DATE));
		dto.setQFlag(V_QFLAG.charAt(0));
		dto.setVFlag(V_VFLAG.charAt(0));
		dto.setRemark(V_REMARKS);

		Double value = null;
		Long dataDefId = null;
		switch (parName) {
		case PAR_TEMP:
			value = Double.parseDouble(V_PAR_TEMP);
			dataDefId = 1L;
			break;
		case PAR_SP:
			value = Double.parseDouble(V_PAR_SP);
			dataDefId = 2L;
			break;
		case DEPTH:
			value = Double.parseDouble(V_DEPTH);
			dataDefId = 3L;
			break;

		}

		dto.setValue(value);
		dto.setDataDefinition(dataDefId);

		return dto;
	}

	@Test
	public void setterPropertiesTimeSeriesDTOwithBeanUtils()
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		TimeSeriesDTO dtoTemp = new TimeSeriesDTO();

		PropertyUtils.setProperty(dtoTemp, "value", Double.parseDouble("22.22"));
		PropertyUtils.setProperty(dtoTemp, "date", new DateTime(V_DATE));
		PropertyUtils.setProperty(dtoTemp, "VFlag", 'A');
		PropertyUtils.setProperty(dtoTemp, "QFlag", '2');
		PropertyUtils.setProperty(dtoTemp, "dataDefinition", 19L);

		assertEquals(dtoTemp.getDate().getMillisOfSecond(), new DateTime(V_DATE).getMillisOfSecond());
	}

	@Test
	public void parseLineCSVToReturn2BeansTimeSeriesDTOWithEqualsDateTime() throws Exception {
		FieldSet fs = createFieldSet();
		TimeSeriesParameters parameters = createConfig();
		TimeSeriesFieldSetMapper fieldSetMapper = new TimeSeriesFieldSetMapper(parameters);

		List<TimeSeriesDTO> actual = fieldSetMapper.mapFieldSet(fs);

		TimeSeriesDTO expectedDtoDepth = expectedDto(DEPTH), expectedDtoSp = expectedDto(PAR_SP),
				expectedDtoTemp = expectedDto(PAR_TEMP);

		assertEquals(actual.size(), 3);

		assertEquals(actual.get(0).get_grandparentId(), parameters.getActivityId());
		assertEquals(actual.get(0).get_parentId(), parameters.getSurveyId());

		assertEquals(actual.get(0).getDate().getMillisOfSecond(), new DateTime(V_DATE).getMillisOfSecond());
		assertEquals(actual.get(0).getDate().getMillisOfSecond(), actual.get(1).getDate().getMillisOfSecond());
		assertEquals(actual.get(0).getDate().getMillisOfSecond(), actual.get(2).getDate().getMillisOfSecond());

		assertTrue(actual.get(0).getDataDefinition() == 1L);
		assertTrue(actual.get(1).getDataDefinition() == 2L);
		assertTrue(actual.get(2).getDataDefinition() == 3L);

		assertEquals(actual.get(0), expectedDtoTemp);
		assertEquals(actual.get(1), expectedDtoSp);
		assertEquals(actual.get(2), expectedDtoDepth);

	}
}