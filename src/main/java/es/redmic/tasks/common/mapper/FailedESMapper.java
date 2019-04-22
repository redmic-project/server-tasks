package es.redmic.tasks.common.mapper;

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

import org.springframework.stereotype.Component;

import es.redmic.tasks.ingest.model.status.common.TaskStatus;
import es.redmic.tasks.ingest.model.status.dto.FailedTaskDTO;
import es.redmic.tasks.ingest.model.status.model.UserTasks;
import es.redmic.tasks.ingest.model.step.model.FailedStep;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;

@Component
public class FailedESMapper extends CustomMapper<UserTasks, FailedTaskDTO> {

	public void mapAtoB(UserTasks a, FailedTaskDTO b, MappingContext context) {
		
		FailedStep step = (FailedStep) a.lastStep();
		if (step != null) {
			b.setError(step.getError());
			b.setParameters(step.getParameters());
		}
	}
	
	@Override
	public void mapBtoA(FailedTaskDTO b, UserTasks a, MappingContext context) {
		
		FailedStep step = new FailedStep();
		step.setDate(b.getEndDate());
		step.setError(b.getError());
		step.setStatus(TaskStatus.FAILED);
		step.setParameters(b.getParameters());
		a.addStep(step);
	}
}
