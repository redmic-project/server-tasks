package es.redmic.test.tasks.unit.job.ingest.validator.data;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.redmic.exception.tasks.ingest.IngestMatchingColumnRequiredException;
import es.redmic.exception.tasks.ingest.IngestMatchingColumnSizeException;
import es.redmic.exception.tasks.ingest.MatchingDataBindingException;
import es.redmic.models.es.maintenance.administrative.dto.DocumentTypeDTO;
import es.redmic.tasks.ingest.data.document.jobs.DocumentMatchingParametersValidator;
import es.redmic.tasks.ingest.data.document.jobs.DocumentParameters;
import es.redmic.tasks.ingest.model.matching.data.document.dto.DocumentMatching;
import es.redmic.test.tasks.utils.mapper.JsonToBeanTestUtil;

@RunWith(MockitoJUnitRunner.class)
public class DocumentMatchingParametersValidatorTest {

	final String PATH_MATCHING = "/data/ingest/dto/matching/document/";
	final String PATH_DATA = "/data/csv/document/";

	@Mock
	ObjectMapper jacksonMapper;

	@InjectMocks
	DocumentMatchingParametersValidator validator;

	DocumentTypeDTO documentTypeDTO;

	@Before
	public void before() throws IOException {

		documentTypeDTO = (DocumentTypeDTO) JsonToBeanTestUtil
				.getBean("/data/ingest/dto/item/document/documenttype.json", DocumentTypeDTO.class);
	}

	@Test
	public void checkConstraints_ReturnSuccess_IfDataIsCorrect() throws Exception {

		DocumentParameters parameters = completeDocumentParameters(PATH_MATCHING + "job-01.json",
				PATH_DATA + "job-01.csv");

		Whitebox.<Boolean>invokeMethod(validator, "checkConstraints", parameters);
	}

	@Test(expected = IngestMatchingColumnSizeException.class)
	public void checkHeader_ThrowIngestMatchinException_IfExistMoreMatchingThanHeaderFields() throws Exception {

		DocumentParameters parameters = completeDocumentParameters(PATH_MATCHING + "job-01.json",
				PATH_DATA + "job-01.csv");

		parameters.getHeader().remove(0);
		parameters.getHeader().remove(1);

		Whitebox.<Boolean>invokeMethod(validator, "checkHeader", parameters.getTaskId(), parameters.getHeader(),
				parameters.getMatching().getMatchingColumns());
	}

	@Test(expected = MatchingDataBindingException.class)
	public void checkDataBindingConstraints_ThrowIngestMatchinException_IfParametersIsNotComplete() throws Exception {

		DocumentParameters parameters = completeDocumentParameters(PATH_MATCHING + "job-01.json",
				PATH_DATA + "job-01.csv");
		parameters.setUserId(null);
		Whitebox.<Boolean>invokeMethod(validator, "checkDataBindingConstraints", parameters);
	}

	@Test(expected = IngestMatchingColumnRequiredException.class)
	public void checkFileConstraints_ThrowIngestMatchinException_IfInFileNotExistRequiredHeader() throws Exception {

		DocumentParameters parameters = completeDocumentParameters(PATH_MATCHING + "job-01.json",
				PATH_DATA + "job-01.csv");

		parameters.getHeader().remove(0);

		Whitebox.<Boolean>invokeMethod(validator, "checkFileConstraints", parameters.getTaskId(),
				parameters.getHeader(), parameters.getMatching().getMatchingColumns());
	}

	private DocumentParameters completeDocumentParameters(String matchingPath, String csvPath) throws IOException {

		DocumentParameters parameters = new DocumentParameters();

		DocumentMatching matching = (DocumentMatching) JsonToBeanTestUtil.getBean(matchingPath, DocumentMatching.class);
		String file = getClass().getResource(csvPath).getFile();
		int index = file.lastIndexOf("/");
		String filename = file.substring(index + 1);

		parameters.setMatching(matching);
		parameters.setFileName(filename);

		List<String> header = new ArrayList<String>();
		header.add("title");
		header.add("code");
		header.add("remark");
		header.add("author");
		header.add("year");
		header.add("source");
		header.add("language");
		header.add("keywords");
		header.add("documentType");
		header.add("url");

		parameters.setHeader(header);

		parameters.setDelimiter(";");
		parameters.setTaskId("33");
		parameters.setUserId("233");

		return parameters;
	}
}
