package es.redmic.tasks.ingest.common.repository;

import java.util.Map;

import es.redmic.exception.tasks.ingest.IngestFileException;
import es.redmic.tasks.common.repository.TaskBaseWithInterventionRepository;
import es.redmic.tasks.common.utils.TasksTypes;
import es.redmic.tasks.ingest.model.intervention.matching.InterventionMatching;
import es.redmic.tasks.ingest.model.matching.common.dto.MatchingCommonDTO;
import es.redmic.utils.compressor.Zip;
import es.redmic.utils.geo.reader.ShapeFileReader;

public abstract class IngestShapeFileRepository<TDTO extends MatchingCommonDTO> extends TaskBaseWithInterventionRepository<TDTO> {

	@Override
	protected void addInitialData(InterventionMatching interventionMatching, String taskId, String directoryPath,
			Map<String, Object> fileProperties) {

		String fileName = (String) fileProperties.get("fileName");

		if (directoryPath == null || fileName == null)
			throw new IngestFileException(taskId);

		// @formatter:off
		
		String sourceDirectory = directoryPath + "/" + taskId + "/",
				zipFilePath = directoryPath + "/" + fileName;

		// @formatter:on

		Zip zip = new Zip();

		zip.extract(zipFilePath, sourceDirectory);

		ShapeFileReader reader = new ShapeFileReader(sourceDirectory);

		// TODO: getData llega a null
		interventionMatching.setData(reader.getSample(SAMPLE_SIZE));

		interventionMatching.setHeader(reader.getHeader());
	}

	@Override
	protected String getTaskType() {
		return TasksTypes.INGEST;
	}
}