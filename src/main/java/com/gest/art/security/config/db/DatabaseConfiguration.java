package com.gest.art.security.config.db;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
@Slf4j
@Configuration
@EnableTransactionManagement
//@EnableScheduling
@EnableJpaRepositories(
        entityManagerFactoryRef = "multiEntityManager",
        transactionManagerRef = "multiTransactionManager",
        basePackages = {"com.gest.art.security.repository", "com.gest.art.parametre.repository"})
@EntityScan("com.gest.art")
public class DatabaseConfiguration {

    //add JPA entities path here
    private final String PACKAGE_SCAN = "com.gest.art";

    //set db1 as the primary and default database connection
    @Primary
    @Bean(name = "postgres")
    @ConfigurationProperties("spring.datasource.postgres")
    public HikariDataSource postgreDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    //connection objects for the remaining 2 databases
    @Bean(name = "oracle")
    @ConfigurationProperties("spring.datasource.oracle")
    public HikariDataSource oracleDataSource() throws SQLException {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        TimeZone.setDefault(timeZone);
        return DataSourceBuilder.create().type(HikariDataSource.class).build();

    }

    //connection objects for the remaining 3 databases
    @Bean(name = "mysql")
    @ConfigurationProperties("spring.datasource.mysql")
    public HikariDataSource mysqlDataSource() throws SQLException {
        // TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        // TimeZone.setDefault(timeZone);
        return DataSourceBuilder.create().type(HikariDataSource.class).build();

    }

    //The multidatasource configuration
    @Bean(name = "multiRoutingDataSource")
    public DataSource multiRoutingDataSource() throws SQLException {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(ClientDB.POSTGRE, postgreDataSource());
        targetDataSources.put(ClientDB.ORACLE, oracleDataSource());
        targetDataSources.put(ClientDB.MYSQL, mysqlDataSource());
        MultiRoutingDataSource multiRoutingDataSource = new MultiRoutingDataSource();
        multiRoutingDataSource.setDefaultTargetDataSource(postgreDataSource());
        multiRoutingDataSource.setTargetDataSources(targetDataSources);
        return multiRoutingDataSource;
    }

    //add multi entity configuration code
    @Bean(name = "multiEntityManager")
    public LocalContainerEntityManagerFactoryBean multiEntityManager() throws SQLException {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(multiRoutingDataSource());
        em.setPackagesToScan(PACKAGE_SCAN);
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(hibernateProperties());
        return em;
    }

    @Bean(name = "multiTransactionManager")
    public PlatformTransactionManager multiTransactionManager() throws SQLException {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(multiEntityManager().getObject());
        return transactionManager;
    }

    @Primary
    @Qualifier
    @Bean(name = "entityManagerFactory")
    public LocalSessionFactoryBean dbSessionFactory() throws SQLException {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        DataSource dataSource = multiRoutingDataSource();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setPackagesToScan(PACKAGE_SCAN);
        Properties properties = hibernateProperties();
        log.info("MA BASE DANS CONFIG: {}", dataSource);
        sessionFactoryBean.setHibernateProperties(properties);
        return sessionFactoryBean;
    }

    // add hibernate properties
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.show_sql", true);
        properties.put("hibernate.format_sql", true);
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.hbm2ddl.auto", "update");
        //  properties.put("hibernate.id.new_generator_mappings", false);
        //  properties.put("hibernate.jdbc.lob.non_contextual_creation", true);
        return properties;
    }

}
