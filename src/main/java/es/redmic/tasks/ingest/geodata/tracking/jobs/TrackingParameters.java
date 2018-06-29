package es.redmic.tasks.ingest.geodata.tracking.jobs;

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