package es.redmic.tasks.ingest.model.matching.common.dto;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.opengis.feature.simple.SimpleFeature;
import org.springframework.batch.item.file.transform.FieldSet;

import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaNotNull;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaUniqueItemsByRequiredProperties;

@JsonSchemaNotNull
public class ItemClassificationDTO extends ItemBaseWithColumnsDTO {

	@NotNull
	@Size(min = 1)
	@JsonSchemaUniqueItemsByRequiredProperties
	List<String> classifications;

	public List<String> getClassifications() {
		return classifications;
	}

	public void setClassifications(List<String> classifications) {
		this.classifications = classifications;
	}

	@Override
	public List<String> returnValue(FieldSet fs) {

		return getClassifications();
	}

	@Override
	public List<String> returnValue(SimpleFeature fs) {

		return getClassifications();
	}
}
