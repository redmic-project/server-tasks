package es.redmic.tasks.ingest.data.document.jobs;

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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import es.redmic.tasks.ingest.common.jobs.ParametersBase;
import es.redmic.tasks.ingest.model.matching.data.document.dto.DocumentMatching;

public class DocumentParameters extends ParametersBase {

	@NotNull
	String delimiter;

	@NotNull
	@Valid
	private DocumentMatching matching;

	public DocumentParameters() {
	}

	public DocumentMatching getMatching() {
		return matching;
	}

	public void setMatching(DocumentMatching matching) {
		this.matching = matching;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
}
