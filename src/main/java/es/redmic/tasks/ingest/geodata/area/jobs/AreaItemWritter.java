package es.redmic.tasks.ingest.geodata.area.jobs;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import es.redmic.db.geodata.area.service.AreaService;
import es.redmic.exception.tasks.ingest.IngestPersistenceDataException;
import es.redmic.models.es.geojson.area.dto.AreaDTO;

public class AreaItemWritter implements ItemWriter<List<AreaDTO>> {

	AreaService service;

	private String taskId;

	public AreaItemWritter(AreaService service, String taskId) {

		this.service = service;
		this.taskId = taskId;
	}

	@Override
	public void write(List<? extends List<AreaDTO>> rows) {

		try {
			rows.forEach(row -> row.forEach(item -> service.save(item)));
		} catch (Exception e) {
			throw new IngestPersistenceDataException(taskId, e);
		}
	}
}
