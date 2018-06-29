package es.redmic.tasks.report.project.service;

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
