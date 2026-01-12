package com.data.source.hot.update.datasource.wrapper;

import com.data.source.hot.update.datasource.HotUpdateDataSource;

import javax.sql.DataSource;

public interface HotUpdateDataSourceWrapper {

    default boolean isPresent() {
        try {
            Class.forName(getTargetClassName());
            return true;
        } catch (ClassNotFoundException e) {

        }
        return false;
    }

    default HotUpdateDataSource wrapper(DataSource dataSource)  {
        if (!isPresent()) {
            return null;
        }
        try {
            Class<?> cls = Class.forName(getTargetClassName());
            if (dataSource.getClass().isAssignableFrom(cls)) {
                return wrapperInstance(dataSource);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    String getTargetClassName();

    HotUpdateDataSource wrapperInstance(DataSource dataSource);

}
