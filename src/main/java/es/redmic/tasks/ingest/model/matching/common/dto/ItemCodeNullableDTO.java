package es.redmic.tasks.ingest.model.matching.common.dto;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaIgnore;

public class ItemCodeNullableDTO extends ItemStringNullableDTO {

	private static final String FIELD = "code";

	public ItemCodeNullableDTO() {
	}

	@JsonSchemaIgnore
	@JsonIgnore
	@Override
	public String getField() {
		return FIELD;
	}
}