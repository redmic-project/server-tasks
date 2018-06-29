package es.redmic.tasks.worms.jobs;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import es.redmic.db.administrative.taxonomy.service.TaxonService;
import es.redmic.exception.tasks.ingest.IngestPersistenceDataException;
import es.redmic.models.es.administrative.taxonomy.dto.TaxonDTO;

public class WormsToRedmicItemWritter implements ItemWriter<TaxonDTO> {

	TaxonService service;

	private String taskId;

	public WormsToRedmicItemWritter(TaxonService service, String taskId) {

		this.service = service;
		this.taskId = taskId;
	}

	@Override
	public void write(List<? extends TaxonDTO> rows) {
		try {
			rows.forEach(item -> update(item));
		} catch (Exception e) {
			throw new IngestPersistenceDataException(taskId, e);
		}
	}

	private void update(TaxonDTO item) {

		if (item != null)
			service.update(item);
	}
}
