package es.redmic.tasks.worms.jobs;

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
