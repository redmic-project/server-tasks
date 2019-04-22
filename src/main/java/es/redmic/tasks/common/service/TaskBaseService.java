package es.redmic.tasks.common.service;

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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import es.redmic.es.config.OrikaScanBeanESItfc;
import es.redmic.tasks.ingest.model.common.dto.UserTaskDTOEvent;
import es.redmic.tasks.ingest.model.status.dto.UserTaskDTO;

public abstract class TaskBaseService implements ApplicationEventPublisherAware {

	protected static Logger logger = LogManager.getLogger();

	@Autowired
	protected OrikaScanBeanESItfc orikaMapper;

	protected ApplicationEventPublisher eventPublisher;

	public TaskBaseService() {

	}

	protected void publishTaskStatusToUser(UserTaskDTO dto) {
		this.eventPublisher.publishEvent(new UserTaskDTOEvent(this, dto));
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
}
