package com.data.source.hot.update.config;


import com.alibaba.druid.pool.DruidDataSource;
import com.data.source.hot.update.DataSourceConfigChangeListener;
import com.data.source.hot.update.DataSourceHotUpdateManager;
import com.data.source.hot.update.DataSourceWrapperProcessor;
import com.data.source.hot.update.datasource.C3P0HotUpdateDataSource;
import com.data.source.hot.update.datasource.DruidHotUpdateDataSource;
import com.data.source.hot.update.datasource.HikariHotUpdateDataSource;
import com.data.source.hot.update.datasource.HotUpdateDataSource;
import com.data.source.hot.update.datasource.wrapper.*;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.List;
import java.util.function.Supplier;

public class DataSourceHotUpdateAutoConfig {
    protected static final Logger logger = LoggerFactory.getLogger(DataSourceHotUpdateAutoConfig.class);

    @Bean
    DataSourceWrapperProcessor dataSourceWrapperProcessor(@Qualifier("compositeDataSourceWrapper") HotUpdateDataSourceWrapper wrapper) {
        return new DataSourceWrapperProcessor(wrapper);
    }

    @Bean
    DataSourceHotUpdateManager dataSourceHotUpdateManager() {
        return new DataSourceHotUpdateManager();
    }

    @Bean
    CompositeDataSourceWrapper compositeDataSourceWrapper(List<HotUpdateDataSourceWrapper> hotUpdates) {
        return new CompositeDataSourceWrapper(hotUpdates);
    }

    @Bean
    DataSourceConfigChangeListener dataSourceConfigChangeListener() {
        return new DataSourceConfigChangeListener();
    }

    @Bean
    @ConditionalOnClass(name = {"com.alibaba.druid.pool.DruidDataSource"})
    DruidDataSourceWrapper druidDataSourceWrapper() {
        return new DruidDataSourceWrapper() {
            @Override
            public HotUpdateDataSource wrapper(DataSource dataSource) {
                logger.info("使用Druid数据源连接池");
                return new DruidHotUpdateDataSource((DruidDataSource) dataSource, false) {
                    @Override
                    public void waitForPoolNotBusy(Supplier<Integer> poolS, long waitTimeout) throws InterruptedException {
                        logger.info("模拟关闭连接耗时5000ms");
                        Thread.sleep(5000);
                        super.waitForPoolNotBusy(poolS, waitTimeout);
                    }
                };
            }
        };
    }

    @Bean
    @ConditionalOnClass(name = {"com.zaxxer.hikari.HikariDataSource"})
    HikariDataSourceWrapper hikariDataSourceWrapper() {
        return new HikariDataSourceWrapper() {
            @Override
            public HotUpdateDataSource wrapper(DataSource dataSource) {
                logger.info("使用Hikari数据源连接池");
                return new HikariHotUpdateDataSource((HikariDataSource) dataSource, false) {
                    @Override
                    public void waitForPoolNotBusy(Supplier<Integer> poolS, long waitTimeout) throws InterruptedException {
                        logger.info("模拟关闭连接耗时5000ms");
                        Thread.sleep(5000);
                        super.waitForPoolNotBusy(poolS, waitTimeout);
                    }
                };
            }
        };
    }


    @Bean
    @ConditionalOnClass(name = {"com.mchange.v2.c3p0.ComboPooledDataSource"})
    C3P0DataSourceWrapper c3P0DataSourceWrapper() {
        return new C3P0DataSourceWrapper() {
            @Override
            public HotUpdateDataSource wrapper(DataSource dataSource) {
                logger.info("使用C3P0数据源连接池");
                return new C3P0HotUpdateDataSource((ComboPooledDataSource) dataSource, true) {
                    @Override
                    public void waitForPoolNotBusy(Supplier<Integer> poolS, long waitTimeout) throws InterruptedException {
                        logger.info("模拟关闭连接耗时5000ms");
                        Thread.sleep(5000);
                        super.waitForPoolNotBusy(poolS, waitTimeout);
                    }
                };
            }
        };
    }


}
