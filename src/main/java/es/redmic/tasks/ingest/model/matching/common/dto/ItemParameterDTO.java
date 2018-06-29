package es.redmic.tasks.ingest.model.matching.common.dto;

import javax.validation.constraints.NotNull;

import org.opengis.feature.simple.SimpleFeature;
import org.springframework.batch.item.file.transform.FieldSet;

import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaNotNull;

@JsonSchemaNotNull
public class ItemParameterDTO extends ItemBaseWithColumnsDTO {

	@NotNull
	Long dataDefinitionId;

	public Long getDataDefinitionId() {
		return dataDefinitionId;
	}

	public void setDataDefinitionId(Long dataDefinitionId) {
		this.dataDefinitionId = dataDefinitionId;
	}

	@Override
	public Double returnValue(FieldSet fs) {

		return fs.readDouble(columns.get(0));
	}

	@Override
	public Double returnValue(SimpleFeature fs) {

		return (Double) fs.getAttribute(columns.get(0));
	}
}
