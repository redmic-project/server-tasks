package es.redmic.test.tasks.unit.job.ingest.fieldmapper.series;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.batch.item.file.transform.FieldSet;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.redmic.es.maintenance.objects.service.ObjectTypeESService;
import es.redmic.models.es.maintenance.objects.dto.ObjectClassificationDTO;
import es.redmic.models.es.series.objectcollecting.dto.ObjectCollectingSeriesDTO;
import es.redmic.tasks.ingest.model.matching.series.objectcollectingseries.dto.ObjectCollectingSeriesMatching;
import es.redmic.tasks.ingest.series.objectcollectingseries.jobs.ObjectCollectingSeriesFieldSetMapper;
import es.redmic.tasks.ingest.series.objectcollectingseries.jobs.ObjectCollectingSeriesParameters;
import es.redmic.test.tasks.utils.mapper.JsonToBeanTestUtil;

@RunWith(MockitoJUnitRunner.class)
public class ObjectCollectingSeriesFieldSetMapperTest {

	private static final String DATE = "Date";
	private static final String PAR_CONT = "Cont";
	private static final String CAT_TRASH_TYPE = "TrashType";
	private static final String CAT_TRASH_ORIGIN = "TrashOrigin";
	private static final String QFLAG = "Quality";
	private static final String VFLAG = "Validation";
	private static final String REMARKS = "Notas";

	private static final String V_DATE = "2004-12-13T21:39:45.618-08:00";
	private static final String V_PAR_CONT = "3";
	private static final String V_CAT_TRASH_TYPE = "1";
	private static final String V_CAT_TRASH_ORIGIN = "2";
	private static final String V_VFLAG = "A";
	private static final String V_QFLAG = "3";
	private static final String V_REMARKS = "Notas";

	@Mock
	ObjectTypeESService objectTypeESService;

	private ObjectMapper jacksonMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
			false);

	List<ObjectClassificationDTO> trashTypeClassification;

	List<ObjectClassificationDTO> trashOriginClassification;

	protected FieldSet createFieldSet() {
		String[] columnNames = new String[] { DATE, PAR_CONT, CAT_TRASH_TYPE, CAT_TRASH_ORIGIN, QFLAG, VFLAG, REMARKS };
		String[] tokens = new String[] { V_DATE, V_PAR_CONT, V_CAT_TRASH_TYPE, V_CAT_TRASH_ORIGIN, V_QFLAG, V_VFLAG,
				V_REMARKS };

		return new DefaultFieldSet(tokens, columnNames);
	}

	@Before
	public void setupTest() throws IOException {

		ObjectClassificationDTO classification1 = (ObjectClassificationDTO) JsonToBeanTestUtil
				.getBean("/data/ingest/dto/item/objectcollectingseries/category1.json", ObjectClassificationDTO.class);
		when(objectTypeESService.getObjectClassification(anyString())).thenReturn(classification1);

		ObjectClassificationDTO classification2 = (ObjectClassificationDTO) JsonToBeanTestUtil
				.getBean("/data/ingest/dto/item/objectcollectingseries/category2.json", ObjectClassificationDTO.class);
		when(objectTypeESService.getObjectClassification(anyString())).thenReturn(classification2);

		trashTypeClassification = new ArrayList<ObjectClassificationDTO>();
		trashTypeClassification.add(classification1);
		trashTypeClassification.add(classification2);

		ObjectClassificationDTO classification3 = (ObjectClassificationDTO) JsonToBeanTestUtil
				.getBean("/data/ingest/dto/item/objectcollectingseries/category3.json", ObjectClassificationDTO.class);
		when(objectTypeESService.getObjectClassification(anyString())).thenReturn(classification3);

		ObjectClassificationDTO classification4 = (ObjectClassificationDTO) JsonToBeanTestUtil
				.getBean("/data/ingest/dto/item/objectcollectingseries/category4.json", ObjectClassificationDTO.class);
		when(objectTypeESService.getObjectClassification(anyString())).thenReturn(classification4);

		trashOriginClassification = new ArrayList<ObjectClassificationDTO>();
		trashOriginClassification.add(classification3);
		trashOriginClassification.add(classification4);
	}

	@Test
	public void setterPropertiesTimeSeriesDTOwithBeanUtils()
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		ObjectCollectingSeriesDTO dtoTemp = new ObjectCollectingSeriesDTO();

		PropertyUtils.setProperty(dtoTemp, "value", Double.parseDouble("3"));
		PropertyUtils.setProperty(dtoTemp, "date", new DateTime(V_DATE));
		PropertyUtils.setProperty(dtoTemp, "VFlag", 'A');
		PropertyUtils.setProperty(dtoTemp, "QFlag", '2');
		PropertyUtils.setProperty(dtoTemp, "dataDefinition", 10L);

		assertEquals(dtoTemp.getDate().getMillisOfSecond(), new DateTime(V_DATE).getMillisOfSecond());
	}

	@Test
	public void parseLineCSVToReturn2BeansTimeSeriesDTOWithEqualsDateTime() throws Exception {
		FieldSet fs = createFieldSet();
		ObjectCollectingSeriesParameters parameters = createConfig();
		ObjectCollectingSeriesFieldSetMapper fieldSetMapper = new ObjectCollectingSeriesFieldSetMapper(parameters,
				objectTypeESService);

		List<ObjectCollectingSeriesDTO> actual = fieldSetMapper.mapFieldSet(fs);

		ObjectCollectingSeriesDTO expectedTrashTypeDto = expectedDto(CAT_TRASH_TYPE),
				expectedTrashOriginDto = expectedDto(CAT_TRASH_ORIGIN);

		assertEquals(actual.size(), 2);

		assertEquals(actual.get(0).get_grandparentId(), parameters.getActivityId());
		assertEquals(actual.get(0).get_parentId(), parameters.getSurveyId());

		assertEquals(actual.get(0).getDate().getMillisOfSecond(), new DateTime(V_DATE).getMillisOfSecond());
		assertEquals(actual.get(0).getDate().getMillisOfSecond(), actual.get(1).getDate().getMillisOfSecond());

		assertTrue(actual.get(0).getDataDefinition() == 10L);
		assertTrue(actual.get(0).getDataDefinition() == actual.get(1).getDataDefinition());
		assertEquals(actual.get(0), expectedTrashTypeDto);
		assertEquals(actual.get(1), expectedTrashOriginDto);
	}

	protected ObjectCollectingSeriesDTO expectedDto(String parName) {

		ObjectCollectingSeriesDTO dto = new ObjectCollectingSeriesDTO();
		dto.setDate(new DateTime(V_DATE));
		dto.setQFlag(V_QFLAG.charAt(0));
		dto.setVFlag(V_VFLAG.charAt(0));
		dto.setRemark(V_REMARKS);
		dto.setDataDefinition(10L);
		dto.setValue(Double.parseDouble(V_PAR_CONT));
		switch (parName) {
		case CAT_TRASH_TYPE:
			dto.setObject(trashTypeClassification);
			break;
		case CAT_TRASH_ORIGIN:
			dto.setObject(trashOriginClassification);
			break;
		}
		return dto;
	}

	protected ObjectCollectingSeriesParameters createConfig() throws IOException {

		ObjectCollectingSeriesParameters parameters = new ObjectCollectingSeriesParameters();
		parameters.setActivityId("activityId");
		parameters.setSurveyId("19");
		parameters.setMatching(
				readListResourceJson("/data/ingest/dto/item/objectcollectingseries/trashcollecting-config.json"));
		return parameters;
	}

	private ObjectCollectingSeriesMatching readListResourceJson(String path) throws IOException {

		InputStream resource = getClass().getResource(path).openStream();

		return jacksonMapper.readValue(resource, ObjectCollectingSeriesMatching.class);
	}
}