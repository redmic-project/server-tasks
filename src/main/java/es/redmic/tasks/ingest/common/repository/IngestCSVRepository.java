package es.redmic.tasks.ingest.common.repository;

import java.io.File;
import java.util.Map;

import es.redmic.exception.tasks.ingest.IngestFileException;
import es.redmic.tasks.common.repository.TaskBaseWithInterventionRepository;
import es.redmic.tasks.common.utils.TasksTypes;
import es.redmic.tasks.ingest.model.intervention.matching.InterventionMatching;
import es.redmic.tasks.ingest.model.matching.common.dto.MatchingCommonDTO;
import es.redmic.utils.csv.DataLoaderIngestData;

public abstract class IngestCSVRepository<TDTO extends MatchingCommonDTO> extends TaskBaseWithInterventionRepository<TDTO> {

	@Override
	protected void addInitialData(InterventionMatching interventionMatching, String taskId, String directoryPath,
			Map<String, Object> fileProperties) {

		String fileName = (String) fileProperties.get("fileName"), delimiter = (String) fileProperties.get("delimiter");

		if (directoryPath == null || fileName == null)
			throw new IngestFileException(taskId);

		File file = mediaStorage.openTempFile(directoryPath, fileName);

		if (file == null)
			throw new IngestFileException(taskId);

		DataLoaderIngestData dataLoader = new DataLoaderIngestData(file, delimiter);

		interventionMatching.setData(dataLoader.getSample(SAMPLE_SIZE));
		interventionMatching.setHeader(dataLoader.getHeader());
	}

	@Override
	protected String getTaskType() {
		return TasksTypes.INGEST;
	}
}