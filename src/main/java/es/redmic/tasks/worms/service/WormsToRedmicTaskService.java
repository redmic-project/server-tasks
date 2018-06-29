package es.redmic.tasks.worms.service;

import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.redmic.tasks.common.service.TaskJobBaseService;
import es.redmic.tasks.worms.repository.WormsToRedmicTaskRepository;

@Service
public class WormsToRedmicTaskService extends TaskJobBaseService {

	WormsToRedmicTaskRepository wormsToRedmicRepository;

	@Autowired
	public WormsToRedmicTaskService(WormsToRedmicTaskRepository wormsToRedmicRepository) {
		super(wormsToRedmicRepository);
		this.wormsToRedmicRepository = wormsToRedmicRepository;
	}

	public void resume(String taskId) {

		JobParameters newParameters = wormsToRedmicRepository.resume(taskId);
		resumeJob(taskId, newParameters);
	}
}