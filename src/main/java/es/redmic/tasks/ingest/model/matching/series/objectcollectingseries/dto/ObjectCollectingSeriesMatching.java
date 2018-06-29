package es.redmic.tasks.ingest.model.matching.series.objectcollectingseries.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaNotNull;

import es.redmic.tasks.ingest.model.matching.common.dto.ClassificationDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemClassificationDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemCommonDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemRemarkDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.MatchingBaseDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ParameterDTO;

@JsonSchemaNotNull
public class ObjectCollectingSeriesMatching extends MatchingBaseDTO {

	@NotNull
	@Valid
	private ParameterDTO parameter;
	
	@NotNull
	@Valid
	private ClassificationDTO classifications;
	
	@Valid
	private ItemRemarkDTO remark;

	public ParameterDTO getParameter() {
		return parameter;
	}

	public void setParameter(ParameterDTO parameter) {
		this.parameter = parameter;
	}

	public ClassificationDTO getClassifications() {
		return classifications;
	}

	public void setClassifications(ClassificationDTO classifications) {
		this.classifications = classifications;
		
		for (ItemClassificationDTO item: classifications.getMatching())
			matchingColumns.addAll(item.getColumns());
	}
	
	public ItemRemarkDTO getRemark() {
		return remark;
	}

	public void setRemark(ItemRemarkDTO remark) {
		this.remark = remark;
		
		if(remark != null)
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