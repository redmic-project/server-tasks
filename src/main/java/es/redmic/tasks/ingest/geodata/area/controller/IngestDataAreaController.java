package es.redmic.tasks.ingest.geodata.area.controller;

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

import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

import es.redmic.brokerlib.avro.common.MessageWrapper;
import es.redmic.brokerlib.utils.MessageWrapperUtils;
import es.redmic.exception.common.ExceptionType;
import es.redmic.exception.common.InternalException;
import es.redmic.tasks.common.controller.TaskJobBaseWithInterventionController;
import es.redmic.tasks.ingest.geodata.area.service.IngestDataAreaService;
import es.redmic.tasks.ingest.model.geodata.area.dto.RunTaskIngestDataAreaDTO;
import es.redmic.tasks.ingest.model.matching.area.dto.AreaMatching;

@Controller
public class IngestDataAreaController extends TaskJobBaseWithInterventionController<AreaMatching> {

	@Value("${property.INGEST_DATA_AREA_TASK_NAME}")
	private String TASK_NAME;

	@Autowired
	public IngestDataAreaController(IngestDataAreaService service,
			@Autowired @Qualifier("createAreaIndexing") Job createAreaIndexing) {
		super(service);
		service.setIndexingJob(createAreaIndexing);
	}

	@KafkaListener(topics = "${broker.topic.task.ingest.area.run}")
	public void run(MessageWrapper payload) {

		logger.info("Arrancar tarea de ingesta de areas. User: " + payload.getUserId());

		RunTaskIngestDataAreaDTO dto = objectMapper.convertValue(
				MessageWrapperUtils.getMessageFromMessageWrapper(payload), RunTaskIngestDataAreaDTO.class);
		super.register(dto, payload.getUserId(), TASK_NAME);
	}

	@KafkaListener(topics = "${broker.topic.task.ingest.area.resume}")
	public void resume(MessageWrapper payload) {

		logger.info("Intervención del usuario para ingesta de areas. User: " + payload.getUserId() + " task: "
				+ payload.getActionId());

		String taskId = payload.getActionId();

		// TODO: hacer excepción específica
		if (taskId == null)
			throw new InternalException(ExceptionType.INTERNAL_EXCEPTION);

		super.resume(taskId, objectMapper.convertValue(MessageWrapperUtils.getMessageFromMessageWrapper(payload),
				AreaMatching.class));
	}

	@Override
	protected boolean chkEventIsMine(String taskName) {
		return taskName.equals(TASK_NAME);
	}
}
