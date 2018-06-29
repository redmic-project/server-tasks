package es.redmic.tasks.ingest.geodata.tracking.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.redmic.tasks.common.listener.TaskStepListener;
import es.redmic.tasks.ingest.geodata.tracking.jobs.TrackingParameters;
import es.redmic.tasks.ingest.geodata.tracking.service.IngestDataTrackingService;

@Component
public class IngestDataTrackingStepListener extends TaskStepListener<TrackingParameters> {
	
	@Autowired
	public IngestDataTrackingStepListener(IngestDataTrackingService ingestDataTrackingService) {
		super(ingestDataTrackingService);
	}
}