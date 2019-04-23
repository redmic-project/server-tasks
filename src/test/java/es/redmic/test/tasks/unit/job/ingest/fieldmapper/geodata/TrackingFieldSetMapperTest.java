package es.redmic.test.tasks.unit.job.ingest.fieldmapper.geodata;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.batch.item.file.transform.FieldSet;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

import es.redmic.models.es.administrative.dto.PlatformDTO;
import es.redmic.models.es.geojson.tracking.common.ElementTrackingDTO;
import es.redmic.models.es.geojson.tracking.common.ElementTrackingPropertiesDTO;
import es.redmic.models.es.maintenance.device.dto.DeviceDTO;
import es.redmic.tasks.ingest.geodata.tracking.jobs.TrackingFieldSetMapper;
import es.redmic.tasks.ingest.geodata.tracking.jobs.TrackingParameters;
import es.redmic.tasks.ingest.model.matching.tracking.dto.TrackingMatching;
import es.redmic.test.tasks.utils.mapper.JsonToBeanTestUtil;

@RunWith(MockitoJUnitRunner.class)
public class TrackingFieldSetMapperTest {

	private static final String DATE = "Date";
	private static final String GEOMETRY_X = "GeometryX";
	private static final String GEOMETRY_Y = "GeometryY";
	private static final String DEVICE = "Device";
	private static final String QFLAG = "Quality";
	private static final String VFLAG = "Validation";

	private static final String V_DATE = "2004-12-13T21:39:45.618-08:00";
	private static final String V_GEOMETRY_X = "-16.510296";
	private static final String V_GEOMETRY_Y = "28.064209";
	private static final String V_DEVICE = "10";
	private static final String V_VFLAG = "A";
	private static final String V_QFLAG = "3";

	PlatformDTO platformDTO;

	private ObjectMapper jacksonMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
			false);

	protected FieldSet createFieldSet() {
		String[] columnNames = new String[] { DATE, GEOMETRY_X, GEOMETRY_Y, DEVICE, QFLAG, VFLAG };
		String[] tokens = new String[] { V_DATE, V_GEOMETRY_X, V_GEOMETRY_Y, V_DEVICE, V_QFLAG, V_VFLAG };

		return new DefaultFieldSet(tokens, columnNames);
	}

	@Before
	public void setupTest() throws IOException {

		platformDTO = (PlatformDTO) JsonToBeanTestUtil.getBean("/data/ingest/dto/item/tracking/platform.json",
				PlatformDTO.class);
		// when(platformESService.findByUuid(anyString())).thenReturn(platformDTO);
	}

	@Test
	public void setterPropertiesTimeSeriesDTOwithBeanUtils()
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		ElementTrackingDTO dtoTemp = new ElementTrackingDTO();

		PropertyUtils.setProperty(dtoTemp, "geometry", getGeometry());

		ElementTrackingPropertiesDTO elementTrackingPropertiesDTO = new ElementTrackingPropertiesDTO();

		PropertyUtils.setProperty(elementTrackingPropertiesDTO, "date", new DateTime(V_DATE));
		PropertyUtils.setProperty(elementTrackingPropertiesDTO, "qFlag", 'A');
		PropertyUtils.setProperty(elementTrackingPropertiesDTO, "vFlag", '2');
		PropertyUtils.setProperty(elementTrackingPropertiesDTO, "device", getDevice(10L));

		assertEquals(elementTrackingPropertiesDTO.getDate().getMillisOfSecond(),
				new DateTime(V_DATE).getMillisOfSecond());
	}

	@Test
	public void parseLineCSVToReturn2BeansTimeSeriesDTOWithEqualsDateTime() throws Exception {

		FieldSet fs = createFieldSet();
		TrackingParameters parameters = createConfig();
		TrackingFieldSetMapper fieldSetMapper = new TrackingFieldSetMapper(parameters);

		List<ElementTrackingDTO> actual = fieldSetMapper.mapFieldSet(fs);

		ElementTrackingDTO expectedElementTrackingDTO = expectedDto();

		assertEquals(actual.size(), 1);
		assertEquals(actual.get(0).getGeometry(), getGeometry());
		assertEquals(actual.get(0).getProperties().getActivityId(), parameters.getActivityId());
		assertEquals(actual.get(0).getProperties().getDate().getMillisOfSecond(),
				new DateTime(V_DATE).getMillisOfSecond());
		assertTrue(actual.get(0).getProperties().getDevice().getId() == 10L);
		assertEquals(actual.get(0), expectedElementTrackingDTO);
	}

	protected ElementTrackingDTO expectedDto() {

		ElementTrackingDTO dto = new ElementTrackingDTO();

		dto.setGeometry(getGeometry());

		ElementTrackingPropertiesDTO elementTrackingPropertiesDTO = new ElementTrackingPropertiesDTO();

		elementTrackingPropertiesDTO.setDate(new DateTime(V_DATE));
		elementTrackingPropertiesDTO.setqFlag(V_QFLAG.charAt(0));
		elementTrackingPropertiesDTO.setvFlag(V_VFLAG.charAt(0));

		elementTrackingPropertiesDTO.setDevice(getDevice(Long.valueOf(V_DEVICE)));

		dto.setProperties(elementTrackingPropertiesDTO);

		return dto;
	}

	private Point getGeometry() {

		GeometryFactory geomFactory = new GeometryFactory(new PrecisionModel(), 4326);
		return geomFactory
				.createPoint(new Coordinate(Double.parseDouble(V_GEOMETRY_X), Double.parseDouble(V_GEOMETRY_Y)));
	}

	private DeviceDTO getDevice(Long id) {

		DeviceDTO device = new DeviceDTO();
		device.setId(id);
		return device;
	}

	protected TrackingParameters createConfig() throws IOException {

		TrackingParameters parameters = new TrackingParameters();
		parameters.setActivityId("activityId");
		parameters.setPlatformDTO(platformDTO);
		parameters.setMatching(readListResourceJson("/data/ingest/dto/item/tracking/tracking-config.json"));
		return parameters;
	}

	private TrackingMatching readListResourceJson(String path) throws IOException {

		InputStream resource = getClass().getResource(path).openStream();

		return jacksonMapper.readValue(resource, TrackingMatching.class);
	}
}
