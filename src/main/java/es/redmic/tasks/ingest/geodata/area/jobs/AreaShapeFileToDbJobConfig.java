package es.redmic.tasks.ingest.geodata.area.jobs;

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

import java.util.List;

import javax.annotation.PostConstruct;

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

import es.redmic.db.geodata.area.service.AreaService;
import es.redmic.es.config.OrikaScanBeanESItfc;
import es.redmic.models.es.geojson.area.dto.AreaDTO;
import es.redmic.tasks.common.utils.JobUtils;
import es.redmic.tasks.ingest.geodata.area.listener.IngestDataAreaListener;
import es.redmic.tasks.ingest.geodata.area.listener.IngestDataAreaStepListener;
import es.redmic.tasks.ingest.geodata.common.jobs.shapefile.ShapeFileFieldSetMapper;
import es.redmic.tasks.ingest.geodata.common.jobs.shapefile.ShapeFileItemReader;
import es.redmic.tasks.ingest.geodata.common.jobs.shapefile.ShapeFileLineMapper;

@Configuration
public class AreaShapeFileToDbJobConfig {

	@Value("${property.path.media_storage.temp.INGEST_DATA}")
	private String PATH_AREA_TEMP;

	@Value("${property.INGEST_DATA_AREA_TASK_NAME}")
	private String TASK_NAME;

	@Autowired
	protected JobBuilderFactory jobBuilderFactory;

	@Autowired
	protected StepBuilderFactory stepBuilderFactory;

	@Autowired
	private IngestDataAreaListener ingestDataAreaListener;

	@Autowired
	private IngestDataAreaStepListener ingestDataAreaStepListener;

	@Autowired
	AreaService areaService;

	@Autowired
	@Qualifier("jpaTransactionManager")
	PlatformTransactionManager jpaTransactionManager;

	@Autowired
	protected OrikaScanBeanESItfc orikaMapper;

	@PostConstruct
	@Bean(name = "createAreaIndexing")
	public Job createAreaIndexing() throws Exception {
		// @formatter:off
		return jobBuilderFactory.get(TASK_NAME)
					.listener(ingestDataAreaListener)
						.start(createAreaStepIndexing())
							.build();
		// @formatter:on
	}

	@Bean(name = "createAreaStepIndexing")
	public Step createAreaStepIndexing() throws Exception {

		// @formatter:off
		return stepBuilderFactory.get("indexing-area-step")
				.transactionManager(jpaTransactionManager)
				.<List<AreaDTO>, List<AreaDTO>>chunk(10)
				.reader(areaShapeFileItemReader(null))
					.faultTolerant()
					.writer(saveArea(null))
					.listener(ingestDataAreaStepListener)
//							.listener(logProcessListener)
									.build();
		
		// @formatter:on
	}

	@Bean
	@StepScope
	public ShapeFileItemReader<List<AreaDTO>> areaShapeFileItemReader(
			@Value("#{jobParameters['parameters']}") JobParameter parameter) throws Exception {

		AreaParameters parameters = JobUtils.JobParameters2UserParameters(parameter, AreaParameters.class);

		String tempDir = PATH_AREA_TEMP + "/" + parameters.getTaskId() + "/";

		ShapeFileItemReader<List<AreaDTO>> itemReader = new ShapeFileItemReader<List<AreaDTO>>();

		itemReader.setLineMapper(areaLineMapper(null));
		itemReader.setSourceDirectory(tempDir);

		itemReader.afterPropertiesSet();

		return itemReader;
	}

	@Bean
	@StepScope
	public ShapeFileLineMapper<List<AreaDTO>> areaLineMapper(
			@Value("#{jobParameters['parameters']}") JobParameter parameter) throws Exception {

		AreaParameters parameters = JobUtils.JobParameters2UserParameters(parameter, AreaParameters.class);

		ShapeFileLineMapper<List<AreaDTO>> lineMapper = new ShapeFileLineMapper<List<AreaDTO>>();

		lineMapper.setFieldSetMapper(areaFieldSetMapper(parameters));
		lineMapper.afterPropertiesSet();

		return lineMapper;
	}

	protected ShapeFileFieldSetMapper<List<AreaDTO>> areaFieldSetMapper(AreaParameters parameters) {

		return new AreaFieldSetMapper(parameters);
	}

	@Bean
	@StepScope
	public ItemWriter<List<AreaDTO>> saveArea(@Value("#{jobParameters['parameters']}") JobParameter parameter) {

		AreaParameters parameters = JobUtils.JobParameters2UserParameters(parameter, AreaParameters.class);
		return new AreaItemWritter(areaService, parameters.getTaskId());
	}
}
