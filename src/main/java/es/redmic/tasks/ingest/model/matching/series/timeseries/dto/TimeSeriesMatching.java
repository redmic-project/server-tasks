package es.redmic.tasks.ingest.model.matching.series.timeseries.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaNotNull;

import es.redmic.tasks.ingest.model.matching.common.dto.ItemCommonDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemParameterDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemRemarkDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.MatchingBaseDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ParametersDTO;

@JsonSchemaNotNull
public class TimeSeriesMatching extends MatchingBaseDTO {
	
	@NotNull
	@Valid
	private ParametersDTO parameters;
	
	@Valid
	private ItemRemarkDTO remark;

	public ParametersDTO getParameters() {
		return parameters;
	}

	public void setParameters(ParametersDTO parameters) {
		this.parameters = parameters;
		for (ItemParameterDTO item : parameters.getMatching())
			matchingColumns.addAll(item.getColumns());
	}
	
	public ItemRemarkDTO getRemark() {
		return remark;
	}

	public void setRemark(ItemRemarkDTO remark) {
		this.remark = remark;
		if (remark != null)
			matchingColumns.addAll(remark.getColumns());
	}
	
	@SuppressWarnings("serial")
	@JsonIgnore
	public List<ItemCommonDTO> getItemsCommon() {
		
		return new ArrayList<ItemCommonDTO>() {{
			add(getDate());
			if (getRemark() != null) add(getRemark());
			if (getQFlag() != null) add(getQFlag());
			if (getVFlag() != null) add(getVFlag());
		}};
	}
}