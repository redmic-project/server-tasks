package es.redmic.tasks.ingest.model.matching.common.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaIgnore;

public abstract class MatchingCommonDTO {

	@JsonIgnore
	@JsonSchemaIgnore
	protected Set<String> matchingColumns;

	public MatchingCommonDTO() {
		this.matchingColumns = new HashSet<String>();
	}

	@JsonIgnore
	public Set<String> getMatchingColumns() {
		return matchingColumns;
	}

	@JsonIgnore
	public List<ItemCommonDTO> getItemsCommon() {
		return new ArrayList<>();
	}
}
