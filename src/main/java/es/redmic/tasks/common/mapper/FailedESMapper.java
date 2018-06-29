package es.redmic.tasks.common.mapper;

import org.springframework.stereotype.Component;

import es.redmic.tasks.ingest.model.status.common.TaskStatus;
import es.redmic.tasks.ingest.model.status.dto.FailedTaskDTO;
import es.redmic.tasks.ingest.model.status.model.UserTasks;
import es.redmic.tasks.ingest.model.step.model.FailedStep;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;

@Component
public class FailedESMapper extends CustomMapper<UserTasks, FailedTaskDTO> {

	public void mapAtoB(UserTasks a, FailedTaskDTO b, MappingContext context) {
		
		FailedStep step = (FailedStep) a.lastStep();
		if (step != null) {
			b.setError(step.getError());
			b.setParameters(step.getParameters());
		}
	}
	
	@Override
	public void mapBtoA(FailedTaskDTO b, UserTasks a, MappingContext context) {
		
		FailedStep step = new FailedStep();
		step.setDate(b.getEndDate());
		step.setError(b.getError());
		step.setStatus(TaskStatus.FAILED);
		step.setParameters(b.getParameters());
		a.addStep(step);
	}
}
