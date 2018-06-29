package es.redmic.tasks.ingest.data.document.jobs;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import es.redmic.tasks.ingest.common.jobs.ParametersBase;
import es.redmic.tasks.ingest.model.matching.data.document.dto.DocumentMatching;

public class DocumentParameters extends ParametersBase {

	@NotNull
	String delimiter;

	@NotNull
	@Valid
	private DocumentMatching matching;

	public DocumentParameters() {
	}

	public DocumentMatching getMatching() {
		return matching;
	}

	public void setMatching(DocumentMatching matching) {
		this.matching = matching;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
}