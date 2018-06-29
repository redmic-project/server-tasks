package es.redmic.tasks.ingest.series.timeseries.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.redmic.tasks.common.listener.TaskListener;
import es.redmic.tasks.ingest.series.timeseries.jobs.TimeSeriesParameters;
import es.redmic.tasks.ingest.series.timeseries.service.IngestDataTimeSeriesService;

@Component
public class IngestDataTimeSeriesListener extends TaskListener<TimeSeriesParameters> {

	@Autowired
	public IngestDataTimeSeriesListener(IngestDataTimeSeriesService ingestTimeSeriesService){
		super(ingestTimeSeriesService);
	}
}
