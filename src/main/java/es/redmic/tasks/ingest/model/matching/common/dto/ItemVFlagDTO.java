package es.redmic.tasks.ingest.model.matching.common.dto;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaIgnore;

public class ItemVFlagDTO extends ItemCharBaseDTO {

	private static final String FIELD = "vFlag";

	public ItemVFlagDTO() {
	}

	@JsonSchemaIgnore
	@JsonIgnore
	@Override
	public String getField() {
		return FIELD;
	}
}