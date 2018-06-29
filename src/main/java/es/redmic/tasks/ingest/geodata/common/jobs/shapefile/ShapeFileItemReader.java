package es.redmic.tasks.ingest.geodata.common.jobs.shapefile;

import org.opengis.feature.simple.SimpleFeature;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.util.Assert;

import es.redmic.utils.geo.reader.ShapeFileReader;

public class ShapeFileItemReader<T> implements ItemReader<T> {

	private ShapeFileReader reader;

	private ShapeFileLineMapper<T> lineMapper;

	private String sourceDirectory;

	public void setLineMapper(ShapeFileLineMapper<T> lineMapper) {
		this.lineMapper = lineMapper;
	}

	public void setSourceDirectory(String sourceDirectory) {
		this.sourceDirectory = sourceDirectory;
	}

	public ShapeFileItemReader() {
	}

	@Override
	public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

		SimpleFeature item = reader.getNext();

		if (item != null) {

			return lineMapper.mapLine(item);
		}
		return null;
	}

	public void afterPropertiesSet() throws Exception {

		Assert.notNull(lineMapper, "LineMapper is required");
		Assert.notNull(sourceDirectory, "Source directory is required");

		reader = new ShapeFileReader(sourceDirectory);
	}
}
