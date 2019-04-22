package es.redmic.tasks.ingest.series.timeseries.jobs;

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

import es.redmic.db.series.timeseries.service.TimeSeriesService;
import es.redmic.mediastorage.service.MediaStorageService;
import es.redmic.models.es.series.timeseries.dto.TimeSeriesDTO;
import es.redmic.tasks.common.utils.JobUtils;
import es.redmic.tasks.ingest.common.jobs.CsvToDbJobConfig;
import es.redmic.tasks.ingest.series.timeseries.listener.IngestDataTimeSeriesListener;
import es.redmic.tasks.ingest.series.timeseries.listener.IngestDataTimeSeriesStepListener;

@Configuration
public class TimeSeriesCsvToDbJobConfig extends CsvToDbJobConfig {

	@Value("${property.path.media_storage.temp.INGEST_DATA}")
	private String PATH_SERIES_TEMP;
	
	@Value("${property.INGEST_DATA_TIME_SERIES_TASK_NAME}")
	private String TASK_NAME;

	@Autowired
	protected JobBuilderFactory jobBuilderFactory;

	@Autowired
	protected StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private IngestDataTimeSeriesListener ingestDataTimeSeriesListener;
	
	@Autowired
	private IngestDataTimeSeriesStepListener ingestDataTimeSeriesStepListener;
	
	@Autowired
	MediaStorageService mediaStorage;

	@Autowired
	TimeSeriesService timeSeriesService;
	
	@Autowired
	@Qualifier("jpaTransactionManager")
	PlatformTransactionManager jpaTransactionManager;

	@PostConstruct
	@Bean(name= "createTimeSeriesIndexing")
	public Job createTimeSeriesIndexing() throws Exception {
		// @formatter:off
		return jobBuilderFactory.get(TASK_NAME)
					.listener(ingestDataTimeSeriesListener)
						.start(createTimeSeriesStepIndexing())
							.build();
		// @formatter:on
	}

	@Bean(name="createTimeSeriesStepIndexing")
	public Step createTimeSeriesStepIndexing() throws Exception {

		// @formatter:off
		return stepBuilderFactory.get("indexing-timeseries-step")
				.transactionManager(jpaTransactionManager)
				.listener(ingestDataTimeSeriesStepListener)
				.<List<TimeSeriesDTO>, List<TimeSeriesDTO>>chunk(10)
				.reader(timeSeriesCsvFileItemReader(null))
					.writer(saveTimeSeries(null))
//							.listener(logProcessListener)
								.faultTolerant()
									.build();
		
		// @formatter:on
	}

	@Bean
	@StepScope
	public FlatFileItemReader<List<TimeSeriesDTO>> timeSeriesCsvFileItemReader(@Value("#{jobParameters['parameters']}") JobParameter parameter) throws Exception {
		
		TimeSeriesParameters parameters = JobUtils.JobParameters2UserParameters(parameter, TimeSeriesParameters.class);

		FlatFileItemReader<List<TimeSeriesDTO>> itemReader = new FlatFileItemReader<List<TimeSeriesDTO>>(); 
		File file = mediaStorage.openTempFile(PATH_SERIES_TEMP, parameters.getFileName());
		
		itemReader.setResource(new FileSystemResource(file));		
		itemReader.setLineMapper(timeSeriesLineMapper(null));
		itemReader.setLinesToSkip(1);

		itemReader.afterPropertiesSet();

		return itemReader;
	}

	@Bean
	@StepScope
	public DefaultLineMapper<List<TimeSeriesDTO>> timeSeriesLineMapper(@Value("#{jobParameters['parameters']}") JobParameter parameter) {
		
		TimeSeriesParameters parameters = JobUtils.JobParameters2UserParameters(parameter, TimeSeriesParameters.class);
		
		DefaultLineMapper<List<TimeSeriesDTO>> lineMapper = new DefaultLineMapper<List<TimeSeriesDTO>>();

		lineMapper.setLineTokenizer(createTokenizer(parameters.getHeader(), parameters.getDelimiter()));
		lineMapper.setFieldSetMapper(timeSeriesFieldSetMapper(parameters));
		lineMapper.afterPropertiesSet();

		return lineMapper;
	}

	protected FieldSetMapper<List<TimeSeriesDTO>> timeSeriesFieldSetMapper(TimeSeriesParameters parameters) {
		
		return new TimeSeriesFieldSetMapper(parameters);
	}

	@Bean
	@StepScope
	public ItemWriter<List<TimeSeriesDTO>> saveTimeSeries(@Value("#{jobParameters['parameters']}") JobParameter parameter) {
		
		TimeSeriesParameters parameters = JobUtils.JobParameters2UserParameters(parameter, TimeSeriesParameters.class);
		
		return new TimeSeriesItemWritter(timeSeriesService, parameters.getTaskId());	
	}
}
