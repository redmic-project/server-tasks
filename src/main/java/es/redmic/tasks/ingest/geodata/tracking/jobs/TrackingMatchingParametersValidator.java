package es.redmic.tasks.ingest.geodata.tracking.jobs;

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

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import es.redmic.db.geodata.tracking.animal.service.AnimalTrackingService;
import es.redmic.db.geodata.tracking.platform.service.PlatformTrackingService;
import es.redmic.tasks.common.utils.JobUtils;
import es.redmic.tasks.ingest.common.jobs.MatchingParametersValidator;

@Component
@Transactional
public class TrackingMatchingParametersValidator extends MatchingParametersValidator {

	@Autowired
	AnimalTrackingService animalTrackingService;

	@Autowired
	PlatformTrackingService platformTrackingService;

	public TrackingMatchingParametersValidator() {
	}

	public void validate(JobParameters jobParameters) {

		JobParameter parameters = jobParameters.getParameters().get(JOB_PARAMETER_KEY);
		TrackingParameters trackingParameters = JobUtils.JobParameters2UserParameters(parameters,
				TrackingParameters.class);

		checkConstraints(trackingParameters);
		beforeResumeJob(trackingParameters);
	}

	protected void checkConstraints(TrackingParameters trackingParameters) {

		checkDataBindingConstraints(trackingParameters);
		checkHeader(trackingParameters.getTaskId(), trackingParameters.getHeader(),
				trackingParameters.getMatching().getMatchingColumns());
		checkFileConstraints(trackingParameters.getTaskId(), trackingParameters.getHeader(),
				trackingParameters.getMatching().getMatchingColumns());
	}

	public void beforeResumeJob(TrackingParameters trackingParameters) {

		// Comprueba y asigna el tipo de actividad en relación con el tipo de
		// dato a almacenar
		if (trackingParameters.getAnimalDTO() != null)
			animalTrackingService.checkDataType(trackingParameters.getActivityId());
		else if (trackingParameters.getPlatformDTO() != null)
			platformTrackingService.checkDataType(trackingParameters.getActivityId());
	}
}
