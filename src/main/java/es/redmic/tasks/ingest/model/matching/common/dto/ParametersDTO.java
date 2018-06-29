package es.redmic.tasks.ingest.model.matching.common.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaIgnore;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaNotNull;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaUniqueItemsByRequiredProperties;

@JsonSchemaNotNull
public class ParametersDTO implements ItemDTOItf {

	private static final String FIELD = "value";
	
	@Valid
	@NotNull
	@JsonSchemaUniqueItemsByRequiredProperties
	@Size(min=1)
	private List<ItemParameterDTO> matching = new ArrayList<ItemParameterDTO>();
	
	@JsonSchemaIgnore
	@JsonIgnore
	@Override
	public String getField() {
		return FIELD;
	}

	public List<ItemParameterDTO> getMatching() {
		return matching;
	}

	public void setMatching(List<ItemParameterDTO> matching) {
		this.matching = matching;
	}
}