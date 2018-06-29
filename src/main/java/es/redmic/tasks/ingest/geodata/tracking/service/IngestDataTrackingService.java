package es.redmic.tasks.ingest.geodata.tracking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.redmic.tasks.common.service.TaskJobWithInterventionService;
import es.redmic.tasks.ingest.geodata.tracking.jobs.TrackingMatchingParametersValidator;
import es.redmic.tasks.ingest.geodata.tracking.repository.IngestDataTrackingRepository;
import es.redmic.tasks.ingest.model.matching.tracking.dto.TrackingMatching;

@Service
public class IngestDataTrackingService extends TaskJobWithInterventionService<TrackingMatching> {

	@Autowired
	public IngestDataTrackingService(IngestDataTrackingRepository ingestDataTrackingRepository,
			TrackingMatchingParametersValidator trackingMatchingParametersValidator) {
		super(ingestDataTrackingRepository, trackingMatchingParametersValidator);
	}
}