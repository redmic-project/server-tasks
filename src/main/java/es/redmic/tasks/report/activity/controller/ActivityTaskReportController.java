package es.redmic.tasks.report.activity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

import es.redmic.brokerlib.avro.common.MessageWrapper;
import es.redmic.tasks.report.activity.service.ActivityTaskReportESService;
import es.redmic.tasks.report.common.controller.ReportController;

@Controller
public class ActivityTaskReportController extends ReportController {

	private final String SERVICE_NAME = "activity";

	ActivityTaskReportESService service;

	@Autowired
	public ActivityTaskReportController(ActivityTaskReportESService service) {
		super(service);
		this.service = service;
	}

	@KafkaListener(topics = "${broker.topic.task.report.activity.run}")
	public void report(MessageWrapper payload) {

		logger.info("Report " + SERVICE_NAME + ". Received payload='{}'", payload);
		service.report(SERVICE_NAME, payload);
	}

	@Override
	protected boolean chkEventIsMine(String taskName) {
		return taskName.equals(SERVICE_NAME);
	}
}
