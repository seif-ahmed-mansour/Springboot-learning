package com.demo.crud.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        basePackages = "com.demo.crud.audit",
        entityManagerFactoryRef = "training2EntityManagerFactory",
        transactionManagerRef = "training2TransactionManager"
)
public class TrainingDb2Config {

    @Bean(name = "training2DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.training2")
    public DataSource training2DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "training2EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean training2EntityManagerFactory(
            @Qualifier("training2DataSource") DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.demo.crud.audit.model");
        em.setPersistenceUnitName("training2");

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

    @Bean(name = "training2TransactionManager")
    public PlatformTransactionManager training2TransactionManager(
            @Qualifier("training2EntityManagerFactory") LocalContainerEntityManagerFactoryBean training2EntityManagerFactory) {
        return new JpaTransactionManager(training2EntityManagerFactory.getObject());
    }
}