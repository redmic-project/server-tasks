package es.redmic.tasks.ingest.data.document.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.redmic.tasks.common.service.TaskJobWithInterventionService;
import es.redmic.tasks.ingest.data.document.jobs.DocumentMatchingParametersValidator;
import es.redmic.tasks.ingest.data.document.repository.IngestDataDocumentRepository;
import es.redmic.tasks.ingest.model.matching.data.document.dto.DocumentMatching;

@Service
public class IngestDataDocumentService extends TaskJobWithInterventionService<DocumentMatching> {

	@Autowired
	public IngestDataDocumentService(IngestDataDocumentRepository ingestDataDocumentRepository,
			DocumentMatchingParametersValidator documentMatchingParametersValidator) {
		super(ingestDataDocumentRepository, documentMatchingParametersValidator);

	}
}