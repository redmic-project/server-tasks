package es.redmic.tasks.worms.jobs;

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
