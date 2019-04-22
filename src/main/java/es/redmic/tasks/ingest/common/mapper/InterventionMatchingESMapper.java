package es.redmic.tasks.ingest.common.mapper;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import es.redmic.tasks.ingest.model.intervention.common.InterventionType;
import es.redmic.tasks.ingest.model.intervention.matching.InterventionMatching;
import es.redmic.tasks.ingest.model.intervention.matching.RequestUserInterventionMatchingTaskDTO;
import es.redmic.tasks.ingest.model.status.common.TaskStatus;
import es.redmic.tasks.ingest.model.status.model.UserTasks;
import es.redmic.tasks.ingest.model.step.intervention.WaitingInterventionStep;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;

@Component
public class InterventionMatchingESMapper extends CustomMapper<UserTasks, RequestUserInterventionMatchingTaskDTO> {

	private final Logger LOGGER = LoggerFactory.getLogger(InterventionMatchingESMapper.class);

	public void mapAtoB(UserTasks a, RequestUserInterventionMatchingTaskDTO b, MappingContext context) {
		
		WaitingInterventionStep step = (WaitingInterventionStep) a.lastStep();
		if ((step.getStatus() != TaskStatus.WAITING_INTERVENTION)
				&& (step.getInterventionDescription().getType() != InterventionType.MATCHING)) {
			
			LOGGER.error("El último step no es necesita intervención");
			// TODO Lanzar exception
		}
		
		b.setInterventionDescription((InterventionMatching) step.getInterventionDescription());
	}
}
