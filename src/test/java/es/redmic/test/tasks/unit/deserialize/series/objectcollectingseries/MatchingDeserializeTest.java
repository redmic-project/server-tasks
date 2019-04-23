package es.redmic.test.tasks.unit.deserialize.series.objectcollectingseries;

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
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

import es.redmic.tasks.ingest.model.matching.common.dto.ParameterDTO;
import es.redmic.tasks.ingest.model.matching.series.objectcollectingseries.dto.ObjectCollectingSeriesMatching;
import es.redmic.test.tasks.utils.mapper.JsonToBeanTestUtil;

public class MatchingDeserializeTest {

	private static Validator validator;

	static final String PATH = "/data/ingest/dto/item/objectcollectingseries/";

	@BeforeClass
	public static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

		validator = factory.getValidator();
	}

	@Test
	public void deserializeMatchingParameterDtoInSubTypeGeom() throws IOException {

		ParameterDTO dto = (ParameterDTO) JsonToBeanTestUtil.getBean(PATH + "matching-parameter.json",
				ParameterDTO.class);

		assertEquals(dto.getField(), "value");
		assertEquals(dto.getDataDefinitionId().longValue(), 10L);
	}

	@Test
	public void deserializeListMatchingDtoInSubTypes() throws IOException {

		ObjectCollectingSeriesMatching match = (ObjectCollectingSeriesMatching) JsonToBeanTestUtil
				.getBean(PATH + "matching-all.json", ObjectCollectingSeriesMatching.class);

		Set<ConstraintViolation<ObjectCollectingSeriesMatching>> a = validator.validate(match);
		assertEquals(a.size(), 0);
	}
}
