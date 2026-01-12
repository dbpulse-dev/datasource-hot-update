package com.data.source.hot.update;


import com.data.source.hot.update.datasource.HotUpdateDataSource;
import com.data.source.hot.update.parse.ConnectionInfo;
import com.data.source.hot.update.parse.URLParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 数据源更新管理
 */
public class DataSourceHotUpdateManager implements ApplicationListener<ApplicationReadyEvent> {

    protected static final Logger logger = LoggerFactory.getLogger(DataSourceHotUpdateManager.class);

    private static List<DataSourceHolder> dataSourceHolders = new ArrayList<>();

    private static AtomicInteger index = new AtomicInteger(0);

    @Override
    public void onApplicationEvent(ApplicationReadyEvent readyEvent) {
        ConfigurableApplicationContext applicationContext = readyEvent.getApplicationContext();
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanName);
            if (bean instanceof HotUpdateDataSource) {
                DataSourceHolder holder = build((HotUpdateDataSource) bean);
                dataSourceHolders.add(holder);
            }
        }
    }

    public static List<DataSourceHolder> getDataSourceHolders() {
        return dataSourceHolders;
    }

    public static boolean updateDataSource(DataSourceHolder holder, String newJdbcUrl,int poolSize) {
        HotUpdateDataSource hotUpdateDataSource = holder.getDataSource();
        logger.info("正在更新新数据源地址->{}", newJdbcUrl);
        synchronized (hotUpdateDataSource) {
            hotUpdateDataSource.setUpdateIng(true);
            try {
                hotUpdateDataSource.getShutDownStrategy().shutdown();
                hotUpdateDataSource.setJdbcUrl(newJdbcUrl);
                hotUpdateDataSource.setMaxPoolSize(poolSize);
                hotUpdateDataSource.rebuild();
                holder.setUrl(newJdbcUrl);
                hotUpdateDataSource.setName("data-source-" + index.incrementAndGet());
                logger.info("更新数据源地址成功->{}", newJdbcUrl);
                return true;
            } catch (Exception e) {
                logger.error("热更新数据源失败->{}", newJdbcUrl, e);
            } finally {
                hotUpdateDataSource.setUpdateIng(false);
            }
            return false;
        }
    }

    private static DataSourceHolder build(HotUpdateDataSource dataSource) {
        ConnectionInfo connectionInfo = URLParser.parser(dataSource.getJdbcUrl());
        DataSourceHolder holder = new DataSourceHolder();
        holder.setDataSource(dataSource);
        holder.setUrl(dataSource.getJdbcUrl());
        holder.setHost(connectionInfo.getHost());
        holder.setPort(connectionInfo.getPort());
        holder.setDbName(connectionInfo.getDatabaseName());
        holder.setDbType(connectionInfo.getDBType());
        return holder;
    }


}
