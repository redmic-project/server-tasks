package es.redmic.tasks.ingest.data.document.jobs;

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

import es.redmic.db.administrative.service.DocumentService;
import es.redmic.es.config.OrikaScanBeanESItfc;
import es.redmic.exception.tasks.ingest.IngestPersistenceDataException;
import es.redmic.models.es.administrative.dto.DocumentDTO;

public class DocumentItemWritter implements ItemWriter<List<DocumentDTO>> {

	DocumentService service;

	private String taskId;

	OrikaScanBeanESItfc orikaMapper;

	public DocumentItemWritter(OrikaScanBeanESItfc orikaMapper, DocumentService service, String taskId) {

		this.service = service;
		this.taskId = taskId;
		this.orikaMapper = orikaMapper;
	}

	@Override
	public void write(List<? extends List<DocumentDTO>> rows) {
		try {
			rows.forEach(row -> row.forEach(item -> saveOrUpdate(item)));
		} catch (Exception e) {
			throw new IngestPersistenceDataException(taskId, e);
		}
	}

	private void saveOrUpdate(DocumentDTO item) {

		DocumentDTO document = service.findByCode(item.getCode());

		if (document != null) {
			Long id = document.getId();
			orikaMapper.getMapperFacade().map(item, document);
			document.setId(id);
			service.update(document);
		} else {
			service.save(item);
		}
	}
}
