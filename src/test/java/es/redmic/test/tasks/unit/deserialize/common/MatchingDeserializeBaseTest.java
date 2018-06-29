package es.redmic.test.tasks.unit.deserialize.common;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import es.redmic.tasks.ingest.model.matching.common.dto.ItemDateDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemPointGeometryDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemQFlagDTO;
import es.redmic.test.tasks.utils.mapper.JsonToBeanTestUtil;

public class MatchingDeserializeBaseTest {

	static final String PATH = "/data/ingest/dto/item/common/";

	@Test
	public void deserializeMatchingDateDtoCheckProps() throws IOException {

		ItemDateDTO dto = (ItemDateDTO) JsonToBeanTestUtil.getBean(PATH + "matching-date.json", ItemDateDTO.class);

		assertEquals(dto.getField(), "date");
		assertEquals(dto.getColumns().get(0), "col1");
		assertEquals(dto.getFormat(), "YYYY/MM/DD");
	}

	@Test
	public void deserializeMatchingGeomDtoCheckProps() throws IOException {

		ItemPointGeometryDTO dto = (ItemPointGeometryDTO) JsonToBeanTestUtil.getBean(PATH + "matching-geom.json",
				ItemPointGeometryDTO.class);
		assertEquals(dto.getColumns().get(0), "colX");
		assertEquals(dto.getColumns().get(1), "colY");
		assertEquals(dto.getField(), "geometry");
	}

	@Test
	public void deserializeMatchingQualityControlDtoCheckProps() throws IOException {

		ItemQFlagDTO dto = (ItemQFlagDTO) JsonToBeanTestUtil.getBean(PATH + "matching-quality.json",
				ItemQFlagDTO.class);

		assertEquals(dto.getColumns().get(0), "col1");
		assertEquals(dto.getField(), "qFlag");
	}
}
