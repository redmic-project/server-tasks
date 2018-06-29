package es.redmic.tasks.ingest.series.objectcollectingseries.controller;

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
import es.redmic.tasks.ingest.model.matching.series.objectcollectingseries.dto.ObjectCollectingSeriesMatching;
import es.redmic.tasks.ingest.model.series.dto.RunTaskIngestDataSeriesDTO;
import es.redmic.tasks.ingest.series.objectcollectingseries.service.IngestDataObjectCollectingSeriesService;

@Controller
public class IngestDataObjectCollectingSeriesController
		extends TaskJobBaseWithInterventionController<ObjectCollectingSeriesMatching> {

	@Value("${property.INGEST_DATA_OBJECT_COLLECTING_SERIES_TASK_NAME}")
	private String TASK_NAME;

	@Autowired
	public IngestDataObjectCollectingSeriesController(IngestDataObjectCollectingSeriesService service,
			@Autowired @Qualifier("createObjectCollectingSeriesIndexing") Job createObjectCollectingSeriesIndexing) {
		super(service);
		service.setIndexingJob(createObjectCollectingSeriesIndexing);
	}

	@KafkaListener(topics = "${broker.topic.task.ingest.objectcollectingseries.run}")
	public void run(MessageWrapper payload) {

		logger.info("Arrancar tarea de ingesta de objectcollectingseries. User: " + payload.getUserId());

		RunTaskIngestDataSeriesDTO dto = objectMapper.convertValue(
				MessageWrapperUtils.getMessageFromMessageWrapper(payload), RunTaskIngestDataSeriesDTO.class);
		super.register(dto, payload.getUserId(), TASK_NAME);
	}

	@KafkaListener(topics = "${broker.topic.task.ingest.objectcollectingseries.resume}")
	public void resume(MessageWrapper payload) {

		logger.info("Intervención del usuario para ingesta de objectcollectingseries. User: " + payload.getUserId()
				+ " task: " + payload.getActionId());

		String taskId = payload.getActionId();

		// TODO: hacer excepción específica
		if (taskId == null)
			throw new InternalException(ExceptionType.INTERNAL_EXCEPTION);

		super.resume(taskId, objectMapper.convertValue(MessageWrapperUtils.getMessageFromMessageWrapper(payload),
				ObjectCollectingSeriesMatching.class));
	}

	@Override
	protected boolean chkEventIsMine(String taskName) {
		return taskName.equals(TASK_NAME);
	}
}