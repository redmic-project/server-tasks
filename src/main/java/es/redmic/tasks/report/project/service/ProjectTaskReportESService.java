package es.redmic.tasks.report.project.service;

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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.redmic.es.administrative.service.ActivityESService;
import es.redmic.es.administrative.service.ProgramESService;
import es.redmic.es.administrative.service.ProjectESService;
import es.redmic.models.birt.ProjectWrapper;
import es.redmic.models.es.administrative.dto.ActivityDTO;
import es.redmic.models.es.administrative.dto.ProgramDTO;
import es.redmic.models.es.administrative.dto.ProjectDTO;
import es.redmic.models.es.common.dto.JSONCollectionDTO;
import es.redmic.tasks.report.common.repository.ReportRepository;
import es.redmic.tasks.report.common.service.ReportService;

@Service
public class ProjectTaskReportESService extends ReportService<ProjectWrapper> {

	@Autowired
	public ProjectTaskReportESService(ReportRepository repository) {
		super(repository);
	}

	private static String designInfoPath = "ProjectInfoReport";
	private static String designListPath = "ProjectListReport";

	@Autowired
	ProjectESService service;

	@Autowired
	ActivityESService activityService;

	@Autowired
	ProgramESService programService;

	private void setDesigns() {

		setDesignInfo(designInfoPath);
		setDesignList(designListPath);
	}

	@Override
	public ProjectWrapper getWrapper() {

		return null;
	}

	@Override
	public ArrayList<ProjectWrapper> getListWrapper() {

		return null;
	}

	@Override
	public ArrayList<ProjectWrapper> getListWrapper(List<String> ids) {

		setDesigns();

		ArrayList<ProjectWrapper> data = new ArrayList<ProjectWrapper>();
		for (int i = 0; i < ids.size(); i++) {
			ProjectDTO project = service.get(ids.get(i));
			ProjectWrapper wrap = new ProjectWrapper();
			wrap.setProject(project);

			ProgramDTO program = programService.get(project.getParent().getId().toString());
			wrap.setProgram(program);

			wrap.setActivities(getActivityList(Long.toString(project.getId())));

			data.add(wrap);
		}
		return data;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ProjectWrapper> getListWrapper(String programId) {

		JSONCollectionDTO response = service.getProjectsByProgram(programId);
		List<ProjectDTO> projects = response.getData();

		ArrayList<ProjectWrapper> data = new ArrayList<ProjectWrapper>();
		for (int i = 0; i < projects.size(); i++) {
			ProjectDTO project = projects.get(i);
			ProjectWrapper wrap = new ProjectWrapper();
			wrap.setProject(project);
			wrap.setActivities(getActivityList(Long.toString(project.getId())));
			data.add(wrap);
		}
		return data;
	}

	@SuppressWarnings("unchecked")
	public List<ActivityDTO> getActivityList(String projectId) {

		JSONCollectionDTO response = activityService.getActivitiesByProject(projectId);
		return response.getData();
	}
}
