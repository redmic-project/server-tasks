package es.redmic.tasks.ingest.model.matching.common.dto;

import org.opengis.feature.simple.SimpleFeature;
import org.springframework.batch.item.file.transform.FieldSet;

public abstract class ItemBaseDTO {

	public abstract Object returnValue(FieldSet fs);

	public abstract Object returnValue(SimpleFeature fs);
}
