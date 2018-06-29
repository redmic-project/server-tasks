package es.redmic.tasks.ingest.model.data.document.dto;

import es.redmic.tasks.ingest.model.common.dto.RunTaskWithParametersDTO;

public class RunTaskIngestDataDocumentDTO extends RunTaskWithParametersDTO<IngestDataDocumentParameters> {

	
	private static final long serialVersionUID = -5563016785693018119L;

	public RunTaskIngestDataDocumentDTO() {
		super(IngestDataDocumentParameters.class);
	}
}
