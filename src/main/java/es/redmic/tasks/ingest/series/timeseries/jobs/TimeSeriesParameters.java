package es.redmic.tasks.ingest.series.timeseries.jobs;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import es.redmic.tasks.ingest.model.matching.series.timeseries.dto.TimeSeriesMatching;
import es.redmic.tasks.ingest.series.common.jobs.SeriesParameters;

public class TimeSeriesParameters extends SeriesParameters {

	
	@NotNull
	@Valid
	private TimeSeriesMatching matching;

	public TimeSeriesMatching getMatching() {
		return matching;
	}

	public void setMatching(TimeSeriesMatching matching) {
		this.matching = matching;
	}
}
