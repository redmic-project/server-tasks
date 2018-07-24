package es.redmic.tasks.ingest.model.matching.common.dto;

import org.opengis.feature.simple.SimpleFeature;
import org.springframework.batch.item.file.transform.FieldSet;

public abstract class ItemStringDTO extends ItemCommonDTO {

	public ItemStringDTO() {
	}

	@Override
	public String returnValue(FieldSet fs) {

		if (columns == null || columns.size() < 1)
			return null;

		return fs.readString(columns.get(0).toString()).trim();
	}

	@Override
	public String returnValue(SimpleFeature fs) {

		if (columns == null || columns.size() < 1)
			return null;

		return ((String) fs.getAttribute(columns.get(0).toString())).trim();
	}
}