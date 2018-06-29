package es.redmic.tasks.ingest.geodata.common.jobs.shapefile;

import org.opengis.feature.simple.SimpleFeature;
import org.springframework.util.Assert;

public class ShapeFileLineMapper<T> {

	private ShapeFileFieldSetMapper<T> fieldSetMapper;

	public void setFieldSetMapper(ShapeFileFieldSetMapper<T> fieldSetMapper) {
		this.fieldSetMapper = fieldSetMapper;
	}

	public T mapLine(SimpleFeature line) throws Exception {

		return (T) this.fieldSetMapper.mapFieldSet(line);
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(fieldSetMapper, "ShapeFileFieldSetMapper is required");
	}
}
