package es.redmic.tasks.ingest.geodata.area.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.redmic.tasks.common.listener.TaskListener;
import es.redmic.tasks.ingest.geodata.area.jobs.AreaParameters;
import es.redmic.tasks.ingest.geodata.area.service.IngestDataAreaService;

@Component
public class IngestDataAreaListener extends TaskListener<AreaParameters> {

	@Autowired
	public IngestDataAreaListener(IngestDataAreaService ingestDataAreaService) {
		super(ingestDataAreaService);
	}
}