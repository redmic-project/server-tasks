package es.redmic.tasks.ingest.model.intervention.common;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import es.redmic.tasks.ingest.model.intervention.matching.InterventionMatching;

@JsonSubTypes({ 
	@Type(value = InterventionMatching.class, name = InterventionType.Constants.MATCHING)
 })
public abstract class InterventionDescriptionBase extends InterventionBase {

}
