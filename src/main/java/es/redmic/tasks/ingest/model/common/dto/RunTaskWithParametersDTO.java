package es.redmic.tasks.ingest.model.common.dto;

public class RunTaskWithParametersDTO<TParams extends TaskParametersCommon> extends RunTaskDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7988125623626364819L;

	TParams parameters;

	public RunTaskWithParametersDTO() {

	}

	@SuppressWarnings("unchecked")
	public RunTaskWithParametersDTO(Class<? extends TaskParametersCommon> clazz) {
		try {
			parameters = (TParams) clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public TParams getParameters() {

		return parameters;
	}

	public void setParameters(TParams parameters) {
		this.parameters = parameters;
	}

}
