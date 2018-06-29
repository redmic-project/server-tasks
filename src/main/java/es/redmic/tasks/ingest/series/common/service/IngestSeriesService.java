package es.redmic.tasks.ingest.series.common.service;

import es.redmic.tasks.common.repository.TaskBaseWithInterventionRepository;
import es.redmic.tasks.common.service.TaskJobWithInterventionService;
import es.redmic.tasks.ingest.model.matching.common.dto.MatchingBaseDTO;
import es.redmic.tasks.ingest.series.common.jobs.SeriesMatchingParametersValidator;

public abstract class IngestSeriesService<TDTO extends MatchingBaseDTO> extends TaskJobWithInterventionService<TDTO> {

	public IngestSeriesService(TaskBaseWithInterventionRepository<TDTO> intestRepository,
			SeriesMatchingParametersValidator validator) {
		super(intestRepository, validator);
	}
}
