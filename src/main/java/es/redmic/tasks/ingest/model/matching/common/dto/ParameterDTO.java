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

import java.util.List;

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.batch.item.file.transform.FieldSet;

import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaIgnore;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaNotNull;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaUniqueItemsByRequiredProperties;

@JsonSchemaNotNull
public class ParameterDTO extends ItemBaseDTO implements ItemDTOItf {

	private static final String FIELD = "value";

	public ParameterDTO() {
	}

	@NotNull
	Long dataDefinitionId;

	@JsonSchemaUniqueItemsByRequiredProperties
	protected List<String> columns;

	public Long getDataDefinitionId() {
		return dataDefinitionId;
	}

	public void setDataDefinitionId(Long dataDefinitionId) {
		this.dataDefinitionId = dataDefinitionId;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	@JsonSchemaIgnore
	@JsonIgnore
	@Override
	public String getField() {
		return FIELD;
	}

	@Override
	public Double returnValue(FieldSet fs) {

		if (columns == null || columns.size() < 1)
			return null;
		return fs.readDouble(columns.get(0));
	}

	@Override
	public Double returnValue(SimpleFeature fs) {

		if (columns == null || columns.size() < 1)
			return null;

		return (Double) fs.getAttribute(columns.get(0));
	}
}
