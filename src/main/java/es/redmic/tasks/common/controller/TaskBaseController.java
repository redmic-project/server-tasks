package es.redmic.tasks.common.controller;

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
import org.springframework.context.ApplicationListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.redmic.brokerlib.avro.common.MessageWrapper;
import es.redmic.brokerlib.producer.Sender;
import es.redmic.brokerlib.utils.MessageWrapperUtils;
import es.redmic.exception.common.ExceptionType;
import es.redmic.exception.common.InternalException;
import es.redmic.tasks.common.service.UserTasksService;
import es.redmic.tasks.ingest.model.common.dto.UserTaskDTOEvent;
import es.redmic.tasks.ingest.model.status.dto.UserTaskDTO;

public abstract class TaskBaseController implements ApplicationListener<UserTaskDTOEvent> {

	protected static Logger logger = LogManager.getLogger();

	protected final String PRE_PUBLISHING_CHANNEL = "tasks.";
	protected final String SUF_PUBLISHING_CHANNEL_STATUS = ".status";

	@Autowired
	private Sender sender;

	@Autowired
	UserTasksService userTasksService;

	@Autowired
	protected ObjectMapper objectMapper;

	public TaskBaseController() {
	}

	@Override
	public void onApplicationEvent(UserTaskDTOEvent eventDTO) {

		if (chkEventIsMine(eventDTO.getDto().getTaskName()))
			publishStatusTaskToUser(eventDTO.getDto());
	}

	protected abstract boolean chkEventIsMine(String taskName);

	protected void publishStatusTaskToUser(UserTaskDTO dto) {

		MessageWrapper msg = new MessageWrapper();
		msg.setUserId(dto.getUserId().toString());
		try {
			msg.setContent(MessageWrapperUtils.getContent(dto));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new InternalException(ExceptionType.INTERNAL_EXCEPTION);
		}
		msg.setActionId(dto.getId());

		publishToBroker(getPublishingChannelSocket(dto), msg);
	}

	protected void publishToBroker(String topic, MessageWrapper message) {

		logger.info("Publicando al broker por el topic: " + topic + ". Mensaje: " + message);
		sender.send(topic, message);
	}

	protected String getPublishingChannelSocket(UserTaskDTO dto) {
		return PRE_PUBLISHING_CHANNEL + dto.getTaskType() + SUF_PUBLISHING_CHANNEL_STATUS;
	}
}
