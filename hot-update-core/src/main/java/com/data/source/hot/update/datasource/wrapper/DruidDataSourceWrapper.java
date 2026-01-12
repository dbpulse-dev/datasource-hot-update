package com.data.source.hot.update.datasource.wrapper;

import com.alibaba.druid.pool.DruidDataSource;
import com.data.source.hot.update.datasource.DruidHotUpdateDataSource;
import com.data.source.hot.update.datasource.HotUpdateDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public class DruidDataSourceWrapper implements HotUpdateDataSourceWrapper {
    protected  final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String getTargetClassName() {
        return "com.alibaba.druid.pool.DruidDataSource";
    }

    @Override
    public HotUpdateDataSource wrapperInstance(DataSource dataSource) {
        logger.info("使用Druid数据源连接池");
        return new DruidHotUpdateDataSource((DruidDataSource) dataSource);
    }

}
