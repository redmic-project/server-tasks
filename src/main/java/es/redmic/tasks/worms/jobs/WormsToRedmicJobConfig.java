package es.redmic.tasks.worms.jobs;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import es.redmic.db.administrative.taxonomy.model.Taxon;
import es.redmic.db.administrative.taxonomy.repository.TaxonRepository;
import es.redmic.db.administrative.taxonomy.service.TaxonService;
import es.redmic.models.es.administrative.taxonomy.dto.TaxonDTO;
import es.redmic.tasks.common.utils.JobUtils;
import es.redmic.tasks.worms.listener.WormsToRedmicListener;
import es.redmic.tasks.worms.listener.WormsToRedmicStepListener;

@Configuration
public class WormsToRedmicJobConfig {

	@Autowired
	protected JobBuilderFactory jobBuilderFactory;

	@Autowired
	protected StepBuilderFactory stepBuilderFactory;

	@Autowired
	protected WormsToRedmicListener wormsToRedmicListener;

	@Autowired
	protected WormsToRedmicStepListener wormsToRedmicStepListener;

	@Autowired
	@Qualifier("jpaTransactionManager")
	PlatformTransactionManager jpaTransactionManager;

	@Autowired
	TaxonRepository repository;

	@Autowired
	TaxonService service;

	@Value("${property.WORMS_TO_REDMIC_TASK_NAME}")
	private String TASK_NAME;

	private static final String JOB_NAME = "wormsToRedmic";

	// @formatter:off
	
	private static final Integer COMMIT_INTERVAL = 1,
			PAGE_SIZE = 10;

	// @formatter:on

	public WormsToRedmicJobConfig() {

	}

	@PostConstruct
	@Bean(name = "wormsToRedmicJob")
	public Job wormsToRedmicJob() {

		// @formatter:off

		return jobBuilderFactory.get(TASK_NAME)
				.listener(wormsToRedmicListener)
				.start(wormsToRedmicStep()).build();
		
		// @formatter:on
	}

	@Bean(name = "wormsToRedmicStep")
	public Step wormsToRedmicStep() {

		// @formatter:off
		
		return stepBuilderFactory.get("indexing-" + JOB_NAME + "-step")
				.transactionManager(jpaTransactionManager)
				.listener(wormsToRedmicStepListener)
					.<Taxon, TaxonDTO>chunk(COMMIT_INTERVAL)
				.reader(taxonItemReader())
					.processor(wormsToRedmicProcessor())
						.writer(wormsToRedmicItemWriter(null))
							.faultTolerant()
								.build();
		
		// @formatter:on
	}

	@Bean
	@StepScope
	public WormsToRedmicItemReader taxonItemReader() {

		return new WormsToRedmicItemReader(repository, PAGE_SIZE);
	}

	@Bean
	public WormsToRedmicProcessor wormsToRedmicProcessor() {

		return new WormsToRedmicProcessor();
	}

	@Bean
	@StepScope
	@Transactional
	public ItemWriter<? super TaxonDTO> wormsToRedmicItemWriter(
			@Value("#{jobParameters['parameters']}") JobParameter parameter) {

		WormsToRedmicParameters parameters = JobUtils.JobParameters2UserParameters(parameter,
				WormsToRedmicParameters.class);

		return new WormsToRedmicItemWritter(service, parameters.getTaskId());
	}
}
