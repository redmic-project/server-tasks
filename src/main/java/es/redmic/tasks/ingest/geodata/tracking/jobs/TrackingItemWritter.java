package es.redmic.tasks.ingest.geodata.tracking.jobs;

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

import org.springframework.batch.item.ItemWriter;

import es.redmic.db.geodata.tracking.animal.service.AnimalTrackingService;
import es.redmic.db.geodata.tracking.platform.service.PlatformTrackingService;
import es.redmic.es.config.OrikaScanBeanESItfc;
import es.redmic.exception.tasks.ingest.IngestPersistenceDataException;
import es.redmic.models.es.administrative.dto.PlatformCompactDTO;
import es.redmic.models.es.administrative.taxonomy.dto.AnimalCompactDTO;
import es.redmic.models.es.geojson.tracking.animal.dto.AnimalTrackingDTO;
import es.redmic.models.es.geojson.tracking.common.ElementTrackingDTO;
import es.redmic.models.es.geojson.tracking.platform.dto.PlatformTrackingDTO;

public class TrackingItemWritter implements ItemWriter<List<ElementTrackingDTO>> {

	AnimalTrackingService animalTrackingService;

	PlatformTrackingService platformTrackingService;

	OrikaScanBeanESItfc orikaMapper;

	private String taskId;

	public TrackingItemWritter(AnimalTrackingService animalTrackingService,
			PlatformTrackingService platformTrackingService, OrikaScanBeanESItfc orikaMapper, String taskId) {

		this.animalTrackingService = animalTrackingService;
		this.platformTrackingService = platformTrackingService;
		this.orikaMapper = orikaMapper;
		this.taskId = taskId;
	}

	@Override
	public void write(List<? extends List<ElementTrackingDTO>> rows) {

		try {
			rows.forEach(row -> row.forEach(item -> saveItem(item)));
		} catch (Exception e) {
			throw new IngestPersistenceDataException(taskId, e);
		}
	}

	private void saveItem(ElementTrackingDTO item) {

		Object elementDTO = item.getProperties().getElement();

		if (elementDTO == null) {
			throw new IngestPersistenceDataException(taskId);
		}

		if (elementDTO instanceof PlatformCompactDTO) {

			PlatformTrackingDTO platformTrackingDTO = orikaMapper.getMapperFacade().map(item,
					PlatformTrackingDTO.class);
			platformTrackingDTO.getProperties().setPlatform((PlatformCompactDTO) elementDTO);
			platformTrackingService.save(platformTrackingDTO);
		} else if (elementDTO instanceof AnimalCompactDTO) {

			AnimalTrackingDTO animalTrackingDTO = orikaMapper.getMapperFacade().map(item, AnimalTrackingDTO.class);
			animalTrackingDTO.getProperties().setAnimal((AnimalCompactDTO) elementDTO);
			animalTrackingService.save(animalTrackingDTO);
		} else {
			throw new IngestPersistenceDataException(taskId);
		}
	}
}
