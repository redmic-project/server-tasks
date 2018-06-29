package es.redmic.tasks.ingest.series.objectcollectingseries.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.redmic.tasks.ingest.model.matching.series.objectcollectingseries.dto.ObjectCollectingSeriesMatching;
import es.redmic.tasks.ingest.series.common.service.IngestSeriesService;
import es.redmic.tasks.ingest.series.objectcollectingseries.jobs.ObjectCollectingSeriesMatchingParametersValidator;
import es.redmic.tasks.ingest.series.objectcollectingseries.repository.IngestObjectCollectingSeriesRepository;

@Service
public class IngestDataObjectCollectingSeriesService extends IngestSeriesService<ObjectCollectingSeriesMatching> {

	@Autowired
	public IngestDataObjectCollectingSeriesService(
			IngestObjectCollectingSeriesRepository ingestObjectCollectingSeriesRepository,
			ObjectCollectingSeriesMatchingParametersValidator objectCollectingSeriesMatchingParametersValidator) {
		super(ingestObjectCollectingSeriesRepository, objectCollectingSeriesMatchingParametersValidator);
	}
}