package es.redmic.tasks.common.service;

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

import org.springframework.batch.core.JobParameters;

import es.redmic.tasks.common.repository.TaskBaseWithInterventionRepository;
import es.redmic.tasks.ingest.common.jobs.MatchingParametersValidator;
import es.redmic.tasks.ingest.model.intervention.matching.RequestUserInterventionMatchingTaskDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.MatchingCommonDTO;

public abstract class TaskJobWithInterventionService<TDTO extends MatchingCommonDTO> extends TaskJobBaseService {

	private TaskBaseWithInterventionRepository<TDTO> ingestRepository;

	MatchingParametersValidator validator;

	public TaskJobWithInterventionService(TaskBaseWithInterventionRepository<TDTO> intestRepository,
			MatchingParametersValidator validator) {
		super(intestRepository);
		this.ingestRepository = intestRepository;
		this.validator = validator;
	}

	public RequestUserInterventionMatchingTaskDTO preMatching(String taskId) {

		return ingestRepository.preMatching(taskId);
	}

	public void resume(String taskId, TDTO matching) {

		JobParameters newParameters = ingestRepository.resume(taskId, matching);
		validator.validate(newParameters);
		resumeJob(taskId, newParameters);
	}
}
