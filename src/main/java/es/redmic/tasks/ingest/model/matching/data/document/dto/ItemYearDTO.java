package es.redmic.tasks.ingest.model.matching.data.document.dto;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.batch.item.file.transform.FieldSet;

import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaIgnore;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaNotNull;

import es.redmic.tasks.ingest.model.matching.common.dto.ItemCommonDTO;

@JsonSchemaNotNull
public class ItemYearDTO extends ItemCommonDTO {

	private static final String FIELD = "year";

	public ItemYearDTO() {
	}

	@JsonSchemaIgnore
	@JsonIgnore
	@Override
	public String getField() {
		return FIELD;
	}

	@Override
	public Integer returnValue(FieldSet fs) {

		return fs.readInt(columns.get(0));
	}

	@Override
	public Integer returnValue(SimpleFeature fs) {

		return (Integer) fs.getAttribute(columns.get(0));
	}
}