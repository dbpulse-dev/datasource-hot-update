package com.data.source.change.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages =
        {
                "com.data.source.change.mapper"
        },
        sqlSessionTemplateRef = "adminSqlSessionTemplate")
public class DBAutoConfig {


    @Bean
    @ConditionalOnClass(name = {"com.alibaba.druid.pool.DruidDataSource"})
    @ConfigurationProperties(prefix = "spring.datasource.admin")
    public DataSource druidDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        return dataSource;
    }

    @Bean
    @ConditionalOnClass(name = {"com.mchange.v2.c3p0.ComboPooledDataSource"})
    @ConfigurationProperties(prefix = "spring.datasource.admin")
    public DataSource comboPoolDataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        return dataSource;
    }


    @Bean
    @ConditionalOnClass(name = {"com.zaxxer.hikari.HikariDataSource"})
    @ConfigurationProperties(prefix = "spring.datasource.admin")
    public DataSource hikariDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        return dataSource;
    }

    //@Bean
//    @ConditionalOnClass(name = {"com.zaxxer.hikari.HikariConfig"})
//    @ConfigurationProperties(prefix = "spring.datasource.admin")
//    HikariConfig hikariConfig() {
//        return new HikariConfig();
//    }

//    @Primary
//    @Bean
//    @ConditionalOnClass(name = {"com.mchange.v2.c3p0.ComboPooledDataSource"})
//    @ConfigurationProperties(prefix = "spring.datasource.admin")
//    public DataSource comboPooledDataSource() {
//        ComboPooledDataSource dataSource = new ComboPooledDataSource();
//        return dataSource;
//    }


    @Bean
    public SqlSessionFactory adminSqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mappers/*Mapper.xml"));
        return factoryBean.getObject();

    }

    @Bean
    public SqlSessionTemplate adminSqlSessionTemplate(@Qualifier("adminSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    DataSourceTransactionManager adminDataSourceTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


}
