package es.redmic.tasks.ingest.series.objectcollectingseries.repository;

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

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import es.redmic.tasks.ingest.model.matching.series.objectcollectingseries.dto.ObjectCollectingSeriesMatching;
import es.redmic.tasks.ingest.model.status.model.UserTasks;
import es.redmic.tasks.ingest.series.common.repository.IngestSeriesRepository;
import es.redmic.tasks.ingest.series.objectcollectingseries.jobs.ObjectCollectingSeriesParameters;

@Component
public class IngestObjectCollectingSeriesRepository extends IngestSeriesRepository<ObjectCollectingSeriesMatching> {

	public IngestObjectCollectingSeriesRepository() {
	}

	@Override
	public JobParameters getJobParameters(String taskId) {

		UserTasks task = (UserTasks) repository.findById(taskId).get_source();

		Map<String, JobParameter> jobParameters = new HashMap<String, JobParameter>();

		Map<String, Object> firstStepParameters = getAndCheckFirstParameters(task);

		ObjectCollectingSeriesParameters parameters = orikaMapper.getMapperFacade().map(firstStepParameters,
				ObjectCollectingSeriesParameters.class);

		parameters.setMatching(getMatching(task));
		parameters.setUserId(task.getUserId().toString());
		parameters.setHeader(getHeader(task));

		String parametersRaw = null;
		try {
			parametersRaw = objectMapper.writeValueAsString(parameters);
		} catch (JsonProcessingException e) {
			// TODO emitir exception
			e.printStackTrace();
		}
		jobParameters.put("parameters", new JobParameter(parametersRaw));
		return new JobParameters(jobParameters);
	}
}
