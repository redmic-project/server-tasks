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
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

import es.redmic.tasks.ingest.model.intervention.matching.InterventionMatching;
import es.redmic.tasks.ingest.model.status.model.UserTasks;
import es.redmic.tasks.ingest.model.step.intervention.ResponseMatchingIntervention;
import es.redmic.tasks.ingest.model.step.intervention.WaitingInterventionStep;
import es.redmic.tasks.ingest.model.step.model.RegisteredStep;
import es.redmic.tasks.ingest.model.step.model.StartedStep;
import es.redmic.test.tasks.utils.mapper.JsonToBeanTestUtil;

public class DeserializeFromES {

	private static Validator validator;

	static final String PATH = "/data/tasks/";

	@BeforeClass
	public static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

		validator = factory.getValidator();
	}

	@Test
	public void DeserializeRunDTOToReturnOK() throws IOException {

		UserTasks dto = (UserTasks) JsonToBeanTestUtil.getBean(PATH + "Task-ES.json", UserTasks.class);

		Set<ConstraintViolation<UserTasks>> error = validator.validate(dto);

		assertEquals(error.size(), 0);
		assertEquals(dto.getTaskName(), "ingest-data-timeseries");
		assertEquals(dto.getSteps().size(), 4);
		assertThat(dto.getSteps().get(0), instanceOf(RegisteredStep.class));
		assertThat(dto.getSteps().get(1), instanceOf(StartedStep.class));
		assertThat(dto.getSteps().get(2), instanceOf(WaitingInterventionStep.class));

		// Step de intervención
		WaitingInterventionStep waitStep = (WaitingInterventionStep) dto.getSteps().get(2);
		assertThat(waitStep.getInterventionDescription(), instanceOf(InterventionMatching.class));
		assertThat(waitStep.getResponse(), instanceOf(ResponseMatchingIntervention.class));

		assertThat(dto, instanceOf(UserTasks.class));
	}

}
