package es.redmic.tasks.ingest.model.geodata.tracking.dto;

import javax.validation.constraints.NotNull;

import es.redmic.tasks.ingest.model.common.dto.TaskParametersBase;


public class IngestDataTrackingParameters extends TaskParametersBase {
	
	@NotNull
	private String elementUuid;
	
	public IngestDataTrackingParameters() {
		super();
	}

	public String getElementUuid() {
		return elementUuid;
	}

	public void setElementUuid(String elementUuid) {
		this.elementUuid = elementUuid;
	}
}