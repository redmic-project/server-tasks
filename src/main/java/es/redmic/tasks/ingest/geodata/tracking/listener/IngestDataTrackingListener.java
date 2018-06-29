package es.redmic.tasks.ingest.geodata.tracking.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.redmic.tasks.common.listener.TaskListener;
import es.redmic.tasks.ingest.geodata.tracking.jobs.TrackingParameters;
import es.redmic.tasks.ingest.geodata.tracking.service.IngestDataTrackingService;

@Component
public class IngestDataTrackingListener extends TaskListener<TrackingParameters> {
	
	@Autowired
	public IngestDataTrackingListener(IngestDataTrackingService ingestDataTrackingService) {
		super(ingestDataTrackingService);
	}
}