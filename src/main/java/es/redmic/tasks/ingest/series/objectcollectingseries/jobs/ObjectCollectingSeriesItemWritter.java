package es.redmic.tasks.ingest.series.objectcollectingseries.jobs;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import es.redmic.db.series.objectcollecting.service.ObjectCollectingSeriesService;
import es.redmic.exception.tasks.ingest.IngestPersistenceDataException;
import es.redmic.models.es.series.objectcollecting.dto.ObjectCollectingSeriesDTO;

public class ObjectCollectingSeriesItemWritter implements ItemWriter<List<ObjectCollectingSeriesDTO>> {

	ObjectCollectingSeriesService service;

	private String taskId;

	public ObjectCollectingSeriesItemWritter(ObjectCollectingSeriesService service, String taskId) {
		this.service = service;
		this.taskId = taskId;
	}

	@Override
	public void write(List<? extends List<ObjectCollectingSeriesDTO>> rows) throws Exception {

		try {
			rows.forEach(row -> row.forEach(item -> service.save(item)));
		} catch (Exception e) {
			throw new IngestPersistenceDataException(taskId, e);
		}
	}
}