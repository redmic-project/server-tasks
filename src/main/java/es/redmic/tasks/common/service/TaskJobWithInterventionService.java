package es.redmic.tasks.common.service;

import org.springframework.batch.core.JobParameters;

import es.redmic.tasks.common.repository.TaskBaseWithInterventionRepository;
import es.redmic.tasks.ingest.common.jobs.MatchingParametersValidator;
import es.redmic.tasks.ingest.model.intervention.matching.RequestUserInterventionMatchingTaskDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.MatchingCommonDTO;

public abstract class TaskJobWithInterventionService<TDTO extends MatchingCommonDTO> extends TaskJobBaseService {

	private TaskBaseWithInterventionRepository<TDTO> ingestRepository;

	MatchingParametersValidator validator;

	public TaskJobWithInterventionService(TaskBaseWithInterventionRepository<TDTO> intestRepository,
			MatchingParametersValidator validator) {
		super(intestRepository);
		this.ingestRepository = intestRepository;
		this.validator = validator;
	}

	public RequestUserInterventionMatchingTaskDTO preMatching(String taskId) {

		return ingestRepository.preMatching(taskId);
	}

	public void resume(String taskId, TDTO matching) {

		JobParameters newParameters = ingestRepository.resume(taskId, matching);
		validator.validate(newParameters);
		resumeJob(taskId, newParameters);
	}
}