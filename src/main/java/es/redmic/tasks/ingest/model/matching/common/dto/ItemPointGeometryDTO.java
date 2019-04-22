package es.redmic.tasks.ingest.model.matching.common.dto;

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

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.batch.item.file.transform.FieldSet;

import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaIgnore;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaNotNull;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaUniqueItemsByRequiredProperties;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

import es.redmic.models.es.geojson.common.dto.CrsDTO;

@JsonSchemaNotNull
public class ItemPointGeometryDTO extends ItemBaseDTO implements ItemDTOItf {

	private static final String FIELD = "geometry";

	private static Integer SRID = 4326;

	@NotNull
	@JsonSchemaUniqueItemsByRequiredProperties
	@Size(min = 2, max = 2)
	protected List<String> columns;

	@JsonSchemaIgnore
	@Valid
	private CrsDTO crs;

	public ItemPointGeometryDTO() {
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public CrsDTO getCrs() {
		return crs;
	}

	public void setCrs(CrsDTO crs) {
		this.crs = crs;
	}

	@JsonSchemaIgnore
	@JsonIgnore
	@Override
	public String getField() {
		return FIELD;
	}

	@Override
	public Point returnValue(FieldSet fs) {

		// TODO: si crs != null obtener el SRID, en otro caso, usar el de por
		// defecto

		GeometryFactory geomFactory = new GeometryFactory(new PrecisionModel(), SRID);
		return geomFactory.createPoint(new Coordinate(fs.readDouble(columns.get(0)), fs.readDouble(columns.get(1))));
	}

	@Override
	public Point returnValue(SimpleFeature fs) {
		return (Point) fs.getDefaultGeometry();
	}
}
