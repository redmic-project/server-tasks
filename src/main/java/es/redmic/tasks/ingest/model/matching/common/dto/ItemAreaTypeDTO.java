package es.redmic.tasks.ingest.model.matching.common.dto;

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.batch.item.file.transform.FieldSet;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaIgnore;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaNotNull;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaUrl;

import es.redmic.models.es.common.deserializer.CustomRelationDeserializer;
import es.redmic.models.es.maintenance.areas.dto.AreaTypeDTO;

@JsonSchemaNotNull
public class ItemAreaTypeDTO extends ItemBaseDTO implements ItemDTOItf {

	private static final String FIELD = "areaType";

	public ItemAreaTypeDTO() {
	}

	@NotNull
	@JsonDeserialize(using = CustomRelationDeserializer.class)
	@JsonSchemaUrl(value = "areaTypeUrl")
	private AreaTypeDTO areaType;

	public AreaTypeDTO getAreaType() {
		return areaType;
	}

	public void setAreaType(AreaTypeDTO areaType) {
		this.areaType = areaType;
	}

	@JsonSchemaIgnore
	@JsonIgnore
	@Override
	public String getField() {
		return FIELD;
	}

	@Override
	public AreaTypeDTO returnValue(FieldSet fs) {

		return areaType;
	}

	@Override
	public AreaTypeDTO returnValue(SimpleFeature fs) {

		return areaType;
	}
}