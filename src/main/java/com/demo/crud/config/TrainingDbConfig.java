package com.demo.crud.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.demo.crud.students.repo", // Changed to your student repo package
        entityManagerFactoryRef = "trainingEntityManagerFactory",
        transactionManagerRef = "trainingTransactionManager"
)
public class TrainingDbConfig {

    @Primary
    @Bean(name = "trainingDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.training")
    public DataSource trainingDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "trainingEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean trainingEntityManagerFactory(
            @Qualifier("trainingDataSource") DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.demo.crud.students.model"); // Your student entity package
        em.setPersistenceUnitName("training");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.temp.use_jdbc_metadata_defaults", "false"); // Fix for CLOB issue
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Primary
    @Bean(name = "trainingTransactionManager")
    public PlatformTransactionManager trainingTransactionManager(
            @Qualifier("trainingEntityManagerFactory") LocalContainerEntityManagerFactoryBean trainingEntityManagerFactory) {
        return new JpaTransactionManager(trainingEntityManagerFactory.getObject());
    }
}