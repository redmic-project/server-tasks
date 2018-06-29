package es.redmic.tasks.common.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.redmic.exception.tasks.ingest.IngestBaseException;
import es.redmic.exception.tasks.ingest.JobExecutionException;
import es.redmic.tasks.common.repository.TaskBaseRepository;
import es.redmic.tasks.ingest.model.common.dto.RunTaskWithParametersDTO;
import es.redmic.tasks.ingest.model.status.dto.RegisteredTaskDTO;
import es.redmic.tasks.ingest.model.status.dto.StartedTaskDTO;

public abstract class TaskJobBaseService extends TaskBaseService {

	@Autowired
	protected JobLauncher jobLauncher;

	protected JobExecution jobExecution;

	protected Job indexingJob;

	@Autowired
	protected ObjectMapper objectMapper;

	private TaskBaseRepository ingestRepository;

	public TaskJobBaseService(TaskBaseRepository intestRepository) {
		this.ingestRepository = intestRepository;
	}

	public RegisteredTaskDTO register(Long userId, RunTaskWithParametersDTO<?> runDto) {

		return ingestRepository.register(userId, runDto);
	}

	public StartedTaskDTO run(String taskId) {

		return ingestRepository.run(taskId);
	}

	protected void resumeJob(String taskId, JobParameters newParameters) {

		try {
			jobExecution = jobLauncher.run(indexingJob, newParameters);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			throw new JobExecutionException(taskId, e);
		}
	}

	public void setInProgress(String taskId) {

		publishTaskStatusToUser(ingestRepository.setInProgress(taskId));
	}

	public void setFailed(IngestBaseException ex) {

		publishTaskStatusToUser(ingestRepository.setFailed(ex));
	}

	public void finish(String taskId) {

		publishTaskStatusToUser(ingestRepository.finish(taskId));
	}

	public Job getIndexingJob() {
		return indexingJob;
	}

	public void setIndexingJob(Job indexingJob) {
		this.indexingJob = indexingJob;
	}
}