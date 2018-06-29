package es.redmic.test.tasks.integration.report.species;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.redmic.brokerlib.avro.common.MessageWrapper;
import es.redmic.test.tasks.integration.report.common.ReportBaseTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class ReportSpeciesTest extends ReportBaseTest {

	@Value("${broker.topic.task.report.species.run}")
	String REPORT_SPECIES_RUN_TOPIC;

	@Override
	@Test
	public void runIngestData_ReturnStatusMsg_IfBrokerIsListen() throws Exception {

		super.runIngestData_ReturnStatusMsg_IfBrokerIsListen();
	}

	@KafkaListener(topics = "${broker.topic.task.report.species.status}")
	public void run(MessageWrapper payload) {

		blockingQueue.offer(payload);
	}

	@Override
	protected String getRunTopic() {
		return REPORT_SPECIES_RUN_TOPIC;
	}

	@Override
	protected Map<String, String> createRunTaskRequest() {

		Map<String, String> request = new HashMap<>();
		request.put("selectId", "20144");
		return request;
	}
}