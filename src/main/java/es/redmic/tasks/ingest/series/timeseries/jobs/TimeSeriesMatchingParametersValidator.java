package es.redmic.tasks.ingest.series.timeseries.jobs;

/*-
 * #%L
 * Tasks
 * %%
 * Copyright (C) 2019 REDMIC Project / Server
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
