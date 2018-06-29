package es.redmic.tasks.ingest.data.document.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.redmic.tasks.common.listener.TaskStepListener;
import es.redmic.tasks.ingest.data.document.jobs.DocumentParameters;
import es.redmic.tasks.ingest.data.document.service.IngestDataDocumentService;

@Component
public class IngestDataDocumentStepListener extends TaskStepListener<DocumentParameters> {

	@Autowired
	public IngestDataDocumentStepListener(IngestDataDocumentService ingestDataDocumentService) {
		super(ingestDataDocumentService);
	}
}