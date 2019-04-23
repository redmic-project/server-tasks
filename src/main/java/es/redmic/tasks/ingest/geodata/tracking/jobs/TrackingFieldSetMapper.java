package es.redmic.tasks.ingest.geodata.tracking.jobs;

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
