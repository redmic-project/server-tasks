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

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.redmic.es.config.EsClientProvider;
import es.redmic.es.config.EsConfig;

@Configuration
public class ElasticsearchConfiguration {

	@Value("#{'${elastic.addresses}'.split(',')}")
	private List<String> addresses;
	@Value("${elastic.port}")
	private Integer port;
	@Value("${elastic.clusterName}")
	private String clusterName;
	@Value("${elastic.xpackSecurityUser}")
	private String xpackSecurityUser;

	@Bean
	public EsClientProvider esClientProvider() {

		EsConfig elastic = new EsConfig();
		elastic.setAddresses(addresses);
		elastic.setPort(port);
		elastic.setClusterName(clusterName);
		elastic.setXpackSecurityUser(xpackSecurityUser);
		return new EsClientProvider(elastic);
	}
}
