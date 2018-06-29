package es.redmic.tasks.ingest.series.objectcollectingseries.jobs;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import es.redmic.tasks.ingest.model.matching.series.objectcollectingseries.dto.ObjectCollectingSeriesMatching;
import es.redmic.tasks.ingest.series.common.jobs.SeriesParameters;

public class ObjectCollectingSeriesParameters extends SeriesParameters {

	@NotNull
	@Valid
	private ObjectCollectingSeriesMatching matching;

	public ObjectCollectingSeriesMatching getMatching() {
		return matching;
	}

	public void setMatching(ObjectCollectingSeriesMatching matching) {
		this.matching = matching;
	}
}
