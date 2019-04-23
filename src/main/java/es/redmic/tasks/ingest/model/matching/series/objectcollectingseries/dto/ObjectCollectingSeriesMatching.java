package es.redmic.tasks.ingest.model.matching.series.objectcollectingseries.dto;

/*-
 * #%L
 * Tasks
 * %%
 * Copyright (C) 2019 REDMIC Project / Server
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
