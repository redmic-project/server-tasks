package es.redmic.tasks.report.common.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.redmic.brokerlib.avro.common.MessageWrapper;
import es.redmic.brokerlib.utils.MessageWrapperUtils;
import es.redmic.es.common.repository.SelectionWorkRepository;
import es.redmic.exception.tasks.report.GenerateReportException;
import es.redmic.mediastorage.service.FileUtils;
import es.redmic.models.birt.Wrapper;
import es.redmic.models.es.common.dto.ReportDTO;
import es.redmic.models.es.common.dto.SelectionWorkDTO;
import es.redmic.models.es.common.model.Selection;
import es.redmic.reports.ReportUtil;
import es.redmic.tasks.common.service.TaskBaseService;
import es.redmic.tasks.common.service.UserTasksService;
import es.redmic.tasks.ingest.model.status.dto.CompletedTaskDTO;
import es.redmic.tasks.ingest.model.status.dto.RegisteredTaskDTO;
import es.redmic.tasks.ingest.model.status.dto.StartedTaskDTO;
import es.redmic.tasks.report.common.repository.ReportRepository;

@Service
public abstract class ReportService<TWrapper extends Wrapper> extends TaskBaseService
		implements IReportService<TWrapper> {

	@Autowired
	SelectionWorkRepository selectionWorkRepository;

	@Value("${property.path.temp.report}")
	private String PATH_BASE;

	@Value("${property.REPORT_TASK_NAME}")
	private String TASK_NAME;

	private String URL_BASE = "/api/temp/report/";

	@Autowired
	UserTasksService userTasksService;

	protected ObjectMapper objectMapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	private String designInfo;
	private String designList;

	ReportRepository repository;

	@Autowired
	public ReportService(ReportRepository repository) {
		this.repository = repository;
	}

	public void report(String service, MessageWrapper payload) {

		logger.info("Ejecutando report de " + service);

		Map<String, Object> msg = MessageWrapperUtils.getMessageFromMessageWrapper(payload);

		ReportDTO reportDTO = objectMapper.convertValue(msg, ReportDTO.class);
		reportDTO.setUserId(Long.parseLong(payload.getUserId()));
		reportDTO.setTaskLabel(service);

		RegisteredTaskDTO initResponse = initializedReport(reportDTO);

		reportDTO.setId(initResponse.getId());

		publishTaskStatusToUser(initResponse);

		StartedTaskDTO runResponse = sendToRunReport(reportDTO);

		publishTaskStatusToUser(runResponse);

		CompletedTaskDTO reportResponse = getReportResult(reportDTO);

		if (reportResponse != null)
			publishTaskStatusToUser(reportResponse);
	}

	public RegisteredTaskDTO initializedReport(ReportDTO dto) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("selectId", dto.getSelectId());
		params.put("titleDefinedByUser", dto.getTitleDefinedByUser());

		return repository.register(dto.getUserId(), dto.getTaskLabel(), params);
	}

	public StartedTaskDTO sendToRunReport(ReportDTO dto) {

		StartedTaskDTO taskStarted = repository.run(dto.getId());

		repository.setInProgress(dto.getId());

		return taskStarted;
	}

	public CompletedTaskDTO getReportResult(ReportDTO dto) {

		String fileName = null;
		try {
			fileName = reportSelection(dto);

		} catch (Exception e) {
			logger.error("Tarea de report fallida " + e.getMessage());
			setFailed(new GenerateReportException(dto.getTaskLabel(), dto.getSelectId(), dto.getId()));
			return null;
		}

		logger.info("Tarea de report completada");
		return repository.finish(dto.getId(), URL_BASE + fileName);
	}

	public String reportSelection(ReportDTO dto) throws Exception {

		String format = "pdf";

		// A�adir par�metros deseados
		Map<String, String> params = new HashMap<String, String>();
		if (dto.getTitleDefinedByUser() != null)
			params.put("TitleDefinedByUser", dto.getTitleDefinedByUser());

		ArrayList<String> ids = new ArrayList<String>();

		// Obtener seleccionados
		if (dto.isSelectionId()) {
			String selectionId = dto.getSelectId();
			Selection selectionCurrent = (Selection) selectionWorkRepository.findById(selectionId).get_source();
			SelectionWorkDTO selectionWork = objectMapper.convertValue(selectionCurrent, SelectionWorkDTO.class);

			ids.addAll(selectionWork.getIds());
		} else {
			ids.add(dto.getSelectId());
		}
		ArrayList<TWrapper> data = getListWrapper(ids);
		String design;
		if (data.size() > 1) {
			design = designList;
		} else {
			design = designInfo;
		}

		logger.info("Generando report de " + dto.getTaskLabel() + " para el/los id/ids " + ids);

		String fileName = System.currentTimeMillis() + "." + format;

		FileUtils.createDirectoryIfNotExist(PATH_BASE);
		FileOutputStream output = new FileOutputStream(new File(PATH_BASE + "/" + fileName));

		ReportUtil report = new ReportUtil();
		report.runReport(design, data, output, format.toLowerCase(), params);

		output.flush();
		output.close();

		logger.info("Report generado con éxito");

		return fileName;
	}

	public String getDesignInfo() {
		return designInfo;
	}

	public void setDesignInfo(String designInfo) {
		this.designInfo = designInfo;
	}

	public String getDesignList() {
		return designList;
	}

	public void setDesignList(String designList) {
		this.designList = designList;
	}

	public void setFailed(GenerateReportException e) {

		logger.info("Guardando tarea como fallida " + e.getTaskId());

		publishTaskStatusToUser(repository.setFailed(e));
	}
}