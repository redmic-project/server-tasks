package es.redmic.tasks.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.validation.annotation.Validated;

@Configuration
public class RedmicDataSource {

	@Bean
	@Primary
	@Validated
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource primaryDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "jpaTransactionManager")
	@Primary
	@Autowired
	public PlatformTransactionManager jpaTransactionManager(
			LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setDataSource(primaryDataSource());
		txManager.setEntityManagerFactory(localContainerEntityManagerFactoryBean.getObject()/* entityManagerFactory */);
		txManager.afterPropertiesSet();

		return txManager;
	}

}
