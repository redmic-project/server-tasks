package es.redmic.tasks.worms.controller;

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
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

import es.redmic.brokerlib.avro.common.MessageWrapper;
import es.redmic.brokerlib.utils.MessageWrapperUtils;
import es.redmic.tasks.common.controller.TaskJobBaseController;
import es.redmic.tasks.ingest.model.status.dto.RegisteredTaskDTO;
import es.redmic.tasks.ingest.model.status.dto.StartedTaskDTO;
import es.redmic.tasks.worms.dto.RunTaskWormsToRedmicDTO;
import es.redmic.tasks.worms.service.WormsToRedmicTaskService;

@Controller
public class WormsToRedmicTaskController extends TaskJobBaseController {

	private final Logger LOGGER = LoggerFactory.getLogger(WormsToRedmicTaskController.class);

	@Value("${property.WORMS_TO_REDMIC_TASK_NAME}")
	private String TASK_NAME;

	WormsToRedmicTaskService service;

	Job wormsToRedmicJob;

	@Autowired
	public WormsToRedmicTaskController(WormsToRedmicTaskService service,
			@Autowired @Qualifier("wormsToRedmicJob") Job wormsToRedmicJob) {
		super(service);
		this.service = service;
		service.setIndexingJob(wormsToRedmicJob);
	}

	@KafkaListener(topics = "${broker.topic.task.worms.run}")
	public void run(MessageWrapper payload) {

		logger.info("Arrancar tarea de ingesta de worms. User: " + payload.getUserId());

		RunTaskWormsToRedmicDTO runTask = objectMapper
				.convertValue(MessageWrapperUtils.getMessageFromMessageWrapper(payload), RunTaskWormsToRedmicDTO.class);

		LOGGER.info("Peticion: " + " - " + TASK_NAME);

		runTask.setTaskName(TASK_NAME);

		RegisteredTaskDTO dto = service.register(Long.parseLong(payload.getUserId()), runTask);

		publishStatusTaskToUser(dto);

		StartedTaskDTO resultDTO = service.run(dto.getId());
		publishStatusTaskToUser(resultDTO);

		service.resume(dto.getId());

		LOGGER.info(TASK_NAME + " - Iniciando ejecución");
	}

	@Override
	protected boolean chkEventIsMine(String taskName) {
		return taskName.equals(TASK_NAME);
	}
}
