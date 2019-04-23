package es.redmic.tasks.common.controller;

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

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;

import es.redmic.exception.tasks.ingest.IngestBaseException;
import es.redmic.exception.tasks.ingest.IngestFileException;
import es.redmic.exception.tasks.ingest.IngestItemProcessorDataValueException;
import es.redmic.exception.tasks.ingest.IngestItemProcessorGeometryException;
import es.redmic.exception.tasks.ingest.IngestItemProcessorOrderValueException;
import es.redmic.exception.tasks.ingest.IngestMatchingClassificationException;
import es.redmic.exception.tasks.ingest.IngestMatchingColumnRequiredException;
import es.redmic.exception.tasks.ingest.IngestMatchingColumnSizeException;
import es.redmic.exception.tasks.ingest.IngestMatchingDataDefinitionException;
import es.redmic.exception.tasks.ingest.IngestMatchingException;
import es.redmic.exception.tasks.ingest.IngestPersistenceDataException;
import es.redmic.exception.tasks.ingest.JobExecutionException;
import es.redmic.exception.tasks.ingest.JobFinishStepNotAllowedException;
import es.redmic.exception.tasks.ingest.JobInterventionStepNotAllowedException;
import es.redmic.exception.tasks.ingest.JobMatchingInterventionStepNotAllowedException;
import es.redmic.exception.tasks.ingest.JobRunningStepNotAllowedException;
import es.redmic.exception.tasks.ingest.JobStartedStepNotAllowedException;
import es.redmic.exception.tasks.ingest.MatchingDataBindingException;
import es.redmic.tasks.common.service.TaskJobBaseService;

public abstract class TaskJobBaseController extends TaskBaseController {

	TaskJobBaseService service;

	public TaskJobBaseController(TaskJobBaseService service) {
		this.service = service;
		logger.info("Inicio controller TaskController");
	}

	@MessageExceptionHandler(value = { IngestFileException.class, IngestMatchingException.class,
			IngestMatchingColumnSizeException.class, IngestMatchingColumnRequiredException.class,
			IngestMatchingClassificationException.class, IngestMatchingDataDefinitionException.class,
			IngestPersistenceDataException.class, JobExecutionException.class, JobStartedStepNotAllowedException.class,
			JobRunningStepNotAllowedException.class, JobMatchingInterventionStepNotAllowedException.class,
			JobInterventionStepNotAllowedException.class, JobFinishStepNotAllowedException.class,
			MatchingDataBindingException.class, IngestItemProcessorGeometryException.class,
			IngestItemProcessorOrderValueException.class, IngestItemProcessorDataValueException.class })
	public void handleIngestException(IngestBaseException e) {
		service.setFailed(e);
	}
}
