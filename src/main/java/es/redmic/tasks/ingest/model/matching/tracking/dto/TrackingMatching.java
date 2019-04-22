package es.redmic.tasks.ingest.model.matching.tracking.dto;

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

import es.redmic.tasks.ingest.model.matching.common.dto.ItemCommonDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemDeviceDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemPointGeometryDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.MatchingBaseDTO;

@JsonSchemaNotNull
public class TrackingMatching extends MatchingBaseDTO {
	
	@NotNull
	@Valid
	private ItemPointGeometryDTO pointGeometry;
	
	@NotNull
	@Valid
	private ItemDeviceDTO device;
	
	public TrackingMatching() {
		super();
	}

	public ItemPointGeometryDTO getPointGeometry() {
		return pointGeometry;
	}

	public void setPointGeometry(ItemPointGeometryDTO pointGeometry) {
		this.pointGeometry = pointGeometry;
		matchingColumns.addAll(pointGeometry.getColumns());
	}

	public ItemDeviceDTO getDevice() {
		return device;
	}

	public void setDevice(ItemDeviceDTO device) {
		this.device = device;
	}
	
	@SuppressWarnings("serial")
	@JsonIgnore
	public List<ItemCommonDTO> getItemsCommon() {
		
		return new ArrayList<ItemCommonDTO>() {{
			add(getDate());
			if (getQFlag() != null) add(getQFlag());
			if (getVFlag() != null) add(getVFlag());
		}};
	}
}
