package es.redmic.tasks.common.listener;

/*-
 * #%L
 * Tasks
 * %%
 * Copyright (C) 2019 REDMIC Project / Server
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.lang.reflect.ParameterizedType;
import java.util.Iterator;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;

import es.redmic.tasks.common.jobs.ParametersCommons;
import es.redmic.tasks.common.service.TaskJobBaseService;
import es.redmic.tasks.common.utils.JobUtils;

public abstract class TaskListener<TParameters extends ParametersCommons> implements JobExecutionListener {

	protected final static String JOB_PARAMETER_KEY = "parameters";

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskListener.class);

	private TaskJobBaseService taskService;

	private Class<TParameters> typeOfTParameters;

	@SuppressWarnings("unchecked")
	public TaskListener(TaskJobBaseService taskService) {
		this.taskService = taskService;

		this.typeOfTParameters = (Class<TParameters>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {

		JobParameter parameter = jobExecution.getJobParameters().getParameters().get(JOB_PARAMETER_KEY);
		TParameters parameters = JobUtils.JobParameters2UserParameters(parameter, typeOfTParameters);

		taskService.setInProgress(parameters.getTaskId());
		LOGGER.info("\n Job starter  \n");
		LOGGER.info(generateLogStartJob(jobExecution));
	}

	@Override
	public void afterJob(JobExecution jobExecution) {

		JobParameter parameter = jobExecution.getJobParameters().getParameters().get(JOB_PARAMETER_KEY);
		TParameters parameters = JobUtils.JobParameters2UserParameters(parameter, typeOfTParameters);
		BatchStatus status = jobExecution.getStatus();

		LOGGER.info("\n Job finish  \n");
		LOGGER.info(generateLogEndJob(jobExecution));

		if (status.isUnsuccessful()) {
			jobExecution.stop();
		} else {
			taskService.finish(parameters.getTaskId());
		}
	}

	private String generateLogStartJob(JobExecution jobExecution) {
		StringBuilder protocol = new StringBuilder();
		protocol.append("\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++ \n");
		protocol.append("Protocol for " + jobExecution.getJobInstance().getJobName() + " \n");

		return protocol.toString();
	}

	private String generateLogEndJob(JobExecution jobExecution) {
		StringBuilder protocol = new StringBuilder();
		protocol.append("\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++ \n");
		protocol.append("Protocol for " + jobExecution.getJobInstance().getJobName() + " \n");
		protocol.append("  Started     : " + jobExecution.getStartTime() + "\n");
		protocol.append("  Finished    : " + jobExecution.getEndTime() + "\n");
		protocol.append("  Exit-Code   : " + jobExecution.getExitStatus().getExitCode() + "\n");
		protocol.append("  Exit-Descr. : " + jobExecution.getExitStatus().getExitDescription() + "\n");
		protocol.append("  Status      : " + jobExecution.getStatus() + "\n");
		protocol.append("+++++++++++++++++++++++++++++++++++++++++++++++++++++++ \n");

		protocol.append("Job-Parameter: \n");
		JobParameters jp = jobExecution.getJobParameters();
		for (Iterator<Entry<String, JobParameter>> iter = jp.getParameters().entrySet().iterator(); iter.hasNext();) {
			Entry<String, JobParameter> entry = iter.next();
			protocol.append("  " + entry.getKey() + "=" + entry.getValue() + "\n");
		}
		protocol.append("+++++++++++++++++++++++++++++++++++++++++++++++++++++++ \n");

		for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
			protocol.append("\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++ \n");
			protocol.append("Step " + stepExecution.getStepName() + " \n");
			protocol.append("WriteCount: " + stepExecution.getWriteCount() + "\n");
			protocol.append("Commits: " + stepExecution.getCommitCount() + "\n");
			protocol.append("SkipCount: " + stepExecution.getSkipCount() + "\n");
			protocol.append("Rollbacks: " + stepExecution.getRollbackCount() + "\n");
			protocol.append("Filter: " + stepExecution.getFilterCount() + "\n");
			protocol.append("+++++++++++++++++++++++++++++++++++++++++++++++++++++++ \n");
		}
		return protocol.toString();
	}
}
