package es.redmic.tasks.ingest.series.objectcollectingseries.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.redmic.tasks.common.listener.TaskStepListener;
import es.redmic.tasks.ingest.series.objectcollectingseries.jobs.ObjectCollectingSeriesParameters;
import es.redmic.tasks.ingest.series.objectcollectingseries.service.IngestDataObjectCollectingSeriesService;

@Component
public class IngestDataObjectCollectingSeriesStepListener extends TaskStepListener<ObjectCollectingSeriesParameters> {
	
	@Autowired
	public IngestDataObjectCollectingSeriesStepListener(IngestDataObjectCollectingSeriesService ingestObjectCollectingSeriesService) {
		super(ingestObjectCollectingSeriesService);
	}
}