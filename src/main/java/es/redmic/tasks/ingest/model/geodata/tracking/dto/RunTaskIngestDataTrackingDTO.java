package es.redmic.tasks.ingest.model.geodata.tracking.dto;

import es.redmic.tasks.ingest.model.common.dto.RunTaskWithParametersDTO;

public class RunTaskIngestDataTrackingDTO extends RunTaskWithParametersDTO<IngestDataTrackingParameters> {

	
	private static final long serialVersionUID = -5563016785693018119L;

	public RunTaskIngestDataTrackingDTO() {
		super(IngestDataTrackingParameters.class);
	}
}
