package com.data.source.hot.update.datasource.wrapper;

import com.data.source.hot.update.datasource.HikariHotUpdateDataSource;
import com.data.source.hot.update.datasource.HotUpdateDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public class HikariDataSourceWrapper implements HotUpdateDataSourceWrapper {
    protected  final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String getTargetClassName() {
        return "com.zaxxer.hikari.HikariDataSource";
    }

    @Override
    public HotUpdateDataSource wrapperInstance(DataSource dataSource) {
        logger.info("使用Hikari数据源连接池");
        return new HikariHotUpdateDataSource((HikariDataSource) dataSource);
    }

}
