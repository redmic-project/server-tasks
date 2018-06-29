package es.redmic.tasks.worms.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.redmic.tasks.common.listener.TaskStepListener;
import es.redmic.tasks.worms.jobs.WormsToRedmicParameters;
import es.redmic.tasks.worms.service.WormsToRedmicTaskService;

@Component
public class WormsToRedmicStepListener extends TaskStepListener<WormsToRedmicParameters> {

	@Autowired
	public WormsToRedmicStepListener(WormsToRedmicTaskService wormsToRedmicTaskService) {
		super(wormsToRedmicTaskService);
	}
}