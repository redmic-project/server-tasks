package es.redmic.tasks.ingest.series.timeseries.jobs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.redmic.es.geodata.geofixedstation.service.GeoFixedTimeSeriesESService;
import es.redmic.tasks.common.utils.JobUtils;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemParameterDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ParametersDTO;
import es.redmic.tasks.ingest.series.common.jobs.SeriesMatchingParametersValidator;

@Component
public class TimeSeriesMatchingParametersValidator extends SeriesMatchingParametersValidator {

	@Autowired
	public TimeSeriesMatchingParametersValidator(GeoFixedTimeSeriesESService geoFixedTimeSeriesESService) {
		super(geoFixedTimeSeriesESService);
	}

	public void validate(JobParameters jobParameters) {

		JobParameter parameters = jobParameters.getParameters().get(JOB_PARAMETER_KEY);
		TimeSeriesParameters seriesParameters = JobUtils.JobParameters2UserParameters(parameters,
				TimeSeriesParameters.class);

		checkConstraints(seriesParameters);
	}

	@SuppressWarnings("serial")
	protected void checkConstraints(TimeSeriesParameters seriesParameters) {

		checkDataBindingConstraints(seriesParameters);
		checkHeader(seriesParameters.getTaskId(), seriesParameters.getHeader(),
			seriesParameters.getMatching().getMatchingColumns());

		checkFileConstraints(seriesParameters.getTaskId(), seriesParameters.getHeader(),
			seriesParameters.getMatching().getMatchingColumns());

		List<String> dataDefinitionIds = new ArrayList<String>() {{
			ParametersDTO parametersMatching = seriesParameters.getMatching().getParameters();
			for (ItemParameterDTO item : parametersMatching.getMatching())
				add(item.getDataDefinitionId().toString());
		}};

		checkDataDefinitionConstraints(seriesParameters.getTaskId(), dataDefinitionIds,
			seriesParameters.getSurveyId(), seriesParameters.getActivityId());
	}
}