package com.data.source.hot.update.datasource.wrapper;

import com.data.source.hot.update.datasource.C3P0HotUpdateDataSource;
import com.data.source.hot.update.datasource.HotUpdateDataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public class C3P0DataSourceWrapper implements HotUpdateDataSourceWrapper {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String getTargetClassName() {
        return "com.mchange.v2.c3p0.ComboPooledDataSource";
    }

    @Override
    public HotUpdateDataSource wrapperInstance(DataSource dataSource) {
        logger.info("使用C3P0数据源连接池");
        return new C3P0HotUpdateDataSource((ComboPooledDataSource) dataSource);
    }
}
