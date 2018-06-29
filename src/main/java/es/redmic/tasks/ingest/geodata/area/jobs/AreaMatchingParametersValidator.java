package es.redmic.tasks.ingest.geodata.area.jobs;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import es.redmic.db.geodata.area.service.AreaService;
import es.redmic.tasks.common.utils.JobUtils;
import es.redmic.tasks.ingest.common.jobs.MatchingParametersValidator;

@Component
@Transactional
public class AreaMatchingParametersValidator extends MatchingParametersValidator {

	@Autowired
	AreaService areaService;

	public AreaMatchingParametersValidator() {
	}

	public void validate(JobParameters jobParameters) {

		JobParameter parameters = jobParameters.getParameters().get(JOB_PARAMETER_KEY);
		AreaParameters areaParameters = JobUtils.JobParameters2UserParameters(parameters, AreaParameters.class);

		checkConstraints(areaParameters);
		beforeResumeJob(areaParameters);
	}

	protected void checkConstraints(AreaParameters areaParameters) {

		checkDataBindingConstraints(areaParameters);
		checkHeader(areaParameters.getTaskId(), areaParameters.getHeader(),
				areaParameters.getMatching().getMatchingColumns());
		checkFileConstraints(areaParameters.getTaskId(), areaParameters.getHeader(),
				areaParameters.getMatching().getMatchingColumns());
	}

	public void beforeResumeJob(AreaParameters areaParameters) {

		areaService.checkDataType(areaParameters.getActivityId());
	}
}