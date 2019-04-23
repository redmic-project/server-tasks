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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import es.redmic.models.es.administrative.dto.PlatformCompactDTO;
import es.redmic.models.es.administrative.taxonomy.dto.AnimalCompactDTO;
import es.redmic.tasks.ingest.common.jobs.CSVParameters;
import es.redmic.tasks.ingest.model.matching.tracking.dto.TrackingMatching;

public class TrackingParameters extends CSVParameters {

	private AnimalCompactDTO animalDTO;

	private PlatformCompactDTO platformDTO;

	@NotNull
	@Valid
	private TrackingMatching matching;

	public TrackingParameters() {

	}

	public AnimalCompactDTO getAnimalDTO() {
		return animalDTO;
	}

	public void setAnimalDTO(AnimalCompactDTO animalDTO) {
		this.animalDTO = animalDTO;
	}

	public PlatformCompactDTO getPlatformDTO() {
		return platformDTO;
	}

	public void setPlatformDTO(PlatformCompactDTO platformDTO) {
		this.platformDTO = platformDTO;
	}

	public TrackingMatching getMatching() {
		return matching;
	}

	public void setMatching(TrackingMatching matching) {
		this.matching = matching;
	}
}
