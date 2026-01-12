package com.data.source.hot.update.datasource;

import com.alibaba.druid.pool.DruidDataSource;

public class DruidHotUpdateDataSource extends AbstractHotUpdateDataSource<DruidDataSource> {

    public DruidHotUpdateDataSource(DruidDataSource delegate) {
        super(delegate);
    }

    public DruidHotUpdateDataSource(DruidDataSource delegate,boolean asyncShutDown) {
        super(delegate,asyncShutDown);
    }

    @Override
    public void rebuild() {
        DruidDataSource newDataSource = delegate.cloneDruidDataSource();
        newDataSource.setUrl(jdbcUrl);
        if(maxPoolSize>0){
            newDataSource.setMaxActive(maxPoolSize);
        }
        maxPoolSize = newDataSource.getMaxActive();
        this.delegate = newDataSource;
    }

    @Override
    public String getJdbcUrl() {
        return delegate.getUrl();
    }

    @Override
    public void shutdownOldPool(DruidDataSource oldPool) throws InterruptedException {
        waitForPoolNotBusy(() -> oldPool.getActiveCount(), 60000);
        oldPool.close();
    }


}
