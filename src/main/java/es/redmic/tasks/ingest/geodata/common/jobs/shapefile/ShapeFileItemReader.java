package es.redmic.tasks.ingest.geodata.common.jobs.shapefile;

/*-
 * #%L
 * Tasks
 * %%
 * Copyright (C) 2019 REDMIC Project / Server
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
