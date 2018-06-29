package es.redmic.tasks.worms.repository;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import es.redmic.exception.tasks.ingest.JobExecutionException;
import es.redmic.exception.tasks.ingest.JobInterventionStepNotAllowedException;
import es.redmic.tasks.common.repository.TaskBaseRepository;
import es.redmic.tasks.common.utils.TasksTypes;
import es.redmic.tasks.ingest.model.status.common.TaskStatus;
import es.redmic.tasks.ingest.model.status.model.UserTasks;
import es.redmic.tasks.worms.jobs.WormsToRedmicParameters;

@Component
public class WormsToRedmicTaskRepository extends TaskBaseRepository {

	private final Logger LOGGER = LoggerFactory.getLogger(WormsToRedmicTaskRepository.class);

	public WormsToRedmicTaskRepository() {
	}

	public JobParameters resume(String taskId) {

		UserTasks task = (UserTasks) repository.findById(taskId).get_source();

		if ((task.lastStep() == null) || (task.lastStep().getStatus() != TaskStatus.STARTED)) {
			LOGGER.error("Error. Intentando establecer parámetros a la tarea sin haber sido requeridos.");
			throw new JobInterventionStepNotAllowedException(taskId);
		}

		return getJobParameters(taskId);
	}

	@Override
	public JobParameters getJobParameters(String taskId) {

		UserTasks task = (UserTasks) repository.findById(taskId).get_source();

		Map<String, JobParameter> jobParameters = new HashMap<String, JobParameter>();

		Map<String, Object> firstStepParameters = getAndCheckFirstParameters(task);

		WormsToRedmicParameters parameters = orikaMapper.getMapperFacade().map(firstStepParameters,
				WormsToRedmicParameters.class);

		parameters.setUserId(task.getUserId().toString());

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

		if ((firstStepParameters.get("taskId") == null)) {

			LOGGER.error("Intentando acceder a parámetros no seteados");
			throw new JobExecutionException(task.getId());
		}

		return firstStepParameters;
	}

	@Override
	protected String getTaskType() {
		return TasksTypes.WORMS_TO_REDMIC;
	}
}