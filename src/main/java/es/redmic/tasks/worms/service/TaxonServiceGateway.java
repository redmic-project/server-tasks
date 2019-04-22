package es.redmic.tasks.worms.service;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.redmic.db.administrative.taxonomy.service.SpeciesService;
import es.redmic.db.administrative.taxonomy.service.TaxonService;
import es.redmic.db.config.OrikaScanBeanDBItfc;
import es.redmic.es.administrative.taxonomy.service.TaxonServiceGatewayItfc;
import es.redmic.models.es.administrative.taxonomy.dto.SpeciesDTO;
import es.redmic.models.es.administrative.taxonomy.dto.TaxonDTO;

@Service
public class TaxonServiceGateway implements TaxonServiceGatewayItfc {

	@Autowired
	TaxonService taxonService;

	@Autowired
	SpeciesService speciesService;

	@Autowired
	protected OrikaScanBeanDBItfc factory;

	/*
	 * Método que sirve de pasarela para poder guardar desde la librería de
	 * elasticsearch un taxón en base de datos.
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T save(TaxonDTO taxon) {

		if (taxon.getRank().getId() < 10L)
			return (T) taxonService.save(taxon);
		else
			return (T) speciesService.save(factory.getMapperFacade().map(taxon, SpeciesDTO.class));
	}

	/*
	 * Método que sirve de pasarela para poder modificar desde la librería de
	 * elasticsearch un taxón en base de datos.
	 * 
	 */

	@SuppressWarnings("unchecked")
	@Override
	public <T> T update(TaxonDTO taxon) {

		if (taxon.getRank().getId() < 10L)
			return (T) taxonService.update(taxon);
		else
			return (T) speciesService.update(factory.getMapperFacade().map(taxon, SpeciesDTO.class));
	}
}
