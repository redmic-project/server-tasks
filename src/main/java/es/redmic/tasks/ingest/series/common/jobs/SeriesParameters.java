package es.redmic.tasks.ingest.series.common.jobs;

import javax.validation.constraints.NotNull;

import es.redmic.tasks.ingest.common.jobs.CSVParameters;

public abstract class SeriesParameters extends CSVParameters {

	@NotNull
	String surveyId;

	public SeriesParameters() {

	}

	public String getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}
}