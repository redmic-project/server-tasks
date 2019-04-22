package es.redmic.tasks.ingest.series.common.jobs;

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

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.batch.item.file.transform.FieldSet;

import es.redmic.exception.tasks.ingest.IngestMatchingException;
import es.redmic.models.es.series.common.dto.SeriesBaseDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemCommonDTO;

public abstract class SeriesFieldSetMapper<TDTO extends SeriesBaseDTO> {

	SeriesParameters jobParameters;

	private Class<TDTO> typeOfTDTO;

	@SuppressWarnings("unchecked")
	public SeriesFieldSetMapper(SeriesParameters jobParameters) {

		this.jobParameters = jobParameters;

		this.typeOfTDTO = (Class<TDTO>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	protected TDTO getCommonDTO(FieldSet fieldSet, List<ItemCommonDTO> commonProps) {

		TDTO commonDTO;
		try {
			commonDTO = typeOfTDTO.newInstance();
		} catch (InstantiationException | IllegalAccessException e1) {
			throw new IngestMatchingException(jobParameters.getTaskId(), e1);
		}

		commonDTO.set_grandparentId(jobParameters.getActivityId());
		commonDTO.set_parentId(jobParameters.getSurveyId());

		try {
			// @formatter:off
			for (ItemCommonDTO match : commonProps) {
		    	Object value = match.returnValue(fieldSet);
		    	if (value instanceof String) {
		    		String str = (String) value;
		    		if (str.length() == 0)
		    			value = null;
		    	}
				PropertyUtils.setProperty(commonDTO, match.getField(), value);
				// @formatter:on
			}
		} catch (Exception e) {
			throw new IngestMatchingException(jobParameters.getTaskId(), e);
		}

		return commonDTO;
	}
}
