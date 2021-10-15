package es.redmic.tasks;

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

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.databind.Module;

import es.redmic.databaselib.common.repository.BaseRepositoryImpl;
import es.redmic.db.config.EntityManagerWrapper;
import es.redmic.tasks.config.ResourceBundleMessageSource;
import io.micrometer.core.instrument.MeterRegistry;

@SpringBootApplication(exclude = { MongoAutoConfiguration.class, ElasticsearchAutoConfiguration.class })
@ComponentScan({ "es.redmic.tasks", "es.redmic.es", "es.redmic.databaselib", "es.redmic.db", "es.redmic.mediastorage",
		"es.redmic.brokerlib" })
@EnableJpaRepositories(basePackages = { "es.redmic.tasks", "es.redmic.databaselib",
		"es.redmic.db" }, repositoryBaseClass = BaseRepositoryImpl.class)
@EntityScan({ "es.redmic.db", "es.redmic.databaselib", "es.redmic.tasks" })
@EnableCaching(proxyTargetClass = true)
public class TasksApplication {

	@Value("${info.microservice.name}")
	String microserviceName;

	public static void main(String[] args) {
		SpringApplication.run(TasksApplication.class, args);
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer p = new PropertySourcesPlaceholderConfigurer();
		p.setIgnoreUnresolvablePlaceholders(true);
		return p;
	}

	@PostConstruct
	@Bean
	public MessageSource messageSource() {

		return new ResourceBundleMessageSource();
	}

	@Bean
	public Module jtsModule() {
		return new JtsModule();
	}

	@Bean
	public EntityManagerWrapper entityManagerWrapper() {
		return new EntityManagerWrapper();
	}

	// TODO: Si finalmente se mantiene tasks, pasar esta config a un nuevo fichero.
	@Configuration
	@EnableWebSecurity
	public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().antMatchers("/tasks/actuator/**").permitAll();
		}
	}

	@Bean
	MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
		return registry -> registry.config().commonTags("application", microserviceName);
	}
}
