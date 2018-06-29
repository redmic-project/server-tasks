package es.redmic.tasks.ingest.geodata.area.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.redmic.tasks.common.listener.TaskStepListener;
import es.redmic.tasks.ingest.geodata.area.jobs.AreaParameters;
import es.redmic.tasks.ingest.geodata.area.service.IngestDataAreaService;

@Component
public class IngestDataAreaStepListener extends TaskStepListener<AreaParameters> {

	@Autowired
	public IngestDataAreaStepListener(IngestDataAreaService ingestDataAreaService) {
		super(ingestDataAreaService);
	}
}