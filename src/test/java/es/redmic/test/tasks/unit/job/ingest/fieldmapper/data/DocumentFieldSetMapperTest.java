package es.redmic.test.tasks.unit.job.ingest.fieldmapper.data;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.redmic.es.maintenance.domain.administrative.service.DocumentTypeESService;
import es.redmic.models.es.administrative.dto.DocumentDTO;
import es.redmic.models.es.maintenance.administrative.dto.DocumentTypeDTO;
import es.redmic.tasks.ingest.data.document.jobs.DocumentFieldSetMapper;
import es.redmic.tasks.ingest.data.document.jobs.DocumentParameters;
import es.redmic.tasks.ingest.model.matching.data.document.dto.DocumentMatching;
import es.redmic.test.tasks.utils.mapper.JsonToBeanTestUtil;

@RunWith(MockitoJUnitRunner.class)
public class DocumentFieldSetMapperTest {

	private static final String TITLE = "title";
	private static final String CODE = "code";
	private static final String REMARK = "remark";
	private static final String AUTHOR = "author";
	private static final String YEAR = "year";
	private static final String SOURCE = "source";
	private static final String LANGUAGE = "language";
	private static final String KEYWORDS = "keywords";
	private static final String DOCUMENT_TYE = "documentType";
	private static final String URL = "url";

	private static final String V_TITLE = "Prueba ingesta csv";
	private static final String V_CODE = "PB-01";
	private static final String V_REMARK = "Añade o modifica bibliografía a partir de un fichero csv";
	private static final String V_AUTHOR = "Redmic";
	private static final Integer V_YEAR = 2017;
	private static final String V_SOURCE = "Ingesta csv que añade o modifica bibliografía a partir de un fichero csv";
	private static final String V_LANGUAGE = "es-ES";
	private static final String V_KEYWORDS = "LIBRO, BIBLIOGRAFÍA";
	private static final String V_DOCUMENT_TYE = "Libro de colección";
	private static final String V_URL = "X:\\Info3\\Bibliografía\\BibOAG-05741.pdf";

	@Mock
	DocumentTypeESService documentTypeESService;

	@Value("${property.URL_DOCUMENTS}")
	private String URL_DOCUMENTS;

	DocumentTypeDTO documentTypeDTO;

	private ObjectMapper jacksonMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
			false);

	protected FieldSet createFieldSet() {
		String[] columnNames = new String[] { TITLE, CODE, REMARK, AUTHOR, YEAR, SOURCE, LANGUAGE, KEYWORDS,
				DOCUMENT_TYE, URL };
		String[] tokens = new String[] { V_TITLE, V_CODE, V_REMARK, V_AUTHOR, V_YEAR.toString(), V_SOURCE, V_LANGUAGE,
				V_KEYWORDS, V_DOCUMENT_TYE, V_URL };

		return new DefaultFieldSet(tokens, columnNames);
	}

	@Before
	public void setupTest() throws IOException {

		documentTypeDTO = (DocumentTypeDTO) JsonToBeanTestUtil
				.getBean("/data/ingest/dto/item/document/documenttype.json", DocumentTypeDTO.class);
		when(documentTypeESService.findByName(anyString())).thenReturn(documentTypeDTO);
	}

	@Test
	public void parseLineCSVToReturn2BeansTimeSeriesDTOWithEqualsDateTime() throws Exception {

		FieldSet fs = createFieldSet();
		DocumentParameters parameters = createConfig();
		DocumentFieldSetMapper fieldSetMapper = new DocumentFieldSetMapper(parameters, documentTypeESService,
				URL_DOCUMENTS);

		List<DocumentDTO> actual = fieldSetMapper.mapFieldSet(fs);

		DocumentDTO expectedDocumentDTO = expectedDto();

		assertEquals(actual.size(), 1);
		assertEquals(actual.get(0), expectedDocumentDTO);
	}

	protected DocumentDTO expectedDto() {

		DocumentDTO dto = new DocumentDTO();

		dto.setAuthor(V_AUTHOR);
		dto.setCode(V_CODE);
		dto.setDocumentType(documentTypeDTO);
		dto.setKeyword(V_KEYWORDS);
		dto.setLanguage(V_LANGUAGE);
		dto.setRemark(V_REMARK);
		dto.setSource(V_SOURCE);
		dto.setTitle(V_TITLE);
		dto.setYear(V_YEAR);
		dto.setUrl(URL + "BibOAG-05741.pdf");
		return dto;
	}

	protected DocumentParameters createConfig() throws IOException {

		DocumentParameters parameters = new DocumentParameters();
		parameters.setMatching(readListResourceJson("/data/ingest/dto/item/document/matching-all.json"));
		return parameters;
	}

	private DocumentMatching readListResourceJson(String path) throws IOException {

		InputStream resource = getClass().getResource(path).openStream();

		return jacksonMapper.readValue(resource, DocumentMatching.class);
	}
}