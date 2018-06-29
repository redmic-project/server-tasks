package es.redmic.tasks.ingest.model.series.dto;

import es.redmic.tasks.ingest.model.common.dto.RunTaskWithParametersDTO;

public class RunTaskIngestDataSeriesDTO extends RunTaskWithParametersDTO<IngestDataSeriesParameters> {

	
	private static final long serialVersionUID = -5563016785693018119L;

	public RunTaskIngestDataSeriesDTO() {
		super(IngestDataSeriesParameters.class);
	}
}
