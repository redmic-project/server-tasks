package es.redmic.tasks.ingest.model.matching.common.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaIgnore;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaNotNull;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaUniqueItemsByRequiredProperties;

@JsonSchemaNotNull
public class ClassificationDTO implements ItemDTOItf {

	private static final String FIELD = "object";
	
	@NotNull
	@JsonSchemaUniqueItemsByRequiredProperties
	@Size(min=1)
	private List<ItemClassificationDTO> matching = new ArrayList<ItemClassificationDTO>();
	
	@JsonSchemaIgnore
	@JsonIgnore
	@Override
	public String getField() {
		return FIELD;
	}

	public List<ItemClassificationDTO> getMatching() {
		return matching;
	}

	public void setMatching(List<ItemClassificationDTO> matching) {
		this.matching = matching;
	}
}