package es.redmic.tasks.ingest.model.step.model;

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

import java.util.Map;

import es.redmic.tasks.ingest.model.status.common.TaskError;
import es.redmic.tasks.ingest.model.status.common.TaskStatus;

public class FailedStep extends BaseStep {
	
	private TaskError error;
	
	private Map<String, Object> parameters;
	
	public FailedStep() {
		super();
		setStatus(TaskStatus.FAILED);
	}

	public TaskError getError() {
		return error;
	}

	public void setError(TaskError error) {
		this.error = error;
	}
	
	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
}
