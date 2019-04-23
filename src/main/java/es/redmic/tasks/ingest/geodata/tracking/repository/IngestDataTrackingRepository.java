package es.redmic.tasks.ingest.geodata.tracking.repository;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import es.redmic.es.administrative.service.PlatformESService;
import es.redmic.es.administrative.taxonomy.service.AnimalESService;
import es.redmic.exception.tasks.ingest.JobExecutionException;
import es.redmic.models.es.administrative.dto.PlatformCompactDTO;
import es.redmic.models.es.administrative.taxonomy.dto.AnimalCompactDTO;
import es.redmic.tasks.ingest.common.repository.IngestCSVRepository;
import es.redmic.tasks.ingest.geodata.tracking.jobs.TrackingParameters;
import es.redmic.tasks.ingest.model.matching.tracking.dto.TrackingMatching;
import es.redmic.tasks.ingest.model.status.model.UserTasks;

@Component
public class IngestDataTrackingRepository extends IngestCSVRepository<TrackingMatching> {

	private final Logger LOGGER = LoggerFactory.getLogger(IngestDataTrackingRepository.class);

	@Autowired
	private AnimalESService animalESService;

	@Autowired
	private PlatformESService platformESService;

	public IngestDataTrackingRepository() {
	}

	@Override
	public JobParameters getJobParameters(String taskId) {

		UserTasks task = (UserTasks) repository.findById(taskId).get_source();

		Map<String, JobParameter> jobParameters = new HashMap<String, JobParameter>();

		Map<String, Object> firstStepParameters = getAndCheckFirstParameters(task);

		TrackingParameters parameters = orikaMapper.getMapperFacade().map(firstStepParameters,
				TrackingParameters.class);
		parameters.setMatching(getMatching(task));
		parameters.setUserId(task.getUserId().toString());
		String uuid = firstStepParameters.get("elementUuid").toString();

		PlatformCompactDTO platformDTO = getPlatform(uuid);
		if (platformDTO != null)
			parameters.setPlatformDTO(platformDTO);
		else
			parameters.setAnimalDTO(getAnimal(uuid));

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

	protected Map<String, Object> getAndCheckFirstParameters(UserTasks task) {

		Map<String, Object> firstStepParameters = getIniParameters(task);

		if ((firstStepParameters.get("taskId") == null) || (firstStepParameters.get("activityId") == null)
				|| (firstStepParameters.get("elementUuid") == null) || (firstStepParameters.get("delimiter") == null)
				|| (firstStepParameters.get("fileName") == null)) {

			LOGGER.error("Intentando acceder a parámetros no seteados");
			throw new JobExecutionException(task.getId());
		}

		return firstStepParameters;
	}

	private PlatformCompactDTO getPlatform(String uuid) {
		return orikaMapper.getMapperFacade().map(platformESService.findByUuid(uuid), PlatformCompactDTO.class);
	}

	private AnimalCompactDTO getAnimal(String uuid) {
		return orikaMapper.getMapperFacade().map(animalESService.findByUuid(uuid), AnimalCompactDTO.class);
	}
}
