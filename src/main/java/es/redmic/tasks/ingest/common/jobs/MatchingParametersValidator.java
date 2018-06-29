package es.redmic.tasks.ingest.common.jobs;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;

import es.redmic.exception.tasks.ingest.IngestMatchingColumnRequiredException;
import es.redmic.exception.tasks.ingest.IngestMatchingColumnSizeException;
import es.redmic.exception.tasks.ingest.MatchingDataBindingException;

public abstract class MatchingParametersValidator {

	protected final static String JOB_PARAMETER_KEY = "parameters";

	private final Logger LOGGER = LoggerFactory.getLogger(MatchingParametersValidator.class);

	protected static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	public MatchingParametersValidator() {
	}

	protected <TParameter> void checkDataBindingConstraints(TParameter parameters) {

		Set<ConstraintViolation<TParameter>> bindingErrors = validator.validate(parameters);
		if (bindingErrors.size() > 0) {
			for (ConstraintViolation<TParameter> it : bindingErrors) {
				throw new MatchingDataBindingException(it.getPropertyPath().toString(), it.getMessage(),
						((ParametersBase) parameters).getTaskId());
			}
		}
	}

	protected void checkHeader(String taskId, List<String> header, Set<String> columns) {

		if (header.size() < columns.size()) {
			LOGGER.error("Número de parámetros mayor que columnas en el CSV");
			throw new IngestMatchingColumnSizeException(taskId);
		}
	}

	protected void checkFileConstraints(String taskId, List<String> header, Set<String> columns) {

		for (String colName : columns) {
			if (!header.contains(colName)) {
				LOGGER.error("No existe la columna '" + colName + "' en el CSV");
				throw new IngestMatchingColumnRequiredException(taskId, colName);
			}
		}
	}

	public abstract void validate(JobParameters jobParameters);
}