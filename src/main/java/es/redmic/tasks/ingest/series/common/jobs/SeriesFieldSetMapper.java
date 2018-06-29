package es.redmic.tasks.ingest.series.common.jobs;

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