package es.redmic.tasks.ingest.geodata.tracking.jobs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import es.redmic.models.es.geojson.tracking.common.ElementTrackingDTO;
import es.redmic.tasks.ingest.geodata.common.jobs.GeoDataFieldSetMapper;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemDeviceDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemPointGeometryDTO;

public class TrackingFieldSetMapper extends GeoDataFieldSetMapper<ElementTrackingDTO> implements FieldSetMapper<List<ElementTrackingDTO>> {

	private TrackingParameters jobParameters;

	public TrackingFieldSetMapper(TrackingParameters jobParameters) {
		super(jobParameters);
		this.jobParameters = jobParameters;
	}

	public List<ElementTrackingDTO> mapFieldSet(FieldSet fieldSet) {

		List<ElementTrackingDTO> elementTrackingDTOList = new ArrayList<ElementTrackingDTO>();

		ElementTrackingDTO commonDTO = getCommonDTO(fieldSet, jobParameters.getMatching().getItemsCommon());

		ItemPointGeometryDTO itemGeometry =  jobParameters.getMatching().getPointGeometry();
		if (itemGeometry != null)
			commonDTO.setGeometry(itemGeometry.returnValue(fieldSet));
		
		commonDTO.getProperties().setElement(jobParameters.getPlatformDTO() != null ? jobParameters.getPlatformDTO() : jobParameters.getAnimalDTO());
		
		ItemDeviceDTO itemDevice = jobParameters.getMatching().getDevice();
		if (itemDevice != null)
			commonDTO.getProperties().setDevice(itemDevice.returnValue(fieldSet));
		
		elementTrackingDTOList.add(commonDTO);
		
		return elementTrackingDTOList;
	}
}