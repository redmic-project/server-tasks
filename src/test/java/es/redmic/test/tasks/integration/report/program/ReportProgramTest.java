package es.redmic.test.tasks.integration.report.program;

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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.redmic.brokerlib.avro.common.MessageWrapper;
import es.redmic.es.administrative.repository.ProgramESRepository;
import es.redmic.models.es.administrative.model.Program;
import es.redmic.test.tasks.integration.report.common.ReportBaseTest;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = { "schema.registry.port=18089" })
public class ReportProgramTest extends ReportBaseTest {

	private static final Long PROGRAM_ID = 10L;

	@Value("${broker.topic.task.report.program.run}")
	String REPORT_PROGRAM_RUN_TOPIC;

	@Autowired
	ProgramESRepository programESRepository;

	@Before
	public void config() {

		Program program = new Program();
		program.setPath("r." + PROGRAM_ID );
		program.setId(PROGRAM_ID);
		program.setName("Program de prueba");
		programESRepository.save(program);
	}

	@Override
	@Test
	public void runIngestData_ReturnStatusMsg_IfBrokerIsListen() throws Exception {

		super.runIngestData_ReturnStatusMsg_IfBrokerIsListen();
	}

	@KafkaListener(topics = "${broker.topic.task.report.program.status}")
	public void run(MessageWrapper payload) {

		blockingQueue.offer(payload);
	}

	@Override
	protected String getRunTopic() {
		return REPORT_PROGRAM_RUN_TOPIC;
	}

	@Override
	protected Map<String, String> createRunTaskRequest() {

		Map<String, String> request = new HashMap<>();
		request.put("selectId", PROGRAM_ID.toString());
		return request;
	}
}
