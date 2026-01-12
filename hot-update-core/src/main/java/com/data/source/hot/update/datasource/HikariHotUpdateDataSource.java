package com.data.source.hot.update.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;

public class HikariHotUpdateDataSource extends AbstractHotUpdateDataSource<HikariDataSource> {

    public HikariHotUpdateDataSource(HikariDataSource delegate) {
        super(delegate);
    }

    public HikariHotUpdateDataSource(HikariDataSource delegate,boolean asyncShutDown) {
        super(delegate,asyncShutDown);
    }

    @Override
    public String getJdbcUrl() {
        return delegate.getJdbcUrl();
    }

    @Override
    public void rebuild(){
        HikariConfig newConfig = new HikariConfig();
        delegate.copyStateTo(newConfig);
        if(maxPoolSize>0){
            newConfig.setMaximumPoolSize(maxPoolSize);
        }
        maxPoolSize = newConfig.getMaximumPoolSize();
        newConfig.setJdbcUrl(jdbcUrl);
        HikariDataSource newDataSource = new HikariDataSource(newConfig);
        this.delegate = newDataSource;
    }

    @Override
    public void shutdownOldPool(HikariDataSource oldPool) throws InterruptedException {
        HikariPoolMXBean hikariPoolMXBean = oldPool.getHikariPoolMXBean();
        if(hikariPoolMXBean != null){
            waitForPoolNotBusy(() -> hikariPoolMXBean.getActiveConnections(), 60000);
        }
        oldPool.close();
    }

}
