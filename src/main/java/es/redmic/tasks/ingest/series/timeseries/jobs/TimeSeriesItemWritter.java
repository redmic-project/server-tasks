package es.redmic.tasks.ingest.series.timeseries.jobs;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import es.redmic.db.series.timeseries.service.TimeSeriesService;
import es.redmic.exception.tasks.ingest.IngestPersistenceDataException;
import es.redmic.models.es.series.timeseries.dto.TimeSeriesDTO;

public class TimeSeriesItemWritter implements ItemWriter<List<TimeSeriesDTO>> {

	TimeSeriesService service;

	private String taskId;

	public TimeSeriesItemWritter(TimeSeriesService service, String taskId) {
		this.service = service;
		this.taskId = taskId;
	}

	@Override
	public void write(List<? extends List<TimeSeriesDTO>> rows) {
		try {
			rows.forEach(row -> row.forEach(item -> service.save(item)));
		} catch (Exception e) {
			throw new IngestPersistenceDataException(taskId, e);
		}
	}
}
