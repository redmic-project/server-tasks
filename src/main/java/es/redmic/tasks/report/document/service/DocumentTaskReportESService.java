package es.redmic.tasks.report.document.service;

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
