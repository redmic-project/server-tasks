package es.redmic.tasks.ingest.model.matching.tracking.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaNotNull;

import es.redmic.tasks.ingest.model.matching.common.dto.ItemCommonDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemDeviceDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemPointGeometryDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.MatchingBaseDTO;

@JsonSchemaNotNull
public class TrackingMatching extends MatchingBaseDTO {
	
	@NotNull
	@Valid
	private ItemPointGeometryDTO pointGeometry;
	
	@NotNull
	@Valid
	private ItemDeviceDTO device;
	
	public TrackingMatching() {
		super();
	}

	public ItemPointGeometryDTO getPointGeometry() {
		return pointGeometry;
	}

	public void setPointGeometry(ItemPointGeometryDTO pointGeometry) {
		this.pointGeometry = pointGeometry;
		matchingColumns.addAll(pointGeometry.getColumns());
	}

	public ItemDeviceDTO getDevice() {
		return device;
	}

	public void setDevice(ItemDeviceDTO device) {
		this.device = device;
	}
	
	@SuppressWarnings("serial")
	@JsonIgnore
	public List<ItemCommonDTO> getItemsCommon() {
		
		return new ArrayList<ItemCommonDTO>() {{
			add(getDate());
			if (getQFlag() != null) add(getQFlag());
			if (getVFlag() != null) add(getVFlag());
		}};
	}
}