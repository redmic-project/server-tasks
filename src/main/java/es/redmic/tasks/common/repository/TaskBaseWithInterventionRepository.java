package es.redmic.tasks.common.repository;

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

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.JsonNode;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;
import com.kjetland.jackson.jsonSchema.JsonSchemaResources;

import es.redmic.exception.tasks.ingest.JobInterventionStepNotAllowedException;
import es.redmic.exception.tasks.ingest.JobMatchingInterventionStepNotAllowedException;
import es.redmic.mediastorage.service.MediaStorageService;
import es.redmic.tasks.common.service.TaskJobWithInterventionService;
import es.redmic.tasks.ingest.model.intervention.common.InterventionType;
import es.redmic.tasks.ingest.model.intervention.matching.InterventionMatching;
import es.redmic.tasks.ingest.model.intervention.matching.RequestUserInterventionMatchingTaskDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.MatchingCommonDTO;
import es.redmic.tasks.ingest.model.status.common.TaskStatus;
import es.redmic.tasks.ingest.model.status.model.UserTasks;
import es.redmic.tasks.ingest.model.step.intervention.ResponseMatchingIntervention;
import es.redmic.tasks.ingest.model.step.intervention.WaitingInterventionStep;
import es.redmic.tasks.ingest.model.step.model.RegisteredStep;

public abstract class TaskBaseWithInterventionRepository<TDTO extends MatchingCommonDTO> extends TaskBaseRepository {

	@Value("${property.path.media_storage.temp.INGEST_DATA}")
	String MEDIASTORAGE_TEMP_INGEST_DATA;

	private final Logger LOGGER = LoggerFactory.getLogger(TaskJobWithInterventionService.class);

	protected final int SAMPLE_SIZE = 10;

	@Autowired
	protected MediaStorageService mediaStorage;

	private Class<TDTO> typeOfTDTO;

	@SuppressWarnings("unchecked")
	public TaskBaseWithInterventionRepository() {
		this.typeOfTDTO = (Class<TDTO>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	/*
	 * Paso del workflow que corresponde a la espera de intervención del usuario
	 * de tipo matching
	 * 
	 * Este paso es específico de las tareas con intervención de tipo matching
	 */

	public RequestUserInterventionMatchingTaskDTO preMatching(String taskId) {

		InterventionMatching interventionMatching = new InterventionMatching();

		interventionMatching.setMatching(getMatchingSchema());

		UserTasks task = (UserTasks) repository.findById(taskId).get_source();

		if ((task.lastStep() == null) || (task.lastStep().getStatus() != TaskStatus.STARTED)) {
			LOGGER.error("Error. Intentando establecer parámetros a la tarea sin haber sido requeridos.");
			throw new JobInterventionStepNotAllowedException(taskId);
		}

		task.setStatus(TaskStatus.WAITING_INTERVENTION);
		WaitingInterventionStep step = new WaitingInterventionStep();
		task.addStep(step);

		RegisteredStep registeredStep = (RegisteredStep) task.getSteps().get(0);

		addInitialData(interventionMatching, taskId, MEDIASTORAGE_TEMP_INGEST_DATA, registeredStep.getParameters());

		step.setInterventionDescription(interventionMatching);

		// TODO Llamar a la función de prematching
		task.setUpdated(new DateTime());
		task = repository.update(task);

		return orikaMapper.getMapperFacade().map(task, RequestUserInterventionMatchingTaskDTO.class);
	}

	/*
	 * Paso del workflow que corresponde a la puesta en marcha del job, una vez
	 * recibido el matching
	 * 
	 * Este paso es específico de las tareas con intervención de tipo matching
	 */

	public JobParameters resume(String taskId, TDTO matching) {

		UserTasks task = (UserTasks) repository.findById(taskId).get_source();

		WaitingInterventionStep lastStep = (WaitingInterventionStep) task.lastWaitingInterventionStep();

		if ((task.lastWaitingInterventionStep() == null)
				|| (task.lastWaitingInterventionStep().getStatus() != TaskStatus.WAITING_INTERVENTION)) {
			LOGGER.error("Error. Se esperaba una intervención de tipo matching.");
			throw new JobMatchingInterventionStepNotAllowedException(taskId);
		}

		if (lastStep.getInterventionDescription().getType() != InterventionType.MATCHING) {
			LOGGER.error("Se requiere una intervención de tipo matching");
			throw new JobMatchingInterventionStepNotAllowedException(taskId);
		}

		ResponseMatchingIntervention response = new ResponseMatchingIntervention();
		response.setMatching(matching);
		lastStep.setResponse(response);
		task.updateStep(lastStep);
		task.setUpdated(new DateTime());

		task = repository.update(task);

		return getJobParameters(taskId);
	}

	protected List<String> getHeader(UserTasks task) {

		WaitingInterventionStep lastStep = (WaitingInterventionStep) task.lastStep();
		InterventionMatching interventionDescription = (InterventionMatching) lastStep.getInterventionDescription();
		return interventionDescription.getHeader();
	}

	protected TDTO getMatching(UserTasks task) {

		WaitingInterventionStep lastStep = (WaitingInterventionStep) task.lastStep();
		ResponseMatchingIntervention response = (ResponseMatchingIntervention) lastStep.getResponse();
		if (response == null)
			return null;
		return objectMapper.convertValue(response.getMatching(), typeOfTDTO);
	}

	/*
	 * Retorna el json esquema del dto de matching, para que el cliente cumpla
	 * con las especificaciones.
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getMatchingSchema() {

		// TODO: Obtener los target de la base de datos o de propiedades
		Map<String, Object> resources = new HashMap<String, Object>();
		resources.put("deviceUrl", "/api/devices");
		resources.put("areaTypeUrl", "/api/areatypes");

		JsonSchemaGenerator jsonSchemaGenerator = new JsonSchemaGenerator(objectMapper,
				JsonSchemaResources.setResources(resources));

		JsonNode jsonSchema = jsonSchemaGenerator.generateJsonSchema(typeOfTDTO);

		return objectMapper.convertValue(jsonSchema, Map.class);
	}

	/*
	 * Función que debe estar implementada en los repositorios específicos para
	 * añadir la información necesaria para que el cliente pueda rellenar el
	 * matching
	 */
	protected abstract void addInitialData(InterventionMatching interventionMatching, String taskId,
			String directoryPath, Map<String, Object> fileProperties);
}
