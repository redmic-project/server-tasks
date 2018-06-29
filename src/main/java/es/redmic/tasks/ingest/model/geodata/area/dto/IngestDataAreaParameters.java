package es.redmic.tasks.ingest.model.geodata.area.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaIgnore;

import es.redmic.tasks.ingest.model.common.dto.TaskParametersBase;

public class IngestDataAreaParameters extends TaskParametersBase {

	@JsonSchemaIgnore
	@JsonIgnore
	public String getDelimiter() {
		return null;
	}
}