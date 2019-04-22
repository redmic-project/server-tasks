package es.redmic.tasks.ingest.model.matching.common.dto;

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

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.batch.item.file.transform.FieldSet;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaIgnore;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaNotNull;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaUrl;

import es.redmic.models.es.common.deserializer.CustomRelationDeserializer;
import es.redmic.models.es.common.dto.CompactDTOImpl;
import es.redmic.models.es.maintenance.device.dto.DeviceCompactDTO;

@JsonSchemaNotNull
public class ItemDeviceDTO extends ItemBaseDTO implements ItemDTOItf {

	private static final String FIELD = "device";

	public ItemDeviceDTO() {
	}

	@NotNull
	@JsonSerialize(as = CompactDTOImpl.class)
	@JsonDeserialize(using = CustomRelationDeserializer.class)
	@JsonSchemaUrl(value = "deviceUrl")
	private DeviceCompactDTO device;

	public DeviceCompactDTO getDevice() {
		return device;
	}

	public void setDevice(DeviceCompactDTO device) {
		this.device = device;
	}

	@JsonSchemaIgnore
	@JsonIgnore
	@Override
	public String getField() {
		return FIELD;
	}

	@Override
	public DeviceCompactDTO returnValue(FieldSet fs) {

		return device;
	}

	@Override
	public DeviceCompactDTO returnValue(SimpleFeature fs) {

		return device;
	}
}
