package es.redmic.tasks.ingest.model.matching.common.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.batch.item.file.transform.FieldSet;

import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaIgnore;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaNotNull;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaUniqueItemsByRequiredProperties;

@JsonSchemaNotNull
public class ParameterDTO extends ItemBaseDTO implements ItemDTOItf {

	private static final String FIELD = "value";

	public ParameterDTO() {
	}

	@NotNull
	Long dataDefinitionId;

	@JsonSchemaUniqueItemsByRequiredProperties
	protected List<String> columns;

	public Long getDataDefinitionId() {
		return dataDefinitionId;
	}

	public void setDataDefinitionId(Long dataDefinitionId) {
		this.dataDefinitionId = dataDefinitionId;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	@JsonSchemaIgnore
	@JsonIgnore
	@Override
	public String getField() {
		return FIELD;
	}

	@Override
	public Double returnValue(FieldSet fs) {

		if (columns == null || columns.size() < 1)
			return null;
		return fs.readDouble(columns.get(0));
	}

	@Override
	public Double returnValue(SimpleFeature fs) {

		if (columns == null || columns.size() < 1)
			return null;

		return (Double) fs.getAttribute(columns.get(0));
	}
}