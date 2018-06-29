package es.redmic.tasks.ingest.geodata.tracking.jobs;

import java.io.File;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import es.redmic.db.geodata.tracking.animal.service.AnimalTrackingService;
import es.redmic.db.geodata.tracking.platform.service.PlatformTrackingService;
import es.redmic.es.config.OrikaScanBeanESItfc;
import es.redmic.mediastorage.service.MediaStorageService;
import es.redmic.models.es.geojson.tracking.common.ElementTrackingDTO;
import es.redmic.tasks.common.utils.JobUtils;
import es.redmic.tasks.ingest.common.jobs.CsvToDbJobConfig;
import es.redmic.tasks.ingest.geodata.tracking.listener.IngestDataTrackingListener;
import es.redmic.tasks.ingest.geodata.tracking.listener.IngestDataTrackingStepListener;

@Configuration
public class TrackingCsvToDbJobConfig extends CsvToDbJobConfig {

	@Value("${property.path.media_storage.temp.INGEST_DATA}")
	private String PATH_TRACKING_TEMP;
	
	@Value("${property.INGEST_DATA_TRACKING_TASK_NAME}")
	private String TASK_NAME;

	@Autowired
	protected JobBuilderFactory jobBuilderFactory;

	@Autowired
	protected StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private IngestDataTrackingListener ingestDataTrackingListener;
	
	@Autowired
	private IngestDataTrackingStepListener ingestDataTrackingStepListener;
	
	@Autowired
	MediaStorageService mediaStorage;

	@Autowired
	AnimalTrackingService animalTrackingService;
	
	@Autowired
	PlatformTrackingService platformTrackingService;
	
	@Autowired
	@Qualifier("jpaTransactionManager")
	PlatformTransactionManager jpaTransactionManager;
	
	@Autowired
	protected OrikaScanBeanESItfc orikaMapper;
	
	@PostConstruct
	@Bean(name= "createTrackingIndexing")
	public Job createTrackingIndexing() throws Exception {
		// @formatter:off
		return jobBuilderFactory.get(TASK_NAME)
					.listener(ingestDataTrackingListener)
						.start(createTrackingStepIndexing())
							.build();
		// @formatter:on
	}

	@Bean(name="createTrackingStepIndexing")
	public Step createTrackingStepIndexing() throws Exception {

		// @formatter:off
		return stepBuilderFactory.get("indexing-tracking-step")
				.transactionManager(jpaTransactionManager)
				.<List<ElementTrackingDTO>, List<ElementTrackingDTO>>chunk(10)
				.reader(trackingCsvFileItemReader(null))
					.processor(processor(null))
					.faultTolerant()
					.writer(saveTracking(null))
					.listener(ingestDataTrackingStepListener)
//							.listener(logProcessListener)
									.build();
		
		// @formatter:on
	}

	@Bean
	@StepScope
	public FlatFileItemReader<List<ElementTrackingDTO>> trackingCsvFileItemReader(@Value("#{jobParameters['parameters']}") JobParameter parameter) throws Exception {
		
		TrackingParameters parameters = JobUtils.JobParameters2UserParameters(parameter, TrackingParameters.class);

		FlatFileItemReader<List<ElementTrackingDTO>> itemReader = new FlatFileItemReader<List<ElementTrackingDTO>>(); 
		File file = mediaStorage.openTempFile(PATH_TRACKING_TEMP, parameters.getFileName());
		
		itemReader.setResource(new FileSystemResource(file));		
		itemReader.setLineMapper(trackingLineMapper(null));
		itemReader.setLinesToSkip(1);

		itemReader.afterPropertiesSet();

		return itemReader;
	}

	@Bean
	@StepScope
	public DefaultLineMapper<List<ElementTrackingDTO>> trackingLineMapper(@Value("#{jobParameters['parameters']}") JobParameter parameter) {
		
		TrackingParameters parameters = JobUtils.JobParameters2UserParameters(parameter, TrackingParameters.class);
		
		DefaultLineMapper<List<ElementTrackingDTO>> lineMapper = new DefaultLineMapper<List<ElementTrackingDTO>>();

		lineMapper.setLineTokenizer(createTokenizer(parameters.getHeader(), parameters.getDelimiter()));
		lineMapper.setFieldSetMapper(trackingFieldSetMapper(parameters));
		lineMapper.afterPropertiesSet();

		return lineMapper;
	}

	protected FieldSetMapper<List<ElementTrackingDTO>> trackingFieldSetMapper(TrackingParameters parameters) {
		
		return new TrackingFieldSetMapper(parameters);
	}
	
	@Bean
	@StepScope
	public ItemProcessor<List<ElementTrackingDTO>, List<ElementTrackingDTO>> processor(@Value("#{jobParameters['parameters']}") JobParameter parameter) {
		
		TrackingParameters parameters = JobUtils.JobParameters2UserParameters(parameter, TrackingParameters.class);
		return new TrackingItemProcessor(parameters.getTaskId());
	}
	
	@Bean
	@StepScope
	public ItemWriter<List<ElementTrackingDTO>> saveTracking(@Value("#{jobParameters['parameters']}") JobParameter parameter) {
		
		TrackingParameters parameters = JobUtils.JobParameters2UserParameters(parameter, TrackingParameters.class);
		return new TrackingItemWritter(animalTrackingService, platformTrackingService, orikaMapper, parameters.getTaskId());	
	}
}