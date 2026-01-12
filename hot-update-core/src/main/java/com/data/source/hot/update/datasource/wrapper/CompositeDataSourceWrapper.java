package com.data.source.hot.update.datasource.wrapper;

import com.data.source.hot.update.datasource.HotUpdateDataSource;

import javax.sql.DataSource;
import java.util.List;

public class CompositeDataSourceWrapper implements HotUpdateDataSourceWrapper {

    private List<HotUpdateDataSourceWrapper> wrapperList;

    public CompositeDataSourceWrapper(List<HotUpdateDataSourceWrapper> wrapperList) {
        this.wrapperList = wrapperList;
    }

    @Override
    public String getTargetClassName() {
        return "";
    }

    @Override
    public HotUpdateDataSource wrapper(DataSource dataSource)  {
        for (HotUpdateDataSourceWrapper wr : wrapperList) {
            HotUpdateDataSource wrapper = wr.wrapper(dataSource);
            if (wrapper != null) {
                return wrapper;
            }
        }
        return null;
    }

    @Override
    public HotUpdateDataSource wrapperInstance(DataSource dataSource) {
        return null;
    }
}
