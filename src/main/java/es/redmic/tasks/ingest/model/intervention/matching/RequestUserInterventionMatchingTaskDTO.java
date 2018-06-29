package es.redmic.tasks.ingest.model.intervention.matching;

import es.redmic.tasks.ingest.model.status.common.TaskStatus;
import es.redmic.tasks.ingest.model.status.dto.UserTaskDTO;

public class RequestUserInterventionMatchingTaskDTO extends UserTaskDTO {
	
	InterventionMatching interventionDescription;
	
	public RequestUserInterventionMatchingTaskDTO() {
		setStatus(TaskStatus.WAITING_INTERVENTION);

	}
	
	public InterventionMatching getInterventionDescription() {
		return interventionDescription;
	}

	public void setInterventionDescription(InterventionMatching interventionDescription) {
		this.interventionDescription = interventionDescription;
	}
	
}
