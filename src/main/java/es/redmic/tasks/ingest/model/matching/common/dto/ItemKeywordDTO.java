package es.redmic.tasks.ingest.model.matching.common.dto;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaIgnore;

public class ItemKeywordDTO extends ItemStringNullableDTO {

	private static final String FIELD = "keyword";

	public ItemKeywordDTO() {
	}

	@JsonSchemaIgnore
	@JsonIgnore
	@Override
	public String getField() {
		return FIELD;
	}
}