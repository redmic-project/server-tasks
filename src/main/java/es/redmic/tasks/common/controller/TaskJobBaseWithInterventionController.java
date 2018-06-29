package es.redmic.tasks.common.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.redmic.tasks.common.service.TaskJobWithInterventionService;
import es.redmic.tasks.ingest.model.common.dto.RunTaskWithParametersDTO;
import es.redmic.tasks.ingest.model.intervention.matching.RequestUserInterventionMatchingTaskDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.MatchingCommonDTO;
import es.redmic.tasks.ingest.model.status.dto.RegisteredTaskDTO;
import es.redmic.tasks.ingest.model.status.dto.StartedTaskDTO;

public abstract class TaskJobBaseWithInterventionController<TDTO extends MatchingCommonDTO>
		extends TaskJobBaseController {

	private final Logger LOGGER = LoggerFactory.getLogger(TaskJobBaseWithInterventionController.class);

	TaskJobWithInterventionService<TDTO> service;

	public TaskJobBaseWithInterventionController(TaskJobWithInterventionService<TDTO> service) {
		super(service);
		this.service = service;
	}

	public void resume(String taskId, TDTO dto) {

		LOGGER.info("Intervención del usuario para la tarea: " + taskId);

		service.resume(taskId, dto);
	}

	protected void register(RunTaskWithParametersDTO<?> runTask, String userId, String taskName) {

		runTask.setTaskName(taskName);

		LOGGER.info("Peticion: " + " - " + runTask.getTaskName() + " - " + runTask.getParameters().toString());

		RegisteredTaskDTO dto = service.register(Long.parseLong(userId), runTask);

		publishStatusTaskToUser(dto);

		StartedTaskDTO resultDTO = service.run(dto.getId());
		publishStatusTaskToUser(resultDTO);

		RequestUserInterventionMatchingTaskDTO preMatchingDTO = service.preMatching(dto.getId());
		publishStatusTaskToUser(preMatchingDTO);

		LOGGER.info(runTask.getTaskName() + " - Iniciando ejecución");
	}
}