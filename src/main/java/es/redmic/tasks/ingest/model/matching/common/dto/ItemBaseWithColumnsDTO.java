package es.redmic.tasks.ingest.model.matching.common.dto;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaUniqueItemsByRequiredProperties;

public abstract class ItemBaseWithColumnsDTO extends ItemBaseDTO {
	
	@JsonSchemaUniqueItemsByRequiredProperties
	@NotNull
	@Size(min = 1, max=1)
	protected List<String> columns;

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}
}
