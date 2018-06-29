package es.redmic.tasks.ingest.data.document.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.redmic.tasks.common.listener.TaskListener;
import es.redmic.tasks.ingest.data.document.jobs.DocumentParameters;
import es.redmic.tasks.ingest.data.document.service.IngestDataDocumentService;

@Component
public class IngestDataDocumentListener extends TaskListener<DocumentParameters> {

	@Autowired
	public IngestDataDocumentListener(IngestDataDocumentService ingestDataDocumentService) {
		super(ingestDataDocumentService);
	}
}