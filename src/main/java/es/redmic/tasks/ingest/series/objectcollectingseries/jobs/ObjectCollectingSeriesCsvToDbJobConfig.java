package es.redmic.tasks.ingest.series.objectcollectingseries.jobs;

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

import es.redmic.db.series.objectcollecting.service.ObjectCollectingSeriesService;
import es.redmic.es.maintenance.objects.service.ObjectTypeESService;
import es.redmic.mediastorage.service.MediaStorageService;
import es.redmic.models.es.series.objectcollecting.dto.ObjectCollectingSeriesDTO;
import es.redmic.tasks.common.utils.JobUtils;
import es.redmic.tasks.ingest.common.jobs.CsvToDbJobConfig;
import es.redmic.tasks.ingest.series.objectcollectingseries.listener.IngestDataObjectCollectingSeriesListener;
import es.redmic.tasks.ingest.series.objectcollectingseries.listener.IngestDataObjectCollectingSeriesStepListener;

@Configuration
public class ObjectCollectingSeriesCsvToDbJobConfig extends CsvToDbJobConfig {

	@Value("${property.path.media_storage.temp.INGEST_DATA}")
	private String PATH_SERIES_TEMP;
	
	@Value("${property.INGEST_DATA_OBJECT_COLLECTING_SERIES_TASK_NAME}")
	private String TASK_NAME;

	@Autowired
	protected JobBuilderFactory jobBuilderFactory;

	@Autowired
	protected StepBuilderFactory stepBuilderFactory;

	@Autowired
	MediaStorageService mediaStorage;

	@Autowired
	ObjectCollectingSeriesService objectCollectingSeriesService;

	@Autowired
	@Qualifier("jpaTransactionManager")
	PlatformTransactionManager jpaTransactionManager;
	
	@Autowired
	IngestDataObjectCollectingSeriesListener ingestDataObjectCollectingSeriesListener;
	
	@Autowired
	private IngestDataObjectCollectingSeriesStepListener ingestDataObjectCollectingSeriesStepListener;
	
	@Autowired
	ObjectTypeESService objectTypeESService;

	@PostConstruct
	@Bean(name = "createObjectCollectingSeriesIndexing")
	public Job createObjectCollectingSeriesIndexing() {
		// @formatter:off
		return jobBuilderFactory.get(TASK_NAME)
					.listener(ingestDataObjectCollectingSeriesListener)
						.start(createObjectCollectingSeriesStepIndexing())
							.build();
		// @formatter:on
	}

	@Bean(name = "createObjectCollectingSeriesStepIndexing")
	public Step createObjectCollectingSeriesStepIndexing() {

		// @formatter:off
		return stepBuilderFactory.get("indexing-objectcollectingseries-step")
				.transactionManager(jpaTransactionManager)
				.listener(ingestDataObjectCollectingSeriesStepListener)
				.<List<ObjectCollectingSeriesDTO>, List<ObjectCollectingSeriesDTO>>chunk(10)
				.reader(objectCollectingSeriesCsvFileItemReader(null))
					.writer(saveObjectCollectingSeries(null))
						.faultTolerant()
							.build();
		// @formatter:on
	}

	@Bean
	@StepScope
	public FlatFileItemReader<List<ObjectCollectingSeriesDTO>> objectCollectingSeriesCsvFileItemReader(
			@Value("#{jobParameters['parameters']}") JobParameter parameter) {

		ObjectCollectingSeriesParameters parameters = JobUtils.JobParameters2UserParameters(parameter, ObjectCollectingSeriesParameters.class);

		FlatFileItemReader<List<ObjectCollectingSeriesDTO>> itemReader = new FlatFileItemReader<List<ObjectCollectingSeriesDTO>>();
		File file = mediaStorage.openTempFile(PATH_SERIES_TEMP, parameters.getFileName());

		itemReader.setResource(new FileSystemResource(file));
		itemReader.setLineMapper(objectCollectingSeriesLineMapper(null));
		itemReader.setLinesToSkip(1);

		try {
			itemReader.afterPropertiesSet();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return itemReader;
	}

	@Bean
	@StepScope
	public DefaultLineMapper<List<ObjectCollectingSeriesDTO>> objectCollectingSeriesLineMapper(
			@Value("#{jobParameters['parameters']}") JobParameter parameter) {

		ObjectCollectingSeriesParameters parameters = JobUtils.JobParameters2UserParameters(parameter, ObjectCollectingSeriesParameters.class);

		DefaultLineMapper<List<ObjectCollectingSeriesDTO>> lineMapper = new DefaultLineMapper<List<ObjectCollectingSeriesDTO>>();

		lineMapper.setLineTokenizer(createTokenizer(parameters.getHeader(), parameters.getDelimiter()));
		lineMapper.setFieldSetMapper(objectCollectingSeriesFieldSetMapper(parameters));
		lineMapper.afterPropertiesSet();

		return lineMapper;
	}

	protected FieldSetMapper<List<ObjectCollectingSeriesDTO>> objectCollectingSeriesFieldSetMapper(
			ObjectCollectingSeriesParameters parameters) {

		return new ObjectCollectingSeriesFieldSetMapper(parameters, objectTypeESService);
	}

	@Bean
	@StepScope
	public ItemWriter<List<ObjectCollectingSeriesDTO>> saveObjectCollectingSeries(
			@Value("#{jobParameters['parameters']}") JobParameter parameter) {

		ObjectCollectingSeriesParameters parameters = JobUtils.JobParameters2UserParameters(parameter, ObjectCollectingSeriesParameters.class);
		return new ObjectCollectingSeriesItemWritter(objectCollectingSeriesService, parameters.getTaskId());
	}
}
