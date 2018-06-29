package es.redmic.tasks.common.converter;

import java.util.Locale;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import es.redmic.exception.common.ExceptionTypeItfc;
import es.redmic.exception.tasks.ingest.IngestBaseException;
import es.redmic.tasks.common.repository.UserTasksRepository;
import es.redmic.tasks.ingest.model.status.common.TaskError;
import es.redmic.tasks.ingest.model.status.common.TaskStatus;
import es.redmic.tasks.ingest.model.status.dto.FailedTaskDTO;
import es.redmic.tasks.ingest.model.status.model.UserTasks;
import es.redmic.tasks.ingest.model.step.model.FailedStep;
import es.redmic.tasks.ingest.model.step.model.RegisteredStep;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.metadata.Type;

@Component
public class ExceptionToFailedTaskDTOConverter extends CustomConverter<IngestBaseException, FailedTaskDTO> {

	Locale locale = LocaleContextHolder.getLocale();

	@Autowired
	protected UserTasksRepository repository;

	@Autowired
	private MessageSource messageSource;

	@Override
	public FailedTaskDTO convert(IngestBaseException source, Type<? extends FailedTaskDTO> destinationType) {

		UserTasks task = (UserTasks) repository.findById(source.getTaskId()).get_source();
		task.setStatus(TaskStatus.FAILED);

		FailedStep step = new FailedStep();

		TaskError failedStepError = new TaskError();

		ExceptionTypeItfc code = source.getCode();
		if (code != null) {
			String codeStr = code.toString();
			failedStepError.setCode(codeStr);

			try {
				failedStepError.setDescription(messageSource.getMessage(codeStr, null, locale) + " "
						+ messageSource.getMessage("DefaultMessage", null, locale));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		failedStepError.setExceptionId(source.getTypeId().toString());

		step.setError(failedStepError);

		task.addStep(step);

		FailedTaskDTO dto = mapperFacade.map(task, FailedTaskDTO.class);

		RegisteredStep firstStep = (RegisteredStep) task.getStep(0);
		dto.setParameters(firstStep.getParameters());

		dto.setEndDate(new DateTime());
		dto.setUpdated(dto.getEndDate());
		dto.setProgress(0);

		return dto;
	}
}