package es.redmic.tasks.ingest.model.intervention.matching;

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
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import es.redmic.tasks.ingest.model.intervention.common.InterventionDescriptionBase;
import es.redmic.tasks.ingest.model.intervention.common.InterventionType;

public class InterventionMatching extends InterventionDescriptionBase {

	Object matching;

	List<Map<String, String>> data;
	
	@NotNull
	@Size(min=1)
	List<String> header;

	public InterventionMatching() {
		setType(InterventionType.MATCHING);
	}

	public Object getMatching() {
		return matching;
	}

	public void setMatching(Object matching) {
		
		this.matching = matching;
	}

	public List<Map<String, String>> getData() {
		return data;
	}

	public void setData(List<Map<String, String>> data) {
		this.data = data;
	}

	public List<String> getHeader() {
		return header;
	}

	public void setHeader(List<String> header) {
		this.header = header;
	}
}
