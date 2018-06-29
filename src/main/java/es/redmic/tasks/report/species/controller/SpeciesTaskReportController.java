package es.redmic.tasks.report.species.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

import es.redmic.brokerlib.avro.common.MessageWrapper;
import es.redmic.tasks.report.common.controller.ReportController;
import es.redmic.tasks.report.species.service.SpeciesTaskReportESService;

@Controller
public class SpeciesTaskReportController extends ReportController {

	private final String SERVICE_NAME = "species";

	SpeciesTaskReportESService service;

	@Autowired
	public SpeciesTaskReportController(SpeciesTaskReportESService service) {
		super(service);
		this.service = service;
	}

	@KafkaListener(topics = "${broker.topic.task.report.species.run}")
	public void run(MessageWrapper payload) {

		logger.info("Report " + SERVICE_NAME + ". Received payload='{}'", payload);
		service.report(SERVICE_NAME, payload);
	}

	@Override
	protected boolean chkEventIsMine(String taskName) {
		return taskName.equals(SERVICE_NAME);
	}
}
