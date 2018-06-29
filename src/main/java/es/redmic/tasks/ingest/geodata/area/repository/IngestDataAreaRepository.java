package es.redmic.tasks.ingest.geodata.area.repository;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import es.redmic.exception.tasks.ingest.JobExecutionException;
import es.redmic.tasks.ingest.common.repository.IngestShapeFileRepository;
import es.redmic.tasks.ingest.geodata.area.jobs.AreaParameters;
import es.redmic.tasks.ingest.model.matching.area.dto.AreaMatching;
import es.redmic.tasks.ingest.model.status.model.UserTasks;

@Component
public class IngestDataAreaRepository extends IngestShapeFileRepository<AreaMatching> {

	private final Logger LOGGER = LoggerFactory.getLogger(IngestDataAreaRepository.class);

	public IngestDataAreaRepository() {
	}

	@Override
	public JobParameters getJobParameters(String taskId) {

		UserTasks task = (UserTasks) repository.findById(taskId).get_source();

		Map<String, JobParameter> jobParameters = new HashMap<String, JobParameter>();

		Map<String, Object> firstStepParameters = getAndCheckFirstParameters(task);

		AreaParameters parameters = orikaMapper.getMapperFacade().map(firstStepParameters, AreaParameters.class);

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

	protected Map<String, Object> getAndCheckFirstParameters(UserTasks task) {

		Map<String, Object> firstStepParameters = getIniParameters(task);

		if ((firstStepParameters.get("taskId") == null) || (firstStepParameters.get("activityId") == null)
				|| (firstStepParameters.get("fileName") == null)) {

			LOGGER.error("Intentando acceder a parámetros no seteados");
			throw new JobExecutionException(task.getId());
		}

		return firstStepParameters;
	}
}