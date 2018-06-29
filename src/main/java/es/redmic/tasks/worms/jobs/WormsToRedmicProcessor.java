package es.redmic.tasks.worms.jobs;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import es.redmic.db.administrative.taxonomy.model.Taxon;
import es.redmic.db.config.OrikaScanBeanDBItfc;
import es.redmic.es.administrative.taxonomy.service.WormsToRedmicService;
import es.redmic.models.es.administrative.taxonomy.dto.TaxonDTO;

public class WormsToRedmicProcessor implements ItemProcessor<Taxon, TaxonDTO> {

	@Autowired
	WormsToRedmicService wormsToRedmicService;

	@Autowired
	private OrikaScanBeanDBItfc orikaMapper;

	@Override
	public TaxonDTO process(Taxon inBean) throws Exception {

		TaxonDTO dto = orikaMapper.getMapperFacade().map(inBean, TaxonDTO.class);

		return wormsToRedmicService.processUpdate(dto);
	}
}
