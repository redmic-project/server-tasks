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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import es.redmic.es.maintenance.domain.administrative.service.DocumentTypeESService;
import es.redmic.models.es.administrative.dto.DocumentDTO;
import es.redmic.models.es.maintenance.administrative.dto.DocumentTypeDTO;
import es.redmic.tasks.ingest.data.common.job.DataFieldSetMapper;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemCodeDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemKeywordDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemLanguageDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemRemarkDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemURLDTO;
import es.redmic.tasks.ingest.model.matching.data.document.dto.ItemAuthorDTO;
import es.redmic.tasks.ingest.model.matching.data.document.dto.ItemDocumentTypeDTO;
import es.redmic.tasks.ingest.model.matching.data.document.dto.ItemSourceDTO;
import es.redmic.tasks.ingest.model.matching.data.document.dto.ItemTitleDTO;
import es.redmic.tasks.ingest.model.matching.data.document.dto.ItemYearDTO;

public class DocumentFieldSetMapper extends DataFieldSetMapper<DocumentDTO>
		implements FieldSetMapper<List<DocumentDTO>> {

	private DocumentParameters jobParameters;

	private DocumentTypeESService documentTypeESService;

	private final String urlBase;

	private Map<String, DocumentTypeDTO> documentTypesCache = new HashMap<String, DocumentTypeDTO>();

	public DocumentFieldSetMapper(DocumentParameters jobParameters, DocumentTypeESService documentTypeESService,
			String urlBase) {
		super(jobParameters);
		this.jobParameters = jobParameters;
		this.documentTypeESService = documentTypeESService;
		this.urlBase = urlBase;
	}

	@Override
	public List<DocumentDTO> mapFieldSet(FieldSet fieldSet) throws BindException {

		List<DocumentDTO> documentDTOList = new ArrayList<DocumentDTO>();

		DocumentDTO commonDTO = getCommonDTO(fieldSet, jobParameters.getMatching().getItemsCommon());

		ItemTitleDTO itemTitle = jobParameters.getMatching().getTitle();

		if (itemTitle != null)
			commonDTO.setTitle(itemTitle.returnValue(fieldSet));

		ItemCodeDTO itemCode = jobParameters.getMatching().getCode();

		if (itemCode != null)
			commonDTO.setCode(itemCode.returnValue(fieldSet));

		ItemRemarkDTO itemRemark = jobParameters.getMatching().getRemark();

		if (itemRemark != null)
			commonDTO.setRemark(itemRemark.returnValue(fieldSet));

		ItemAuthorDTO itemAuthor = jobParameters.getMatching().getAuthor();

		if (itemAuthor != null)
			commonDTO.setAuthor(itemAuthor.returnValue(fieldSet));

		ItemYearDTO itemYear = jobParameters.getMatching().getYear();

		if (itemYear != null)
			commonDTO.setYear(itemYear.returnValue(fieldSet));

		ItemSourceDTO itemSource = jobParameters.getMatching().getSource();

		if (itemSource != null)
			commonDTO.setSource(itemSource.returnValue(fieldSet));

		ItemLanguageDTO itemLanguage = jobParameters.getMatching().getLanguage();

		if (itemLanguage != null)
			commonDTO.setLanguage(itemLanguage.returnValue(fieldSet));

		ItemKeywordDTO itemKeywords = jobParameters.getMatching().getKeywords();

		if (itemKeywords != null) {

			String keywords = itemKeywords.returnValue(fieldSet);

			if (keywords != null)
				commonDTO.setKeyword(keywords.replace("/", ","));
		}

		ItemDocumentTypeDTO itemDocumentType = jobParameters.getMatching().getDocumentType();

		if (itemDocumentType != null)
			commonDTO.setDocumentType(getDocumentTypeByName(itemDocumentType.returnValue(fieldSet)));

		ItemURLDTO itemUrl = jobParameters.getMatching().getUrl();

		if (itemUrl != null && itemUrl.returnValue(fieldSet) != null)
			commonDTO.setUrl(getInternalUrl(itemUrl.returnValue(fieldSet)));

		documentDTOList.add(commonDTO);

		return documentDTOList;
	}

	private DocumentTypeDTO getDocumentTypeByName(String documentTypeName) {

		if (documentTypesCache.containsKey(documentTypeName))
			return documentTypesCache.get(documentTypeName);

		DocumentTypeDTO result = documentTypeESService.findByName(documentTypeName);

		documentTypesCache.put(documentTypeName, result);

		return result;
	}

	private String getInternalUrl(String path) {

		String[] pathSplit = path.split("\\\\");

		if (pathSplit == null || pathSplit.length <= 0)
			return null;

		return urlBase + pathSplit[pathSplit.length - 1];
	}
}
