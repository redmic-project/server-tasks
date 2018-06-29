package es.redmic.tasks.ingest.model.matching.common.dto;

import java.util.List;

import javax.validation.constraints.Size;

import org.opengis.feature.simple.SimpleFeature;
import org.springframework.batch.item.file.transform.FieldSet;

public abstract class ItemCharBaseDTO extends ItemCommonDTO {

	public ItemCharBaseDTO() {
	}

	@Override
	@Size(min = 0, max = 1)
	public List<String> getColumns() {
		return columns;
	}

	@Override
	public Character returnValue(FieldSet fs) {

		if (columns == null || columns.size() < 1)
			return null;

		return fs.readChar(columns.get(0));
	}

	@Override
	public Character returnValue(SimpleFeature fs) {

		if (columns == null || columns.size() < 1)
			return null;

		return (Character) fs.getAttribute(columns.get(0));
	}
}