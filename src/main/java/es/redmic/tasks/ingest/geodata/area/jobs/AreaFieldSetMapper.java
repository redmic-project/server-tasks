package es.redmic.tasks.ingest.geodata.area.jobs;

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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

import es.redmic.exception.tasks.ingest.IngestMatchingException;
import es.redmic.exception.tasks.ingest.IngestMatchingGeometryException;
import es.redmic.models.es.geojson.area.dto.AreaDTO;
import es.redmic.models.es.geojson.area.dto.AreaPropertiesDTO;
import es.redmic.tasks.ingest.geodata.common.jobs.shapefile.ShapeFileFieldSetMapper;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemAreaTypeDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemCodeNullableDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemCommonDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemNameDTO;
import es.redmic.tasks.ingest.model.matching.common.dto.ItemRemarkDTO;

public class AreaFieldSetMapper implements ShapeFileFieldSetMapper<List<AreaDTO>> {

	private AreaParameters jobParameters;

	public AreaFieldSetMapper(AreaParameters jobParameters) {
		this.jobParameters = jobParameters;
	}

	@Override
	public List<AreaDTO> mapFieldSet(SimpleFeature fieldSet) {

		List<AreaDTO> areaDTOList = new ArrayList<AreaDTO>();

		AreaDTO commonDTO = getCommonDTO(fieldSet, jobParameters.getMatching().getItemsCommon());

		commonDTO.setGeometry(getGeometryFromFieldSet(fieldSet.getDefaultGeometry()));

		ItemNameDTO itemName = jobParameters.getMatching().getName();

		if (itemName != null)
			commonDTO.getProperties().setName(itemName.returnValue(fieldSet));

		ItemCodeNullableDTO itemCode = jobParameters.getMatching().getCode();

		if (itemCode != null)
			commonDTO.getProperties().setCode(itemCode.returnValue(fieldSet));

		ItemRemarkDTO itemRemark = jobParameters.getMatching().getRemark();

		if (itemRemark != null)
			commonDTO.getProperties().setRemark(itemRemark.returnValue(fieldSet));

		ItemAreaTypeDTO itemAreaType = jobParameters.getMatching().getAreaType();
		if (itemAreaType != null)
			commonDTO.getProperties().setAreaType(itemAreaType.returnValue(fieldSet));

		areaDTOList.add(commonDTO);

		return areaDTOList;
	}

	private MultiPolygon getGeometryFromFieldSet(Object objectGeometry) {

		if (objectGeometry == null)
			throw new IngestMatchingGeometryException("null", "Multipolygon | Polygon", jobParameters.getTaskId());

		if (objectGeometry instanceof MultiPolygon) {
			return (MultiPolygon) objectGeometry;
		} else if (objectGeometry instanceof Polygon) {
			GeometryFactory geomFactory = new GeometryFactory(new PrecisionModel(), 4326);
			List<Polygon> list = new ArrayList<Polygon>();
			list.add((Polygon) objectGeometry);
			Polygon[] polygonArray = GeometryFactory.toPolygonArray(list);
			return geomFactory.createMultiPolygon(polygonArray);
		} else {
			throw new IngestMatchingGeometryException(((Geometry) objectGeometry).getGeometryType(),
					"Multipolygon | Polygon", jobParameters.getTaskId());
		}
	}

	private AreaDTO getCommonDTO(SimpleFeature fieldSet, List<ItemCommonDTO> commonProps) {

		AreaDTO commonDTO = new AreaDTO();

		AreaPropertiesDTO properties = new AreaPropertiesDTO();

		properties.setActivityId(jobParameters.getActivityId());

		try {
			// @formatter:off
			for (ItemCommonDTO match : commonProps) {
		    	Object value = match.returnValue(fieldSet);
		    	if (value instanceof String) {
		    		String str = (String) value;
		    		if (str.length() == 0)
		    			value = null;
		    	}
				PropertyUtils.setProperty(properties, match.getField(), value);
				// @formatter:on
			}
		} catch (Exception e) {
			throw new IngestMatchingException(jobParameters.getTaskId(), e);
		}
		commonDTO.setProperties(properties);
		return commonDTO;
	}
}
