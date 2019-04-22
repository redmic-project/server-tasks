package es.redmic.tasks.ingest.model.matching.data.document.dto;

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

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaIgnore;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaNotNull;

import es.redmic.tasks.ingest.model.matching.common.dto.ItemStringDTO;

@JsonSchemaNotNull
public class ItemTitleDTO extends ItemStringDTO {

	private static final String FIELD = "title";

	public ItemTitleDTO() {
	}

	@JsonSchemaIgnore
	@JsonIgnore
	@Override
	public String getField() {
		return FIELD;
	}
}
