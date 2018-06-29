package es.redmic.tasks.worms.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.redmic.tasks.common.listener.TaskListener;
import es.redmic.tasks.worms.jobs.WormsToRedmicParameters;
import es.redmic.tasks.worms.service.WormsToRedmicTaskService;

@Component
public class WormsToRedmicListener extends TaskListener<WormsToRedmicParameters> {

	@Autowired
	public WormsToRedmicListener(WormsToRedmicTaskService wormsToRedmicTaskService) {
		super(wormsToRedmicTaskService);
	}
}