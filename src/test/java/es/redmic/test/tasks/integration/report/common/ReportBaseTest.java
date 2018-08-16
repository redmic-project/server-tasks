package es.redmic.test.tasks.integration.report.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;

import es.redmic.brokerlib.avro.common.MessageWrapper;
import es.redmic.brokerlib.producer.Sender;
import es.redmic.brokerlib.utils.MessageWrapperUtils;
import es.redmic.tasks.ingest.model.status.common.TaskStatus;
import es.redmic.tasks.ingest.model.status.dto.CompletedTaskDTO;
import es.redmic.tasks.ingest.model.status.dto.RegisteredTaskDTO;
import es.redmic.tasks.ingest.model.status.dto.StartedTaskDTO;
import es.redmic.test.tasks.integration.common.IntegrationTestBase;

public abstract class ReportBaseTest extends IntegrationTestBase {

	protected Long userId = 13L;

	// @formatter:off
	
	protected String taskId,
		USER_ID = userId.toString();
	
	// @formatter:on

	@Autowired
	private Sender sender;

	protected static BlockingQueue<Object> blockingQueue;

	@BeforeClass
	public static void setup() {

		blockingQueue = new LinkedBlockingDeque<>();
	}

	public void runIngestData_ReturnStatusMsg_IfBrokerIsListen() throws Exception {

		MessageWrapper payload = new MessageWrapper();
		payload.setUserId(USER_ID);

		payload.setContent(MessageWrapperUtils.getContent(createRunTaskRequest()));

		// Arrancar tarea
		sender.send(getRunTopic(), payload);

		// Recibe tarea registrada
		MessageWrapper msg = (MessageWrapper) blockingQueue.poll(60, TimeUnit.SECONDS);
		assertNotNull(msg);
		taskId = msg.getActionId();
		assertEquals(msg.getUserId(), USER_ID);

		payload.setActionId(taskId);

		RegisteredTaskDTO registeredTaskDTO = jacksonMapper
				.convertValue(MessageWrapperUtils.getMessageFromMessageWrapper(msg), RegisteredTaskDTO.class);
		assertNotNull(registeredTaskDTO);
		assertEquals(registeredTaskDTO.getId(), taskId);
		assertEquals(registeredTaskDTO.getStatus().toString(), TaskStatus.Constants.REGISTERED);

		// Recibe tarea arrancada
		msg = (MessageWrapper) blockingQueue.poll(50, TimeUnit.SECONDS);
		assertNotNull(msg);
		assertEquals(msg.getUserId(), USER_ID);

		StartedTaskDTO startedTaskDTO = jacksonMapper
				.convertValue(MessageWrapperUtils.getMessageFromMessageWrapper(msg), StartedTaskDTO.class);
		assertNotNull(startedTaskDTO);
		assertEquals(startedTaskDTO.getId(), taskId);
		assertEquals(startedTaskDTO.getStatus().toString(), TaskStatus.Constants.STARTED);

		// Recibe tarea completada

		msg = (MessageWrapper) blockingQueue.poll(2, TimeUnit.MINUTES);
		assertNotNull(msg);
		assertEquals(msg.getUserId(), USER_ID);

		CompletedTaskDTO completedTaskDTO = jacksonMapper
				.convertValue(MessageWrapperUtils.getMessageFromMessageWrapper(msg), CompletedTaskDTO.class);
		assertNotNull(completedTaskDTO);
		assertEquals(completedTaskDTO.getId(), taskId);
		assertEquals(completedTaskDTO.getStatus().toString(), TaskStatus.Constants.COMPLETED);
	}

	protected abstract Map<String, String> createRunTaskRequest();

	protected abstract String getRunTopic();
}
