package es.redmic.tasks.ingest.model.intervention.matching;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import es.redmic.tasks.ingest.model.intervention.common.InterventionDescriptionBase;
import es.redmic.tasks.ingest.model.intervention.common.InterventionType;

public class InterventionMatching extends InterventionDescriptionBase {

	Object matching;

	List<Map<String, String>> data;
	
	@NotNull
	@Size(min=1)
	List<String> header;

	public InterventionMatching() {
		setType(InterventionType.MATCHING);
	}

	public Object getMatching() {
		return matching;
	}

	public void setMatching(Object matching) {
		
		this.matching = matching;
	}

	public List<Map<String, String>> getData() {
		return data;
	}

	public void setData(List<Map<String, String>> data) {
		this.data = data;
	}

	public List<String> getHeader() {
		return header;
	}

	public void setHeader(List<String> header) {
		this.header = header;
	}
}
