package es.redmic.tasks.ingest.data.document.jobs;

import java.io.File;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
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

import es.redmic.db.administrative.service.DocumentService;
import es.redmic.es.config.OrikaScanBeanESItfc;
import es.redmic.es.maintenance.domain.administrative.service.DocumentTypeESService;
import es.redmic.mediastorage.service.MediaStorageService;
import es.redmic.models.es.administrative.dto.DocumentDTO;
import es.redmic.tasks.common.utils.JobUtils;
import es.redmic.tasks.ingest.common.jobs.CsvToDbJobConfig;
import es.redmic.tasks.ingest.data.document.listener.IngestDataDocumentListener;
import es.redmic.tasks.ingest.data.document.listener.IngestDataDocumentStepListener;

@Configuration
public class DocumentCsvToDbJobConfig extends CsvToDbJobConfig {

	@Value("${property.path.media_storage.temp.INGEST_DATA}")
	private String PATH_DATA_TEMP;

	@Value("${property.INGEST_DATA_DOCUMENT_TASK_NAME}")
	private String TASK_NAME;

	@Value("${property.URL_DOCUMENTS}")
	private String URL_DOCUMENTS;

	@Autowired
	protected JobBuilderFactory jobBuilderFactory;

	@Autowired
	protected StepBuilderFactory stepBuilderFactory;

	@Autowired
	private IngestDataDocumentListener ingestDataDocumentListener;

	@Autowired
	private IngestDataDocumentStepListener ingestDataDocumentStepListener;

	@Autowired
	MediaStorageService mediaStorage;

	@Autowired
	DocumentService documentService;

	@Autowired
	DocumentTypeESService documentTypeESService;

	@Autowired
	protected OrikaScanBeanESItfc orikaMapper;

	@Autowired
	@Qualifier("jpaTransactionManager")
	PlatformTransactionManager jpaTransactionManager;

	@PostConstruct
	@Bean(name = "createDocumentIndexing")
	public Job createDocumentIndexing() throws Exception {
		// @formatter:off
		return jobBuilderFactory.get(TASK_NAME)
					.listener(ingestDataDocumentListener)
						.start(createDocumentStepIndexing())
							.build();
		// @formatter:on
	}

	@Bean(name = "createDocumentStepIndexing")
	public Step createDocumentStepIndexing() throws Exception {

		// @formatter:off
		return stepBuilderFactory.get("indexing-document-step")
				.transactionManager(jpaTransactionManager)
				.listener(ingestDataDocumentStepListener)
				.<List<DocumentDTO>, List<DocumentDTO>>chunk(10)
				.reader(documentCsvFileItemReader(null))
					.writer(saveDocument(null))
//							.listener(logProcessListener)
								.faultTolerant()
									.build();
		
		// @formatter:on
	}

	@Bean
	@StepScope
	public FlatFileItemReader<List<DocumentDTO>> documentCsvFileItemReader(
			@Value("#{jobParameters['parameters']}") JobParameter parameter) throws Exception {

		DocumentParameters parameters = JobUtils.JobParameters2UserParameters(parameter, DocumentParameters.class);

		FlatFileItemReader<List<DocumentDTO>> itemReader = new FlatFileItemReader<List<DocumentDTO>>();
		File file = mediaStorage.openTempFile(PATH_DATA_TEMP, parameters.getFileName());

		itemReader.setResource(new FileSystemResource(file));
		itemReader.setLineMapper(documentLineMapper(null));
		itemReader.setLinesToSkip(1);

		itemReader.afterPropertiesSet();

		return itemReader;
	}

	@Bean
	@StepScope
	public DefaultLineMapper<List<DocumentDTO>> documentLineMapper(
			@Value("#{jobParameters['parameters']}") JobParameter parameter) {

		DocumentParameters parameters = JobUtils.JobParameters2UserParameters(parameter, DocumentParameters.class);

		DefaultLineMapper<List<DocumentDTO>> lineMapper = new DefaultLineMapper<List<DocumentDTO>>();

		lineMapper.setLineTokenizer(createTokenizer(parameters.getHeader(), parameters.getDelimiter()));
		lineMapper.setFieldSetMapper(documentFieldSetMapper(parameters));
		lineMapper.afterPropertiesSet();

		return lineMapper;
	}

	protected FieldSetMapper<List<DocumentDTO>> documentFieldSetMapper(DocumentParameters parameters) {

		return new DocumentFieldSetMapper(parameters, documentTypeESService, URL_DOCUMENTS);
	}

	@Bean
	@StepScope
	public ItemWriter<List<DocumentDTO>> saveDocument(@Value("#{jobParameters['parameters']}") JobParameter parameter) {

		DocumentParameters parameters = JobUtils.JobParameters2UserParameters(parameter, DocumentParameters.class);

		return new DocumentItemWritter(orikaMapper, documentService, parameters.getTaskId());
	}
}
