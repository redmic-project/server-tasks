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

import java.util.Collections;

import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.data.domain.Sort.Direction;

import es.redmic.db.administrative.taxonomy.model.Taxon;
import es.redmic.db.administrative.taxonomy.repository.TaxonRepository;

public class WormsToRedmicItemReader extends RepositoryItemReader<Taxon> {

	public WormsToRedmicItemReader(TaxonRepository repository, Integer pageSize) {
		this.setRepository(repository);
		this.setPageSize(pageSize);
		this.setMethodName("findAll");
		this.setSort(Collections.singletonMap("id", Direction.ASC));
	}
}
