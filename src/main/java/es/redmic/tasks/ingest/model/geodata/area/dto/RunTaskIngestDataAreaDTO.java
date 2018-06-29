package es.redmic.tasks.ingest.model.geodata.area.dto;

import es.redmic.tasks.ingest.model.common.dto.RunTaskWithParametersDTO;

public class RunTaskIngestDataAreaDTO extends RunTaskWithParametersDTO<IngestDataAreaParameters> {

	
	private static final long serialVersionUID = -5563016785693018119L;

	public RunTaskIngestDataAreaDTO() {
		super(IngestDataAreaParameters.class);
	}
}
