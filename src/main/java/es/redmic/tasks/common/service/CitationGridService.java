package es.redmic.tasks.common.service;

import org.locationtech.jts.geom.Geometry;

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

import org.springframework.stereotype.Service;

import es.redmic.es.geodata.common.service.GridServiceItfc;
import es.redmic.es.tools.distributions.species.repository.RWTaxonDistributionRepository;
import es.redmic.exception.common.ExceptionType;
import es.redmic.exception.common.InternalException;
import es.redmic.models.es.tools.distribution.model.Distribution;

@Service
public class CitationGridService implements GridServiceItfc {

	public Distribution getDistribution(Geometry geometry, RWTaxonDistributionRepository repo, int radius) {

		throw new InternalException(ExceptionType.INTERNAL_EXCEPTION);
	}
}
