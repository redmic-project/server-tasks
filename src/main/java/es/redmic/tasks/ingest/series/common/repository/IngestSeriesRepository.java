package es.redmic.tasks.ingest.series.common.repository;

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
