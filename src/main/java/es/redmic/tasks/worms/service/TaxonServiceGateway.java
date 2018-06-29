package es.redmic.tasks.worms.service;

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
