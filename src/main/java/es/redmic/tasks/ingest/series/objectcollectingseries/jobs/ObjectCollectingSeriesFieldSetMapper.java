package es.redmic.tasks.ingest.series.objectcollectingseries.jobs;

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
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import es.redmic.es.maintenance.objects.service.ObjectTypeESService;
import es.redmic.exception.tasks.ingest.IngestMatchingException;
import es.redmic.models.es.common.utils.HierarchicalUtils;
import es.redmic.models.es.maintenance.objects.dto.ObjectClassificationDTO;
import es.redmic.models.es.series.objectcollecting.dto.ObjectCollectingSeriesDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ClassificationDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemClassificationDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ParameterDTO;
import es.redmic.tasks.ingest.series.common.jobs.SeriesFieldSetMapper;

public class ObjectCollectingSeriesFieldSetMapper extends SeriesFieldSetMapper<ObjectCollectingSeriesDTO>
		implements FieldSetMapper<List<ObjectCollectingSeriesDTO>> {

	ObjectCollectingSeriesParameters jobParameters;

	ObjectTypeESService objectTypeESService;

	public ObjectCollectingSeriesFieldSetMapper(ObjectCollectingSeriesParameters jobParameters,
			ObjectTypeESService objectTypeESService) {

		super(jobParameters);
		this.jobParameters = jobParameters;
		this.objectTypeESService = objectTypeESService;
	}

	@Override
	public List<ObjectCollectingSeriesDTO> mapFieldSet(FieldSet fieldSet) throws BindException {

		List<ObjectCollectingSeriesDTO> objectCollectingSeriesDTOList = new ArrayList<ObjectCollectingSeriesDTO>();

		ParameterDTO parameterProp = jobParameters.getMatching().getParameter();
		ClassificationDTO classifications = jobParameters.getMatching().getClassifications();
		List<ItemClassificationDTO> classificationsMatching = classifications.getMatching();

		ObjectCollectingSeriesDTO commonDTO = getCommonDTO(fieldSet, jobParameters.getMatching().getItemsCommon());

		for (ItemClassificationDTO match : classificationsMatching) {

			List<ObjectClassificationDTO> objectClassificationList = new ArrayList<ObjectClassificationDTO>();

			try {
				ObjectCollectingSeriesDTO dtoTemp = (ObjectCollectingSeriesDTO) BeanUtils.cloneBean(commonDTO);
				List<String> classificationsIds = HierarchicalUtils
						.getIdsFromPaths((List<String>) match.returnValue(fieldSet));
				// A partir de las categorias enviadas por el usuario, se
				// construye la clasificación
				for (int i = 0; i < classificationsIds.size(); i++) {
					objectClassificationList
							.add(objectTypeESService.getObjectClassification(classificationsIds.get(i)));
				}
				PropertyUtils.setProperty(dtoTemp, classifications.getField(), objectClassificationList);

				// Se asigna el parámetro al dto (para ello se setea la columna
				// de la clasificación)
				parameterProp.setColumns(match.getColumns());
				dtoTemp.setDataDefinition(parameterProp.getDataDefinitionId());
				PropertyUtils.setProperty(dtoTemp, parameterProp.getField(), parameterProp.returnValue(fieldSet));

				objectCollectingSeriesDTOList.add(dtoTemp);
			} catch (Exception e) {
				throw new IngestMatchingException(jobParameters.getTaskId(), e);
			}
		}

		return objectCollectingSeriesDTOList;
	}
}
