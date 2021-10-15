package es.redmic.test.tasks.integration.report.document;

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

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.redmic.brokerlib.avro.common.MessageWrapper;
import es.redmic.es.administrative.repository.ActivityESRepository;
import es.redmic.es.administrative.repository.DocumentESRepository;
import es.redmic.models.es.administrative.model.Activity;
import es.redmic.models.es.administrative.model.Document;
import es.redmic.models.es.data.common.model.DataSearchWrapper;
import es.redmic.test.tasks.integration.report.common.ReportBaseTest;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = { "schema.registry.port=18088" })
public class ReportDocumentTest extends ReportBaseTest {

	private static final Long DOCUMENT_ID = 1536L;

	private static final Long ACTIVITY_ID = 44L;

	@Value("${broker.topic.task.report.document.run}")
	String REPORT_DOCUMENT_RUN_TOPIC;

	@MockBean
	ActivityESRepository activityRepository;

	@Autowired
	DocumentESRepository repository;

	@Before
	public void config() {

		Document document = new Document();
		document.setId(DOCUMENT_ID);
		document.setTitle("Documento de prueba");
		document.setLanguage("es");
		repository.save(document);

		Activity activity = new Activity();
		activity.setId(ACTIVITY_ID);
		activity.setName("Prueba");

		DataSearchWrapper<Activity> result = new DataSearchWrapper<>();
		result.setSource(0, activity);

		when(activityRepository.findByDocuments(any(), any())).thenReturn(result);
	}

	@Override
	@Test
	public void runIngestData_ReturnStatusMsg_IfBrokerIsListen() throws Exception {

		super.runIngestData_ReturnStatusMsg_IfBrokerIsListen();
	}

	@KafkaListener(topics = "${broker.topic.task.report.document.status}")
	public void run(MessageWrapper payload) {

		blockingQueue.offer(payload);
	}

	@Override
	protected String getRunTopic() {
		return REPORT_DOCUMENT_RUN_TOPIC;
	}

	@Override
	protected Map<String, String> createRunTaskRequest() {

		Map<String, String> request = new HashMap<>();
		request.put("selectId", DOCUMENT_ID.toString());
		return request;
	}
}
