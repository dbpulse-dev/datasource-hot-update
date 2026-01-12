
package com.data.source.hot.update;

import com.data.source.hot.update.datasource.HotUpdateDataSource;
import com.data.source.hot.update.datasource.wrapper.HotUpdateDataSourceWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.sql.DataSource;

/**
 * 数据源包装拦截
 * 对被代理的数据源连接池进行代理封装
 */
public class DataSourceWrapperProcessor implements BeanPostProcessor {

    protected static final Logger logger = LoggerFactory.getLogger(DataSourceWrapperProcessor.class);

    private HotUpdateDataSourceWrapper dataSourceWrapper;

    public DataSourceWrapperProcessor(HotUpdateDataSourceWrapper dataSourceWrapper) {
        this.dataSourceWrapper = dataSourceWrapper;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DataSource) {
            HotUpdateDataSource wrapper = dataSourceWrapper.wrapper((DataSource) bean);
            if (wrapper != null) {
                return wrapper;
            }
            logger.warn("没匹配热更新数据源{}", bean.getClass().getName());
            return bean;
        }
        return bean;
    }

}
