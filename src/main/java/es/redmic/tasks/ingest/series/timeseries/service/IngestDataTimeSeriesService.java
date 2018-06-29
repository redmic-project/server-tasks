package es.redmic.tasks.ingest.series.timeseries.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.redmic.tasks.ingest.model.matching.series.timeseries.dto.TimeSeriesMatching;
import es.redmic.tasks.ingest.series.common.service.IngestSeriesService;
import es.redmic.tasks.ingest.series.timeseries.jobs.TimeSeriesMatchingParametersValidator;
import es.redmic.tasks.ingest.series.timeseries.repository.IngestTimeSeriesRepository;

@Service
public class IngestDataTimeSeriesService extends IngestSeriesService<TimeSeriesMatching> {

	@Autowired
	public IngestDataTimeSeriesService(IngestTimeSeriesRepository ingestTimeSeriesRepository,
			TimeSeriesMatchingParametersValidator timeSeriesMatchingParametersValidator) {
		super(ingestTimeSeriesRepository, timeSeriesMatchingParametersValidator);
	}
}