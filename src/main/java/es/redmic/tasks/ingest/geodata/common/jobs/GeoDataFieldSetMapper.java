package es.redmic.tasks.ingest.geodata.common.jobs;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.batch.item.file.transform.FieldSet;

import es.redmic.exception.tasks.ingest.IngestMatchingException;
import es.redmic.models.es.geojson.common.dto.MetaFeatureDTO;
import es.redmic.models.es.geojson.common.dto.PropertiesDTO;
import es.redmic.tasks.ingest.common.jobs.Parameters;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemCommonDTO;

public abstract class GeoDataFieldSetMapper<TDTO extends MetaFeatureDTO<?, ?>> {

	Parameters jobParameters;

	private Class<TDTO> typeOfTDTO;

	@SuppressWarnings("unchecked")
	public GeoDataFieldSetMapper(Parameters jobParameters) {

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
		((PropertiesDTO) commonDTO.getProperties()).setActivityId(jobParameters.getActivityId());
		try {
			// @formatter:off
			for (ItemCommonDTO match : commonProps) {
		    	Object value = match.returnValue(fieldSet);
		    	if (value instanceof String) {
		    		String str = (String) value;
		    		if (str.length() == 0)
		    			value = null;
		    	}
				PropertyUtils.setProperty(commonDTO.getProperties(), match.getField(), value);
				// @formatter:on
			}
		} catch (Exception e) {
			throw new IngestMatchingException(jobParameters.getTaskId(), e);
		}

		return commonDTO;
	}
}