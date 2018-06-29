package es.redmic.tasks.ingest.model.step.intervention;

import javax.validation.constraints.NotNull;

import es.redmic.tasks.ingest.model.intervention.common.InterventionType;

public class ResponseMatchingIntervention extends ResponseInterventionBase {
	
	//@NotEmpty
	//@Valid
	/*List<MatchingItemBaseDTO>*/
	@NotNull
	Object matching;
	
	public ResponseMatchingIntervention() {
		setType(InterventionType.MATCHING);
	}

	public Object getMatching() {
		return matching;
	}

	public void setMatching(Object matching) {
		this.matching = matching;
	}
}
