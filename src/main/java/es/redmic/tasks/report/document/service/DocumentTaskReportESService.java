package es.redmic.tasks.report.document.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.redmic.es.administrative.service.ActivityESService;
import es.redmic.es.administrative.service.DocumentESService;
import es.redmic.models.birt.DocumentWrapper;
import es.redmic.models.es.administrative.dto.ActivityDTO;
import es.redmic.models.es.administrative.dto.DocumentDTO;
import es.redmic.models.es.common.dto.JSONCollectionDTO;
import es.redmic.models.es.common.query.dto.DataQueryDTO;
import es.redmic.tasks.report.common.repository.ReportRepository;
import es.redmic.tasks.report.common.service.ReportService;

@Service
public class DocumentTaskReportESService extends ReportService<DocumentWrapper> {

	@Autowired
	public DocumentTaskReportESService(ReportRepository repository) {
		super(repository);
	}

	private static String designInfoPath = "DocumentInfoReport";
	private static String designListPath = "DocumentListReport";

	@Autowired
	DocumentESService service;

	@Autowired
	ActivityESService activityService;

	private void setDesigns() {
		setDesignInfo(designInfoPath);
		setDesignList(designListPath);
	}

	@Override
	public DocumentWrapper getWrapper() {
		return null;
	}

	@Override
	public ArrayList<DocumentWrapper> getListWrapper() {
		return null;
	}

	@Override
	public ArrayList<DocumentWrapper> getListWrapper(List<String> ids) {

		setDesigns();

		ArrayList<DocumentWrapper> data = new ArrayList<DocumentWrapper>();
		for (int i = 0; i < ids.size(); i++) {
			DocumentDTO document = service.get(ids.get(i));
			DocumentWrapper wrap = new DocumentWrapper();
			wrap.setDocument(document);
			wrap.setActivities(getActivityList(Long.toString(document.getId())));
			data.add(wrap);
		}
		return data;
	}

	@SuppressWarnings("unchecked")
	public List<ActivityDTO> getActivityList(String documentId) {

		JSONCollectionDTO result = service.getActivities(new DataQueryDTO(), Long.parseLong(documentId));
		return result.getData();
	}
}
