package es.redmic.tasks.ingest.geodata.common.jobs.shapefile;

import org.opengis.feature.simple.SimpleFeature;

public interface ShapeFileFieldSetMapper<T> {

	public abstract T mapFieldSet(SimpleFeature fieldSet);
}