package es.redmic.tasks.config;

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

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.validation.annotation.Validated;

@Configuration
@EnableBatchProcessing
public class JobConfig implements BatchConfigurer {

	@Autowired
	JobRegistry jobRegistry;

	@Bean
	@Validated
	@ConfigurationProperties(prefix = "jobs.datasource")
	public DataSource dataSourceJob() {

		return DataSourceBuilder.create().build();
	}

	@Bean
	public StepBuilderFactory stepBuilderFactory() throws Exception {
		StepBuilderFactory stepBuilderFactory = new StepBuilderFactory(getJobRepository(), getTransactionManager());

		return stepBuilderFactory;
	}

	@Bean
	public TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor task = new SimpleAsyncTaskExecutor();

		return task;
	}

	@Bean
	public JobBuilderFactory jobBuilderFactory() throws Exception {
		JobBuilderFactory factory = new JobBuilderFactory(getJobRepository());

		return factory;
	}

	@Bean
	public JobOperator jobOperator() throws Exception {
		SimpleJobOperator jobOperator = new SimpleJobOperator();
		jobOperator.setJobExplorer(getJobExplorer());
		jobOperator.setJobLauncher(getJobLauncher());
		jobOperator.setJobRegistry(jobRegistry);
		jobOperator.setJobRepository(getJobRepository());
		jobOperator.afterPropertiesSet();

		return jobOperator;
	}

	@Bean
	public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() throws Exception {
		JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
		jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
		jobRegistryBeanPostProcessor.afterPropertiesSet();

		return jobRegistryBeanPostProcessor;
	}

	@Override
	public JobExplorer getJobExplorer() throws Exception {
		JobExplorerFactoryBean factory = new JobExplorerFactoryBean();
		factory.setDataSource(dataSourceJob());
		factory.afterPropertiesSet();

		return factory.getObject();
	}

	@Override
	public JobLauncher getJobLauncher() throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(getJobRepository());
		jobLauncher.setTaskExecutor(taskExecutor());
		jobLauncher.afterPropertiesSet();

		return jobLauncher;
	}

	@Override
	public JobRepository getJobRepository() throws Exception {
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
		factory.setDataSource(dataSourceJob());
		factory.setTransactionManager(getTransactionManager());
		// factory.afterPropertiesSet();

		return factory.getObject();
	}

	@Override
	@Bean(name = "jobTransactionManager")
	public PlatformTransactionManager getTransactionManager() throws Exception {
		DataSourceTransactionManager ptm = new DataSourceTransactionManager(dataSourceJob());
		ptm.afterPropertiesSet();

		return ptm;
	}
}
