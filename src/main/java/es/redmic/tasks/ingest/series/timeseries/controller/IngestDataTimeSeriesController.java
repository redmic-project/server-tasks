package es.redmic.tasks.ingest.series.timeseries.controller;

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
import es.redmic.tasks.ingest.model.matching.series.timeseries.dto.TimeSeriesMatching;
import es.redmic.tasks.ingest.model.series.dto.RunTaskIngestDataSeriesDTO;
import es.redmic.tasks.ingest.series.timeseries.service.IngestDataTimeSeriesService;

@Controller
public class IngestDataTimeSeriesController extends TaskJobBaseWithInterventionController<TimeSeriesMatching> {

	@Value("${property.INGEST_DATA_TIME_SERIES_TASK_NAME}")
	private String TASK_NAME;

	@Autowired
	public IngestDataTimeSeriesController(IngestDataTimeSeriesService service,
			@Autowired @Qualifier("createTimeSeriesIndexing") Job createTimeSeriesIndexing) {
		super(service);
		service.setIndexingJob(createTimeSeriesIndexing);
	}

	@KafkaListener(topics = "${broker.topic.task.ingest.timeseries.run}")
	public void run(MessageWrapper payload) {

		logger.info("Arrancar tarea de ingesta de timeseries. User: " + payload.getUserId());

		RunTaskIngestDataSeriesDTO dto = objectMapper.convertValue(
				MessageWrapperUtils.getMessageFromMessageWrapper(payload), RunTaskIngestDataSeriesDTO.class);
		super.register(dto, payload.getUserId(), TASK_NAME);
	}

	@KafkaListener(topics = "${broker.topic.task.ingest.timeseries.resume}")
	public void resume(MessageWrapper payload) {

		logger.info("Intervención del usuario para ingesta de timeseries. User: " + payload.getUserId() + " task: "
				+ payload.getActionId());

		String taskId = payload.getActionId();

		// TODO: hacer excepción específica
		if (taskId == null)
			throw new InternalException(ExceptionType.INTERNAL_EXCEPTION);

		super.resume(taskId, objectMapper.convertValue(MessageWrapperUtils.getMessageFromMessageWrapper(payload),
				TimeSeriesMatching.class));
	}

	@Override
	protected boolean chkEventIsMine(String taskName) {
		return taskName.equals(TASK_NAME);
	}
}
