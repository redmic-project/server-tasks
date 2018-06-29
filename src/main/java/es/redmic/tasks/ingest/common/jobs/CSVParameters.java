package es.redmic.tasks.ingest.common.jobs;

import javax.validation.constraints.NotNull;

public abstract class CSVParameters extends Parameters {

	@NotNull
	String delimiter;

	public CSVParameters() {

	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
}