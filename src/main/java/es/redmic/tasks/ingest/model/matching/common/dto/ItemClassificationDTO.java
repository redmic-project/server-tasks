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
import javax.validation.constraints.Size;

import org.opengis.feature.simple.SimpleFeature;
import org.springframework.batch.item.file.transform.FieldSet;

import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaNotNull;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaUniqueItemsByRequiredProperties;

@JsonSchemaNotNull
public class ItemClassificationDTO extends ItemBaseWithColumnsDTO {

	@NotNull
	@Size(min = 1)
	@JsonSchemaUniqueItemsByRequiredProperties
	List<String> classifications;

	public List<String> getClassifications() {
		return classifications;
	}

	public void setClassifications(List<String> classifications) {
		this.classifications = classifications;
	}

	@Override
	public List<String> returnValue(FieldSet fs) {

		return getClassifications();
	}

	@Override
	public List<String> returnValue(SimpleFeature fs) {

		return getClassifications();
	}
}
