package es.redmic.test.tasks.integration.ingest.data.document;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.redmic.db.administrative.service.DocumentService;
import es.redmic.es.maintenance.domain.administrative.service.DocumentTypeESService;
import es.redmic.models.es.maintenance.administrative.dto.DocumentTypeDTO;
import es.redmic.tasks.ingest.data.document.repository.IngestDataDocumentRepository;
import es.redmic.tasks.ingest.model.data.document.dto.RunTaskIngestDataDocumentDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemCodeDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemKeywordDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemLanguageDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemRemarkDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemURLDTO;
import es.redmic.tasks.ingest.model.matching.data.document.dto.DocumentMatching;
import es.redmic.tasks.ingest.model.matching.data.document.dto.ItemAuthorDTO;
import es.redmic.tasks.ingest.model.matching.data.document.dto.ItemDocumentTypeDTO;
import es.redmic.tasks.ingest.model.matching.data.document.dto.ItemSourceDTO;
import es.redmic.tasks.ingest.model.matching.data.document.dto.ItemTitleDTO;
import es.redmic.tasks.ingest.model.matching.data.document.dto.ItemYearDTO;
import es.redmic.test.tasks.integration.ingest.common.IngestBaseTest;
import es.redmic.test.tasks.utils.mapper.JsonToBeanTestUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = { "schema.registry.port=18082" })
public class IngestDataDocumentTest extends IngestBaseTest {

	@MockBean
	DocumentService documentService;

	@MockBean
	DocumentTypeESService documentTypeESService;

	@Autowired
	IngestDataDocumentRepository repository;

	// @formatter:off
	
	final String CSV_RESOURCES = "/data/csv/document/",
			FILENAME_CSV = "job-01.csv",
			TASK_NAME = "ingest-data-document",
			DELIMITER = "|";
	// @formatter:on		

	String FILENAME_RESULT;

	@Value("${property.path.media_storage.temp.INGEST_DATA}")
	String MEDIASTORAGE_TEMP_DATA;

	@Value("${broker.topic.task.ingest.document.run}")
	String INGEST_DOCUMENT_RUN_TOPIC;

	@Value("${broker.topic.task.ingest.document.resume}")
	String INGEST_DOCUMENT_RESUME_TOPIC;

	@Before
	public void before() throws IOException {

		DocumentTypeDTO documentTypeDTO = (DocumentTypeDTO) JsonToBeanTestUtil
				.getBean("/data/ingest/dto/item/document/documenttype.json", DocumentTypeDTO.class);

		when(documentTypeESService.findByName(anyString())).thenReturn(documentTypeDTO);

		when(documentService.findByCode(anyString())).thenReturn(null);

		when(documentService.save(any())).thenReturn(null);
		when(documentService.update(any())).thenReturn(null);

		FILENAME_RESULT = copyResourceToMediaStorage(CSV_RESOURCES, FILENAME_CSV, MEDIASTORAGE_TEMP_DATA);
	}

	@Override
	@Test
	public void runIngestData_ReturnStatusMsg_IfBrokerIsListen() throws Exception {

		super.runIngestData_ReturnStatusMsg_IfBrokerIsListen();
	}

	@Override
	protected String getRunTopic() {
		return INGEST_DOCUMENT_RUN_TOPIC;
	}

	@Override
	protected RunTaskIngestDataDocumentDTO createRunTaskRequest() {

		RunTaskIngestDataDocumentDTO runTask = new RunTaskIngestDataDocumentDTO();
		runTask.setTaskName(TASK_NAME);
		runTask.setUserId(USER_ID);
		runTask.getParameters().setFileName(FILENAME_RESULT);
		runTask.getParameters().setDelimiter(DELIMITER);

		return runTask;
	}

	@Override
	protected String getResumeTopic() {
		return INGEST_DOCUMENT_RESUME_TOPIC;
	}

	@Override
	protected DocumentMatching createMatchingTaskRequest() {

		DocumentMatching matching = new DocumentMatching();

		ItemTitleDTO itemTitle = new ItemTitleDTO();
		itemTitle.setColumns(Arrays.asList("title"));
		matching.setTitle(itemTitle);

		ItemSourceDTO itemSource = new ItemSourceDTO();
		itemSource.setColumns(Arrays.asList("source"));
		matching.setSource(itemSource);

		ItemAuthorDTO itemAuthor = new ItemAuthorDTO();
		itemAuthor.setColumns(Arrays.asList("author"));
		matching.setAuthor(itemAuthor);

		ItemCodeDTO itemCode = new ItemCodeDTO();
		itemCode.setColumns(Arrays.asList("code"));
		matching.setCode(itemCode);

		ItemRemarkDTO itemRemark = new ItemRemarkDTO();
		itemRemark.setColumns(Arrays.asList("remark"));
		matching.setRemark(itemRemark);

		ItemYearDTO itemYear = new ItemYearDTO();
		itemYear.setColumns(Arrays.asList("year"));
		matching.setYear(itemYear);

		ItemLanguageDTO itemLanguage = new ItemLanguageDTO();
		itemLanguage.setColumns(Arrays.asList("language"));
		matching.setLanguage(itemLanguage);

		ItemKeywordDTO itemKeywords = new ItemKeywordDTO();
		itemKeywords.setColumns(Arrays.asList("keywords"));
		matching.setKeywords(itemKeywords);

		ItemDocumentTypeDTO itemDocumentType = new ItemDocumentTypeDTO();
		itemDocumentType.setColumns(Arrays.asList("documentType"));
		matching.setDocumentType(itemDocumentType);

		ItemURLDTO itemUrl = new ItemURLDTO();
		itemUrl.setColumns(Arrays.asList("url"));
		matching.setUrl(itemUrl);

		return matching;
	}
}
