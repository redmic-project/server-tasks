package es.redmic.tasks.ingest.geodata.area.jobs;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import es.redmic.tasks.ingest.common.jobs.Parameters;
import es.redmic.tasks.ingest.model.matching.area.dto.AreaMatching;

public class AreaParameters extends Parameters {

	@NotNull
	@Valid
	private AreaMatching matching;

	public AreaParameters() {
	}

	public AreaMatching getMatching() {
		return matching;
	}

	public void setMatching(AreaMatching matching) {
		this.matching = matching;
	}
}