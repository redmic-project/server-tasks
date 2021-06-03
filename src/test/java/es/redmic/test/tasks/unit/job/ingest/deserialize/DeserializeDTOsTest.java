package es.redmic.test.tasks.unit.job.ingest.deserialize;

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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;
import com.kjetland.jackson.jsonSchema.JsonSchemaResources;

import es.redmic.tasks.ingest.model.intervention.matching.RequestUserInterventionMatchingTaskDTO;
import es.redmic.tasks.ingest.model.matching.series.timeseries.dto.TimeSeriesMatching;

public class DeserializeDTOsTest {

	ObjectMapper jacksonMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	private static Validator validator;

	static final String path = "/data/tasks/ingest/timeseries/";

	@BeforeClass
	public static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

		validator = factory.getValidator();
	}

	@Test
	public void DeserializeIngestDataDTOToReturnOK() throws IOException {

		InputStream resource = getClass().getResource(path + "Intervention.json").openStream();

		RequestUserInterventionMatchingTaskDTO dto = jacksonMapper.readValue(resource,
				RequestUserInterventionMatchingTaskDTO.class);

		Set<ConstraintViolation<RequestUserInterventionMatchingTaskDTO>> error = validator.validate(dto);

		assertEquals(error.size(), 0);
		assertNotNull(dto.getInterventionDescription().getMatching());

		assertThat(dto, instanceOf(RequestUserInterventionMatchingTaskDTO.class));
		assertEquals(dto.getInterventionDescription().getMatching(), loadSchema(TimeSeriesMatching.class));
	}

	@SuppressWarnings("unchecked")
	protected Map<String, Object> loadSchema(Class<?> typeOfTDTO) {

		JsonSchemaGenerator jsonSchemaGenerator = new JsonSchemaGenerator(jacksonMapper,
				JsonSchemaResources.setResources(new HashMap<>()));

		JsonNode jsonSchema = jsonSchemaGenerator.generateJsonSchema(typeOfTDTO);

		return jacksonMapper.convertValue(jsonSchema, Map.class);
	}
}
