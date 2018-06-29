package es.redmic.tasks.ingest.common.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import es.redmic.tasks.ingest.model.intervention.common.InterventionType;
import es.redmic.tasks.ingest.model.intervention.matching.InterventionMatching;
import es.redmic.tasks.ingest.model.intervention.matching.RequestUserInterventionMatchingTaskDTO;
import es.redmic.tasks.ingest.model.status.common.TaskStatus;
import es.redmic.tasks.ingest.model.status.model.UserTasks;
import es.redmic.tasks.ingest.model.step.intervention.WaitingInterventionStep;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;

@Component
public class InterventionMatchingESMapper extends CustomMapper<UserTasks, RequestUserInterventionMatchingTaskDTO> {

	private final Logger LOGGER = LoggerFactory.getLogger(InterventionMatchingESMapper.class);

	public void mapAtoB(UserTasks a, RequestUserInterventionMatchingTaskDTO b, MappingContext context) {
		
		WaitingInterventionStep step = (WaitingInterventionStep) a.lastStep();
		if ((step.getStatus() != TaskStatus.WAITING_INTERVENTION)
				&& (step.getInterventionDescription().getType() != InterventionType.MATCHING)) {
			
			LOGGER.error("El último step no es necesita intervención");
			// TODO Lanzar exception
		}
		
		b.setInterventionDescription((InterventionMatching) step.getInterventionDescription());
	}
}
