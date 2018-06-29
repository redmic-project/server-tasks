package es.redmic.tasks.ingest.model.step.intervention;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import es.redmic.tasks.ingest.model.intervention.common.InterventionBase;
import es.redmic.tasks.ingest.model.intervention.common.InterventionType;

@JsonSubTypes({ 
	@Type(value = ResponseMatchingIntervention.class, name = InterventionType.Constants.MATCHING)
 })
public abstract class ResponseInterventionBase extends InterventionBase {


}
