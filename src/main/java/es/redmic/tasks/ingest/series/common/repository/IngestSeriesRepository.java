package es.redmic.tasks.ingest.series.common.repository;

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

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.redmic.exception.tasks.ingest.JobExecutionException;
import es.redmic.tasks.ingest.common.repository.IngestCSVRepository;
import es.redmic.tasks.ingest.model.matching.common.dto.MatchingBaseDTO;
import es.redmic.tasks.ingest.model.status.model.UserTasks;

public abstract class IngestSeriesRepository<TDTO extends MatchingBaseDTO> extends IngestCSVRepository<TDTO> {

	private static final Logger LOGGER = LoggerFactory.getLogger(IngestSeriesRepository.class);

	public IngestSeriesRepository() {
	}

	protected Map<String, Object> getAndCheckFirstParameters(UserTasks task) {

		Map<String, Object> firstStepParameters = getIniParameters(task);

		if ((firstStepParameters.get("taskId") == null) || (firstStepParameters.get("activityId") == null)
				|| (firstStepParameters.get("surveyId") == null) || (firstStepParameters.get("delimiter") == null)
				|| (firstStepParameters.get("fileName") == null)) {

			LOGGER.error("Intentando acceder a parámetros no seteados");
			throw new JobExecutionException(task.getId());
		}

		return firstStepParameters;
	}
}
