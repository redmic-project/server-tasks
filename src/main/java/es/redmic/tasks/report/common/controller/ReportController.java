package es.redmic.tasks.report.common.controller;

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

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;

import es.redmic.exception.tasks.report.GenerateReportException;
import es.redmic.tasks.common.controller.TaskBaseController;
import es.redmic.tasks.ingest.model.status.dto.UserTaskDTO;
import es.redmic.tasks.report.common.service.ReportService;

public abstract class ReportController extends TaskBaseController {

	ReportService<?> reportService;

	public ReportController(ReportService<?> service) {
		this.reportService = service;
	}

	@MessageExceptionHandler(value = { GenerateReportException.class })
	public void handleIngestException(GenerateReportException e) {
		reportService.setFailed(e);
	}

	@Override
	protected String getPublishingChannelSocket(UserTaskDTO dto) {
		return PRE_PUBLISHING_CHANNEL + dto.getTaskType() + "." + dto.getTaskName() + SUF_PUBLISHING_CHANNEL_STATUS;
	}
}
