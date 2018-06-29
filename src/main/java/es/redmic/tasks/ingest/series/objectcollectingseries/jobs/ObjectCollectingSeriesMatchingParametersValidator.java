package es.redmic.tasks.ingest.series.objectcollectingseries.jobs;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.redmic.es.geodata.geofixedstation.service.GeoFixedObjectCollectingSeriesESService;
import es.redmic.es.maintenance.objects.service.ObjectTypeESService;
import es.redmic.exception.tasks.ingest.IngestMatchingClassificationException;
import es.redmic.tasks.common.utils.JobUtils;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemClassificationDTO;
import es.redmic.tasks.ingest.model.matching.series.objectcollectingseries.dto.ObjectCollectingSeriesMatching;
import es.redmic.tasks.ingest.series.common.jobs.SeriesMatchingParametersValidator;

@Component
public class ObjectCollectingSeriesMatchingParametersValidator extends SeriesMatchingParametersValidator {

	private final Logger LOGGER = LoggerFactory.getLogger(ObjectCollectingSeriesMatchingParametersValidator.class);

	@Autowired
	ObjectTypeESService objectTypeESService;

	@Autowired
	public ObjectCollectingSeriesMatchingParametersValidator(
			GeoFixedObjectCollectingSeriesESService geoFixedObjectCollectingSeriesESService) {
		super(geoFixedObjectCollectingSeriesESService);
	}

	public void validate(JobParameters jobParameters) {

		JobParameter parameters = jobParameters.getParameters().get(JOB_PARAMETER_KEY);
		ObjectCollectingSeriesParameters seriesParameters = JobUtils.JobParameters2UserParameters(parameters,
				ObjectCollectingSeriesParameters.class);

		checkConstraints(seriesParameters);
	}

	@SuppressWarnings("serial")
	protected void checkConstraints(ObjectCollectingSeriesParameters seriesParameters) {

		checkDataBindingConstraints(seriesParameters);
		checkHeader(seriesParameters.getTaskId(), seriesParameters.getHeader(),
				seriesParameters.getMatching().getMatchingColumns());

		checkFileConstraints(seriesParameters.getTaskId(), seriesParameters.getHeader(),
				seriesParameters.getMatching().getMatchingColumns());

		List<String> dataDefinitionIds = new ArrayList<String>() {
			{
				add(seriesParameters.getMatching().getParameter().getDataDefinitionId().toString());
			}
		};

		checkDataDefinitionConstraints(seriesParameters.getTaskId(), dataDefinitionIds, seriesParameters.getSurveyId(),
				seriesParameters.getActivityId());

		checkDataConstraints(seriesParameters);
	}

	protected void checkDataConstraints(ObjectCollectingSeriesParameters parameters) {

		ObjectCollectingSeriesMatching matching = parameters.getMatching();

		List<ItemClassificationDTO> classificationMatching = matching.getClassifications().getMatching();

		for (ItemClassificationDTO match : classificationMatching) {

			List<String> objectTypesIds = match.getClassifications();

			if (objectTypesIds == null || objectTypesIds.size() == 0) {
				LOGGER.error("No se ha especificado clasificación");
				throw new IngestMatchingClassificationException(parameters.getTaskId());
			}
		}
	}
}
