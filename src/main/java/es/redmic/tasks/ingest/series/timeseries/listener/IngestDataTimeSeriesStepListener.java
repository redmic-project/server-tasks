package es.redmic.tasks.ingest.series.timeseries.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.redmic.tasks.common.listener.TaskStepListener;
import es.redmic.tasks.ingest.series.timeseries.jobs.TimeSeriesParameters;
import es.redmic.tasks.ingest.series.timeseries.service.IngestDataTimeSeriesService;

@Component
public class IngestDataTimeSeriesStepListener extends TaskStepListener<TimeSeriesParameters> {
	
	@Autowired
	public IngestDataTimeSeriesStepListener(IngestDataTimeSeriesService ingestDataTimeSeriesService) {
		super(ingestDataTimeSeriesService);
	}
}