package es.redmic.tasks.report.activity.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import es.redmic.es.administrative.service.ActivityBaseESService;
import es.redmic.es.administrative.service.ActivityESService;
import es.redmic.models.birt.ActivityWrapper;
import es.redmic.models.es.administrative.dto.ActivityBaseDTO;
import es.redmic.models.es.administrative.dto.ActivityDTO;
import es.redmic.models.es.common.dto.JSONCollectionDTO;
import es.redmic.models.es.common.query.dto.MgetDTO;
import es.redmic.models.es.common.utils.HierarchicalUtils;
import es.redmic.tasks.report.common.repository.ReportRepository;
import es.redmic.tasks.report.common.service.ReportService;

@Service
public class ActivityTaskReportESService extends ReportService<ActivityWrapper> {

	@Autowired
	public ActivityTaskReportESService(ReportRepository repository) {
		super(repository);
	}

	private static String designInfoPath = "ActivityInfoReport";
	private static String designListPath = "ActivityListReport";

	@Autowired
	ActivityESService service;

	@Autowired
	@Qualifier("ActivityBaseServiceES")
	ActivityBaseESService activityBaseService;

	private void setDesigns() {
		setDesignInfo(designInfoPath);
		setDesignList(designListPath);
	}

	@Override
	public ActivityWrapper getWrapper() {
		return null;
	}

	@Override
	public ArrayList<ActivityWrapper> getListWrapper() {
		return null;
	}

	@Override
	public ArrayList<ActivityWrapper> getListWrapper(List<String> ids) {

		setDesigns();

		ArrayList<ActivityWrapper> data = new ArrayList<ActivityWrapper>();
		for (int i = 0; i < ids.size(); i++) {
			ActivityDTO activity = service.get(ids.get(i));
			ActivityWrapper wrap = new ActivityWrapper();
			wrap.setActivity(activity);

			setActivityAncestors(wrap, activity.getPath());

			data.add(wrap);
		}
		return data;
	}

	@SuppressWarnings({ "unchecked" })
	public void setActivityAncestors(ActivityWrapper wrap, String path) {

		String[] ancestorIds = HierarchicalUtils.getAncestorsIds(path);

		MgetDTO dto = new MgetDTO(Arrays.asList(ancestorIds));
		JSONCollectionDTO result = activityBaseService.mget(dto);

		List<ActivityBaseDTO> data = result.getData();
		for (int i = 0; i < data.size(); i++) {
			ActivityBaseDTO ancestor = data.get(i);
			int pathLength = StringUtils.countMatches(ancestor.getPath(), ".");
			if (pathLength > 1) {
				wrap.setProject(ancestor);
			} else {
				wrap.setProgram(ancestor);
			}
		}
	}
}
