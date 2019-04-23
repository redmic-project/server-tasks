package es.redmic.tasks.ingest.series.common.jobs;

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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.redmic.es.geodata.geofixedstation.service.GeoFixedBaseESService;
import es.redmic.exception.tasks.ingest.IngestMatchingDataDefinitionException;
import es.redmic.models.es.geojson.common.model.Feature;
import es.redmic.models.es.geojson.properties.model.GeoDataProperties;
import es.redmic.models.es.geojson.properties.model.Measurement;
import es.redmic.models.es.maintenance.parameter.model.DataDefinition;
import es.redmic.tasks.ingest.common.jobs.MatchingParametersValidator;

public abstract class SeriesMatchingParametersValidator extends MatchingParametersValidator {

	private final Logger LOGGER = LoggerFactory.getLogger(SeriesMatchingParametersValidator.class);

	GeoFixedBaseESService<?, ?> geoFixedSeriesBaseESService;

	public SeriesMatchingParametersValidator(GeoFixedBaseESService<?, ?> geoFixedSeriesBaseESService) {
		this.geoFixedSeriesBaseESService = geoFixedSeriesBaseESService;
	}

	protected void checkDataDefinitionConstraints(String taskId, List<String> dataDefinitionIds, String surveyId,
			String activityId) {

		Feature<GeoDataProperties, ?> geoFixedTimeSeries = geoFixedSeriesBaseESService.findById(surveyId, activityId);

		List<Measurement> measurements = geoFixedTimeSeries.getProperties().getMeasurements();

		for (int i = 0; i < measurements.size(); i++) {
			DataDefinition dataDefinition = measurements.get(i).getDataDefinition();
			if (dataDefinition == null) {
				LOGGER.error("Especificado dataDefinition que no se encuentra en los datos de la survey");
				throw new IngestMatchingDataDefinitionException(taskId);
			}
			int pos = dataDefinitionIds.indexOf(dataDefinition.getId().toString());
			if (pos != -1)
				dataDefinitionIds.remove(pos);
		}

		if (dataDefinitionIds.size() > 0) {
			LOGGER.error("Especificado dataDefinition que no se encuentra en los datos de la survey");
			throw new IngestMatchingDataDefinitionException(taskId);
		}
	}
}
