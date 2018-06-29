package es.redmic.tasks.ingest.geodata.area.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.redmic.tasks.common.service.TaskJobWithInterventionService;
import es.redmic.tasks.ingest.geodata.area.jobs.AreaMatchingParametersValidator;
import es.redmic.tasks.ingest.geodata.area.repository.IngestDataAreaRepository;
import es.redmic.tasks.ingest.model.matching.area.dto.AreaMatching;

@Service
public class IngestDataAreaService extends TaskJobWithInterventionService<AreaMatching> {

	@Autowired
	public IngestDataAreaService(IngestDataAreaRepository ingestDataAreaRepository,
			AreaMatchingParametersValidator areaMatchingParametersValidator) {
		super(ingestDataAreaRepository, areaMatchingParametersValidator);
	}
}