package es.redmic.tasks.ingest.model.matching.common.dto;

import java.util.List;

import javax.validation.constraints.Size;

public abstract class ItemStringNullableDTO extends ItemStringDTO {

	public ItemStringNullableDTO() {
	}

	@Override
	@Size(min = 0, max = 1)
	public List<String> getColumns() {
		return columns;
	}
}