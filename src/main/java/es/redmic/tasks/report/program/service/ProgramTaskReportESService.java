package es.redmic.tasks.report.program.service;

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
import es.redmic.models.birt.ProgramWrapper;
import es.redmic.models.birt.ProjectWrapper;
import es.redmic.models.es.administrative.dto.ProgramDTO;
import es.redmic.tasks.report.common.repository.ReportRepository;
import es.redmic.tasks.report.common.service.ReportService;
import es.redmic.tasks.report.project.service.ProjectTaskReportESService;

@Service
public class ProgramTaskReportESService extends ReportService<ProgramWrapper> {

	@Autowired
	public ProgramTaskReportESService(ReportRepository repository) {
		super(repository);
	}

	private static String designInfoPath = "ProgramInfoReport";
	private static String designListPath = "ProgramListReport";

	@Autowired
	ProgramESService service;

	@Autowired
	ActivityESService activityService;

	@Autowired
	ProjectESService projectService;

	@Autowired
	ProjectTaskReportESService projectTaskReportService;

	private void setDesigns() {
		setDesignInfo(designInfoPath);
		setDesignList(designListPath);
	}

	@Override
	public ProgramWrapper getWrapper() {
		return null;
	}

	@Override
	public ArrayList<ProgramWrapper> getListWrapper() {
		return null;
	}

	@Override
	public ArrayList<ProgramWrapper> getListWrapper(List<String> ids) {

		setDesigns();

		ArrayList<ProgramWrapper> data = new ArrayList<ProgramWrapper>();
		for (int i = 0; i < ids.size(); i++) {
			ProgramDTO program = service.get(ids.get(i));
			String programId = Long.toString(program.getId());
			ProgramWrapper wrap = new ProgramWrapper();
			wrap.setProgram(program);
			ArrayList<ProjectWrapper> projectWrappers = projectTaskReportService.getListWrapper(programId);
			wrap.setProjectWrappers(projectWrappers);
			data.add(wrap);
		}
		return data;
	}
}
