package es.redmic.test.tasks.unit.job.ingest.fieldmapper.geodata;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.PrecisionModel;

import es.redmic.exception.tasks.ingest.IngestMatchingGeometryException;
import es.redmic.tasks.ingest.geodata.area.jobs.AreaFieldSetMapper;
import es.redmic.tasks.ingest.geodata.area.jobs.AreaParameters;

@RunWith(MockitoJUnitRunner.class)
public class AreaFieldSetMapperTest {

	AreaFieldSetMapper areaFieldSetMapper;

	GeometryFactory geomFactory = new GeometryFactory(new PrecisionModel(), 4326);

	@Before
	public void setupTest() throws IOException {
		AreaParameters jobParameters = new AreaParameters();
		jobParameters.setTaskId("prueba");
		areaFieldSetMapper = new AreaFieldSetMapper(jobParameters);
	}

	@Test
	public void getGeometryFromFieldSet_ReturnGeometry_IfGeometryIsMultiPolygon() throws Exception {

		MultiPolygon result = Whitebox.<MultiPolygon>invokeMethod(areaFieldSetMapper, "getGeometryFromFieldSet",
				geomFactory.createMultiPolygon(null));
		assertTrue(result.isEmpty());
	}

	@Test
	public void getGeometryFromFieldSet_ReturnGeometry_IfGeometryIsPolygon() throws Exception {

		MultiPolygon result = Whitebox.<MultiPolygon>invokeMethod(areaFieldSetMapper, "getGeometryFromFieldSet",
				geomFactory.createPolygon(null, null));
		assertTrue(result.isEmpty());
	}

	@Test(expected = IngestMatchingGeometryException.class)
	public void getGeometryFromFieldSet_ThrowException_IfGeometryNotIsMultiPolygonOrPolygon() throws Exception {

		Whitebox.<MultiPolygon>invokeMethod(areaFieldSetMapper, "getGeometryFromFieldSet",
				geomFactory.createPoint(new Coordinate()));
	}

	@Test(expected = IngestMatchingGeometryException.class)
	public void getGeometryFromFieldSet_ThrowException_IfGeometryIsNull() throws Exception {

		Whitebox.invokeMethod(areaFieldSetMapper, "getGeometryFromFieldSet", null);
	}
}
