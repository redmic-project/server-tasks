package es.redmic.test.tasks.unit.deserialize.geodata.area;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

import es.redmic.tasks.ingest.model.matching.area.dto.AreaMatching;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemAreaTypeDTO;
import es.redmic.test.tasks.utils.mapper.JsonToBeanTestUtil;

public class MatchingDeserializeTest {

	private static Validator validator;

	static final String PATH = "/data/ingest/dto/item/area/";

	@BeforeClass
	public static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

		validator = factory.getValidator();
	}

	@Test
	public void deserializeMatchingParameterDtoInSubTypeGeom() throws IOException {

		ItemAreaTypeDTO dto = (ItemAreaTypeDTO) JsonToBeanTestUtil.getBean(PATH + "matching-areatype.json",
				ItemAreaTypeDTO.class);

		assertEquals(dto.getField(), "areaType");
		assertEquals(dto.getAreaType().getId().longValue(), 8L);
	}

	@Test
	public void deserializeListMatchingDtoInSubTypes() throws IOException {

		AreaMatching match = (AreaMatching) JsonToBeanTestUtil.getBean(PATH + "matching-all.json", AreaMatching.class);

		Set<ConstraintViolation<AreaMatching>> a = validator.validate(match);

		assertEquals(a.size(), 0);
	}
}
