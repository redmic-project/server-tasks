package es.redmic.tasks.common.listener;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import es.redmic.exception.tasks.ingest.IngestBaseException;
import es.redmic.exception.tasks.ingest.JobExecutionException;
import es.redmic.tasks.common.jobs.ParametersCommons;
import es.redmic.tasks.common.service.TaskJobBaseService;
import es.redmic.tasks.common.utils.JobUtils;

public abstract class TaskStepListener<TParameters extends ParametersCommons> implements StepExecutionListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskStepListener.class);
	private static final Object JOB_PARAMETER_KEY = "parameters";

	private TaskJobBaseService taskService;

	private Class<TParameters> typeOfTParameters;

	@SuppressWarnings("unchecked")
	public TaskStepListener(TaskJobBaseService taskService) {
		super();
		this.taskService = taskService;
		this.typeOfTParameters = (Class<TParameters>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		LOGGER.info("Before step: " + stepExecution.getReadCount());
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		LOGGER.info("After step: " + stepExecution.getSummary());

		List<Throwable> exceptions = stepExecution.getFailureExceptions();
		if (exceptions.isEmpty()) {
			return ExitStatus.COMPLETED;
		}

		Throwable ex = ExceptionUtils.getRootCause(exceptions.get(0));

		IngestBaseException exception;

		if (ex instanceof IngestBaseException) {
			exception = (IngestBaseException) ex;
		} else {
			JobParameter parameter = stepExecution.getJobParameters().getParameters().get(JOB_PARAMETER_KEY);

			TParameters parameters = JobUtils.JobParameters2UserParameters(parameter, typeOfTParameters);

			exception = new JobExecutionException(parameters.getTaskId());
		}

		taskService.setFailed(exception);

		return ExitStatus.FAILED;
	}
}
