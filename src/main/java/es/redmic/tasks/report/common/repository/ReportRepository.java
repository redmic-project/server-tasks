package es.redmic.tasks.report.common.repository;

import org.springframework.batch.core.JobParameters;
import org.springframework.stereotype.Repository;

import es.redmic.tasks.common.repository.TaskBaseRepository;
import es.redmic.tasks.common.utils.TasksTypes;

@Repository
public class ReportRepository extends TaskBaseRepository {

	@Override
	public JobParameters getJobParameters(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getTaskType() {
		return TasksTypes.REPORT;
	}

}
