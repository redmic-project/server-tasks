package es.redmic.tasks.ingest.model.data.document.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaIgnore;

import es.redmic.tasks.ingest.model.common.dto.TaskParametersBase;

public class IngestDataDocumentParameters extends TaskParametersBase {

	@Override
	@JsonSchemaIgnore
	@JsonIgnore
	public String getActivityId() {
		return null;
	}
}