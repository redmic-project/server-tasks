package es.redmic.tasks.ingest.series.objectcollectingseries.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.redmic.tasks.common.listener.TaskListener;
import es.redmic.tasks.ingest.series.objectcollectingseries.jobs.ObjectCollectingSeriesParameters;
import es.redmic.tasks.ingest.series.objectcollectingseries.service.IngestDataObjectCollectingSeriesService;

@Component
public class IngestDataObjectCollectingSeriesListener extends TaskListener<ObjectCollectingSeriesParameters> {

	@Autowired
	public IngestDataObjectCollectingSeriesListener(IngestDataObjectCollectingSeriesService ingestObjectCollectingSeriesService){
		super(ingestObjectCollectingSeriesService);
	}
}