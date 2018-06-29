package es.redmic.test.tasks.unit.deserialize.geodata.tracking;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

import es.redmic.tasks.ingest.model.matching.common.dto.ItemDeviceDTO;
import es.redmic.tasks.ingest.model.matching.tracking.dto.TrackingMatching;
import es.redmic.test.tasks.utils.mapper.JsonToBeanTestUtil;

public class MatchingDeserializeTest {

	private static Validator validator;

	static final String PATH = "/data/ingest/dto/item/tracking/";

	@BeforeClass
	public static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

		validator = factory.getValidator();
	}

	@Test
	public void deserializeMatchingParameterDtoInSubTypeGeom() throws IOException {

		ItemDeviceDTO dto = (ItemDeviceDTO) JsonToBeanTestUtil.getBean(PATH + "matching-device.json",
				ItemDeviceDTO.class);

		assertEquals(dto.getField(), "device");
		assertEquals(dto.getDevice().getId().longValue(), 10L);
	}

	@Test
	public void deserializeListMatchingDtoInSubTypes() throws IOException {

		TrackingMatching match = (TrackingMatching) JsonToBeanTestUtil.getBean(PATH + "matching-all.json",
				TrackingMatching.class);

		Set<ConstraintViolation<TrackingMatching>> a = validator.validate(match);

		assertEquals(a.size(), 0);
	}
}
