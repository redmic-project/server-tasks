package es.redmic.tasks.report.common.service;

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
