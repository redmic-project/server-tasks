package es.redmic.tasks.ingest.model.matching.common.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public abstract class MatchingBaseDTO extends MatchingCommonDTO {

	@NotNull
	@Valid
	private ItemDateDTO date;

	@Valid
	private ItemQFlagDTO qFlag;

	@Valid
	private ItemVFlagDTO vFlag;

	public MatchingBaseDTO() {
	}

	public ItemDateDTO getDate() {
		return date;
	}

	public void setDate(ItemDateDTO date) {
		this.date = date;
		matchingColumns.addAll(date.getColumns());
	}

	@JsonGetter("qFlag")
	public ItemQFlagDTO getQFlag() {
		return qFlag;
	}

	@JsonSetter("qFlag")
	public void setQFlag(ItemQFlagDTO qFlag) {

		this.qFlag = qFlag;

		if (qFlag != null)
			matchingColumns.addAll(qFlag.getColumns());
	}

	@JsonGetter("vFlag")
	public ItemVFlagDTO getVFlag() {
		return vFlag;
	}

	@JsonSetter("vFlag")
	public void setVFlag(ItemVFlagDTO vFlag) {

		this.vFlag = vFlag;

		if (vFlag != null)
			matchingColumns.addAll(vFlag.getColumns());
	}
}
