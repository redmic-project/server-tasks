package es.redmic.tasks.report.document.controller;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

import es.redmic.brokerlib.avro.common.MessageWrapper;
import es.redmic.tasks.report.common.controller.ReportController;
import es.redmic.tasks.report.document.service.DocumentTaskReportESService;

@Controller
public class DocumentTaskReportController extends ReportController {

	private final String SERVICE_NAME = "document";

	DocumentTaskReportESService service;

	@Autowired
	public DocumentTaskReportController(DocumentTaskReportESService service) {
		super(service);
		this.service = service;
	}

	@KafkaListener(topics = "${broker.topic.task.report.document.run}")
	public void run(MessageWrapper payload) {

		logger.info("Report " + SERVICE_NAME + ". Received payload='{}'", payload);
		service.report(SERVICE_NAME, payload);
	}

	@Override
	protected boolean chkEventIsMine(String taskName) {
		return taskName.equals(SERVICE_NAME);
	}
}
