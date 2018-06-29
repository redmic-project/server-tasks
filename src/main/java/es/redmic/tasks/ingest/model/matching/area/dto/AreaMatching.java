package es.redmic.tasks.ingest.model.matching.area.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaNotNull;

import es.redmic.tasks.ingest.model.matching.common.dto.ItemAreaTypeDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemCodeNullableDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemDescriptionDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemNameDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemRemarkDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.MatchingCommonDTO;

@JsonSchemaNotNull
public class AreaMatching extends MatchingCommonDTO {

	@NotNull
	@Valid
	private ItemAreaTypeDTO areaType;

	@Valid
	private ItemNameDTO name;

	@Valid
	private ItemCodeNullableDTO code;

	@Valid
	private ItemDescriptionDTO description;

	@Valid
	private ItemRemarkDTO remark;

	public AreaMatching() {
		super();
	}

	public ItemAreaTypeDTO getAreaType() {
		return areaType;
	}

	public void setAreaType(ItemAreaTypeDTO areaType) {
		this.areaType = areaType;
	}

	public ItemNameDTO getName() {
		return name;
	}

	public void setName(ItemNameDTO name) {
		this.name = name;

		if (name != null)
			matchingColumns.addAll(name.getColumns());
	}

	public ItemCodeNullableDTO getCode() {
		return code;
	}

	public void setCode(ItemCodeNullableDTO code) {
		this.code = code;

		if (code != null)
			matchingColumns.addAll(code.getColumns());
	}

	public ItemDescriptionDTO getDescription() {
		return description;
	}

	public void setDescription(ItemDescriptionDTO description) {
		this.description = description;

		if (description != null)
			matchingColumns.addAll(description.getColumns());
	}

	public ItemRemarkDTO getRemark() {
		return remark;
	}

	public void setRemark(ItemRemarkDTO remark) {
		this.remark = remark;

		if (remark != null)
			matchingColumns.addAll(remark.getColumns());
	}
}