package es.redmic.test.tasks.unit.deserialize.common;

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
