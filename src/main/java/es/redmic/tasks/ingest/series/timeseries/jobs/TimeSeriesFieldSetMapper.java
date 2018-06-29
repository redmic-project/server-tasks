package es.redmic.tasks.ingest.series.timeseries.jobs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import es.redmic.exception.tasks.ingest.IngestMatchingException;
import es.redmic.models.es.series.timeseries.dto.TimeSeriesDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemParameterDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ParametersDTO;
import es.redmic.tasks.ingest.series.common.jobs.SeriesFieldSetMapper;

public class TimeSeriesFieldSetMapper extends SeriesFieldSetMapper<TimeSeriesDTO>
		implements FieldSetMapper<List<TimeSeriesDTO>> {

	private TimeSeriesParameters jobParameters;

	public TimeSeriesFieldSetMapper(TimeSeriesParameters jobParameters) {
		super(jobParameters);
		this.jobParameters = jobParameters;
	}

	@Override
	public List<TimeSeriesDTO> mapFieldSet(FieldSet fieldSet) throws BindException {

		List<TimeSeriesDTO> timeSeriesDTOList = new ArrayList<TimeSeriesDTO>();

		ParametersDTO parameters = jobParameters.getMatching().getParameters();
		List<ItemParameterDTO> parametersMatching = parameters.getMatching();

		TimeSeriesDTO commonDTO = getCommonDTO(fieldSet, jobParameters.getMatching().getItemsCommon());

		for (ItemParameterDTO match : parametersMatching) {
			TimeSeriesDTO dtoTemp;
			Double value = null;
			try {
				value = match.returnValue(fieldSet);
			} catch (Exception e) {
				throw new IngestMatchingException(jobParameters.getTaskId(), e);
			}

			if (value != null) {
				try {
					dtoTemp = (TimeSeriesDTO) BeanUtils.cloneBean(commonDTO);
					PropertyUtils.setProperty(dtoTemp, parameters.getField(), value);
					Long datDefId = match.getDataDefinitionId();
					dtoTemp.setDataDefinition(datDefId);

					timeSeriesDTOList.add(dtoTemp);

				} catch (Exception e) {
					throw new IngestMatchingException(jobParameters.getTaskId(), e);
				}
			}
		}
		return timeSeriesDTOList;
	}
}