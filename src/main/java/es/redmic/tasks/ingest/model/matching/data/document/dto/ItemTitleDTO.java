package es.redmic.tasks.ingest.model.matching.data.document.dto;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaIgnore;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaNotNull;

import es.redmic.tasks.ingest.model.matching.common.dto.ItemStringDTO;

@JsonSchemaNotNull
public class ItemTitleDTO extends ItemStringDTO {

	private static final String FIELD = "title";

	public ItemTitleDTO() {
	}

	@JsonSchemaIgnore
	@JsonIgnore
	@Override
	public String getField() {
		return FIELD;
	}
}