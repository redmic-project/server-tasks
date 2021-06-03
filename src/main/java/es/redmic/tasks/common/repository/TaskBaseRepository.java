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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.redmic.es.config.OrikaScanBeanESItfc;
import es.redmic.exception.tasks.ingest.IngestBaseException;
import es.redmic.exception.tasks.ingest.JobFinishStepNotAllowedException;
import es.redmic.exception.tasks.ingest.JobRunningStepNotAllowedException;
import es.redmic.exception.tasks.ingest.JobStartedStepNotAllowedException;
import es.redmic.tasks.common.service.TaskJobWithInterventionService;
import es.redmic.tasks.ingest.model.common.dto.RunTaskWithParametersDTO;
import es.redmic.tasks.ingest.model.status.common.TaskStatus;
import es.redmic.tasks.ingest.model.status.dto.CompletedTaskDTO;
import es.redmic.tasks.ingest.model.status.dto.FailedTaskDTO;
import es.redmic.tasks.ingest.model.status.dto.RegisteredTaskDTO;
import es.redmic.tasks.ingest.model.status.dto.RunningTaskDTO;
import es.redmic.tasks.ingest.model.status.dto.StartedTaskDTO;
import es.redmic.tasks.ingest.model.status.model.UserTasks;
import es.redmic.tasks.ingest.model.step.model.BaseStep;
import es.redmic.tasks.ingest.model.step.model.CompletedStep;
import es.redmic.tasks.ingest.model.step.model.RegisteredStep;
import es.redmic.tasks.ingest.model.step.model.RunningStep;
import es.redmic.tasks.ingest.model.step.model.StartedStep;

public abstract class TaskBaseRepository {

	private final Logger LOGGER = LoggerFactory.getLogger(TaskJobWithInterventionService.class);

	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	protected OrikaScanBeanESItfc orikaMapper;

	@Autowired
	protected UserTasksRepository repository;

	public TaskBaseRepository() {

	}

	/*
	 * Registro de la tarea
	 */

	@SuppressWarnings("unchecked")
	public RegisteredTaskDTO register(Long userId, RunTaskWithParametersDTO<?> runDto) {

		return register(userId, runDto.getTaskName(),
				(HashMap<String, Object>) objectMapper.convertValue(runDto.getParameters(), Map.class));
	}

	public RegisteredTaskDTO register(Long userId, String taskName, HashMap<String, Object> params) {

		LOGGER.debug("Registrando la tarea " + taskName + " para el usuario " + userId);

		UserTasks task = new UserTasks();
		task.setId(UUID.randomUUID().toString());
		task.setTaskName(taskName);
		task.setTaskType(getTaskType());
		task.setStatus(TaskStatus.REGISTERED);
		task.setUserId(userId);
		task.setUpdated(new DateTime());

		RegisteredStep step = new RegisteredStep();
		step.setParameters(params);
		task.addStep(step);

		task = repository.save(task);

		return orikaMapper.getMapperFacade().map(task, RegisteredTaskDTO.class);
	}

	/*
	 * Una vez la tarea está registrada, si todo va bien pasa al estado de
	 * comenzada.
	 */

	public StartedTaskDTO run(String taskId) {

		UserTasks task = (UserTasks) repository.findById(taskId).get_source();
		task.setStatus(TaskStatus.STARTED);

		LOGGER.debug("Arrancando la tarea " + task.getTaskName() + " para el usuario " + task.getUserId());

		if ((task.lastStep() == null) || (task.lastStep().getStatus() != TaskStatus.REGISTERED)) {
			LOGGER.error("Error. Intentando iniciar una tarea sin estar registrada previamente.");
			throw new JobStartedStepNotAllowedException(taskId);
		}

		StartedStep step = new StartedStep();
		task.addStep(step);
		task.setUpdated(new DateTime());

		task = repository.update(task);

		return orikaMapper.getMapperFacade().map(task, StartedTaskDTO.class);
	}

	/*
	 * Paso previo a ejecutar la acción que conlleva la tarea. Si se tienen los
	 * elementos necesarios se arrancará el job
	 */

	public RunningTaskDTO setInProgress(String taskId) {

		UserTasks task = (UserTasks) repository.findById(taskId).get_source();

		LOGGER.debug("Poniendo en progreso la tarea " + task.getTaskName() + " para el usuario " + task.getUserId());

		BaseStep lastStep = task.lastStep();
		if ((lastStep == null) || !((lastStep.getStatus() == TaskStatus.STARTED)
				|| (lastStep.getStatus() == TaskStatus.WAITING_INTERVENTION))) {
			LOGGER.error("Error. Intentando ejecutar una tarea sin estar iniciada o en espera de intervención.");
			throw new JobRunningStepNotAllowedException(taskId);
		}
		task.setStartDate(new DateTime());
		task.setStatus(TaskStatus.RUNNING);

		RunningStep step = new RunningStep();
		task.addStep(step);
		task.setUpdated(new DateTime());

		task = repository.update(task);

		return orikaMapper.getMapperFacade().map(task, RunningTaskDTO.class);
	}

	/*
	 * Registro de la tarea como fallida
	 */

	public FailedTaskDTO setFailed(IngestBaseException ex) {

		FailedTaskDTO failedTask = orikaMapper.getMapperFacade().convert(ex, FailedTaskDTO.class, null, null);

		LOGGER.debug("Registrando error en la tarea " + failedTask.getTaskName() + " para el usuario "
				+ failedTask.getUserId());

		UserTasks task = orikaMapper.getMapperFacade().map(failedTask, UserTasks.class);

		task.setUpdated(new DateTime());

		repository.update(task);

		return failedTask;
	}

	/*
	 * Registro de la tarea como finalizada
	 */

	public CompletedTaskDTO finish(String taskId) {
		return finish(taskId, null);
	}

	public CompletedTaskDTO finish(String taskId, Object taskResult) {

		UserTasks task = (UserTasks) repository.findById(taskId).get_source();

		if (TaskStatus.FAILED.equals(task.getStatus())) {
			LOGGER.error("Intentando finalizar con un status erroneo");
			throw new JobFinishStepNotAllowedException(taskId);
		}

		if ((task.lastStep() == null) || (task.lastStep().getStatus() != TaskStatus.RUNNING)) {
			LOGGER.error("Error. Intentando finalizar una tarea que no se ha ejecutado.");
			throw new JobFinishStepNotAllowedException(taskId);
		}

		if (taskResult != null)
			task.setTaskResult(taskResult);

		task.setStatus(TaskStatus.COMPLETED);
		task.setEndDate(new DateTime());
		task.setUpdated(new DateTime());
		CompletedStep step = new CompletedStep();
		task.addStep(step);

		repository.update(task);

		return orikaMapper.getMapperFacade().map(task, CompletedTaskDTO.class);
	}

	public abstract JobParameters getJobParameters(String taskId);

	protected Map<String, Object> getIniParameters(UserTasks task) {
		RegisteredStep firstStep = (RegisteredStep) task.firstStep();
		firstStep.getParameters().put("taskId", task.getId());
		return firstStep.getParameters();
	}

	protected abstract String getTaskType();
}
