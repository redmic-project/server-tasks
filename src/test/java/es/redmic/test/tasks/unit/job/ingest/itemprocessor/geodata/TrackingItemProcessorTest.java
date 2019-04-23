package es.redmic.test.tasks.unit.job.ingest.itemprocessor.geodata;

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

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.powermock.reflect.Whitebox;

import com.vividsolutions.jts.geom.Coordinate;

import es.redmic.exception.tasks.ingest.IngestItemProcessorDataValueException;
import es.redmic.exception.tasks.ingest.IngestItemProcessorGeometryException;
import es.redmic.models.es.geojson.common.utils.GeometryUtils;
import es.redmic.tasks.ingest.geodata.tracking.jobs.TrackingItemProcessor;

@RunWith(MockitoJUnitRunner.class)
public class TrackingItemProcessorTest {

	@InjectMocks
	TrackingItemProcessor trackingItemProcessor;

	@Before
	public void setupTest() {
	}

	@Test(expected = IngestItemProcessorDataValueException.class)
	public void getTimeToPreviousPoint_ThrowException_IfDatesIsNotInOrder() throws Exception {

		DateTime previosDate = new DateTime(), currentDate = new DateTime();

		Whitebox.invokeMethod(trackingItemProcessor, "getTimeToPreviousPoint", currentDate, previosDate);
	}

	@Test(expected = IngestItemProcessorDataValueException.class)
	public void getTimeToPreviousPoint_ThrowException_IfDateIsNull() throws Exception {

		DateTime currentDate = new DateTime();

		Whitebox.invokeMethod(trackingItemProcessor, "getTimeToPreviousPoint", currentDate, null);
	}

	@Test(expected = IngestItemProcessorDataValueException.class)
	public void getTimeToPreviousPoint_ThrowException_IfEqualDates() throws Exception {

		DateTime currentDate = new DateTime();

		Whitebox.invokeMethod(trackingItemProcessor, "getTimeToPreviousPoint", currentDate, currentDate);
	}

	@Test(expected = IngestItemProcessorGeometryException.class)
	public void getDistanceToPreviosPoint_ThrowExceptions_IfPointsIsNoValid() throws Exception {

		CoordinateReferenceSystem crs = GeometryUtils.getCRS("EPSG:4326");

		Whitebox.invokeMethod(trackingItemProcessor, "getDistanceToPreviousPoint", new Coordinate(10, 10), null, crs);
	}

	@Test(expected = IngestItemProcessorDataValueException.class)
	public void getSpeedToPreviousPoint_ThrowExceptions_IfTimeToPreviousPointIsZero() throws Exception {

		Whitebox.invokeMethod(trackingItemProcessor, "getSpeedToPreviousPoint", 200.0, 0.0);
	}
}
