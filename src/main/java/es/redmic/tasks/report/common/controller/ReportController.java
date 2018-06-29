package es.redmic.tasks.report.common.controller;

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
