package es.redmic.tasks.report.common.service;

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

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ReportFileService {

	@Value("${property.path.temp.report}")
	private String PATH_BASE;

	public ReportFileService() {
	}

	public void deleteReportFile(String url) {

		String[] urlPath = url.split("/");
		String name = urlPath[urlPath.length - 1];
		try {
			File file = new File(PATH_BASE + "/", name);
			file.delete();
		} catch (Exception e) {
			// throw new ResourceNotFoundException("No se pudo encontar el
			// fichero", e);
		}
	}

}
