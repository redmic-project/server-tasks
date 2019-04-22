package es.redmic.tasks.worms.jobs;

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
