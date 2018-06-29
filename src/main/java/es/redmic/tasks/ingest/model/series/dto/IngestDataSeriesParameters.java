package es.redmic.tasks.ingest.model.series.dto;

import javax.validation.constraints.NotNull;

import es.redmic.tasks.ingest.model.common.dto.TaskParametersBase;

public class IngestDataSeriesParameters extends TaskParametersBase {
	
	@NotNull
	String surveyId;
	
	public IngestDataSeriesParameters() {
		super();
	}

	public String getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}
}