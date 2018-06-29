package es.redmic.tasks.common.service;

import org.springframework.stereotype.Service;

import com.vividsolutions.jts.geom.Geometry;

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
