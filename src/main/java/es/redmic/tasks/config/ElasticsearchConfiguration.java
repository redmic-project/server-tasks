package es.redmic.tasks.config;

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
