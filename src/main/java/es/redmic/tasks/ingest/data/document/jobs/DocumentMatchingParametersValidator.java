package es.redmic.tasks.ingest.data.document.jobs;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import es.redmic.tasks.common.utils.JobUtils;
import es.redmic.tasks.ingest.common.jobs.MatchingParametersValidator;

@Component
@Transactional
public class DocumentMatchingParametersValidator extends MatchingParametersValidator {

	public DocumentMatchingParametersValidator() {
	}

	public void validate(JobParameters jobParameters) {

		JobParameter parameters = jobParameters.getParameters().get(JOB_PARAMETER_KEY);
		DocumentParameters documentParameters = JobUtils.JobParameters2UserParameters(parameters,
				DocumentParameters.class);

		checkConstraints(documentParameters);
	}

	protected void checkConstraints(DocumentParameters documentParameters) {

		checkDataBindingConstraints(documentParameters);
		checkHeader(documentParameters.getTaskId(), documentParameters.getHeader(),
				documentParameters.getMatching().getMatchingColumns());
		checkFileConstraints(documentParameters.getTaskId(), documentParameters.getHeader(),
				documentParameters.getMatching().getMatchingColumns());
	}
}