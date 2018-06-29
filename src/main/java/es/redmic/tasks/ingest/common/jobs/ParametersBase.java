package es.redmic.tasks.ingest.common.jobs;

import java.util.List;

import javax.validation.constraints.NotNull;

import es.redmic.tasks.common.jobs.ParametersCommons;

public abstract class ParametersBase extends ParametersCommons {

	@NotNull
	String fileName;

	@NotNull
	List<String> header;

	public ParametersBase() {

	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String filename) {
		this.fileName = filename;
	}

	public List<String> getHeader() {
		return header;
	}

	public void setHeader(List<String> header) {
		this.header = header;
	}
}