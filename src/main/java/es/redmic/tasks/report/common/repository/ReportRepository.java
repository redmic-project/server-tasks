package es.redmic.tasks.report.common.repository;

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
