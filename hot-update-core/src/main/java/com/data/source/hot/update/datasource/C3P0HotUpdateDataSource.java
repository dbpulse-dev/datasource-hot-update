package com.data.source.hot.update.datasource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.SQLException;

public class C3P0HotUpdateDataSource extends AbstractHotUpdateDataSource<ComboPooledDataSource> {

    public C3P0HotUpdateDataSource(ComboPooledDataSource delegate) {
        super(delegate);
    }

    public C3P0HotUpdateDataSource(ComboPooledDataSource delegate,boolean asyncShutDown) {
        super(delegate,asyncShutDown);
    }

    @Override
    public void rebuild() {
        ComboPooledDataSource newPool = new ComboPooledDataSource();
        newPool.setJdbcUrl(jdbcUrl);
        newPool.setUser(delegate.getUser());
        newPool.setPassword(delegate.getPassword());
        newPool.setMaxPoolSize(delegate.getMaxPoolSize());
        newPool.setMinPoolSize(delegate.getMinPoolSize());
        newPool.setMaxIdleTime(delegate.getMaxIdleTime());
        newPool.setInitialPoolSize(delegate.getInitialPoolSize());
        newPool.setCheckoutTimeout(delegate.getCheckoutTimeout());
        if(maxPoolSize>0){
            newPool.setMaxPoolSize(maxPoolSize);
        }
        maxPoolSize = newPool.getMaxPoolSize();
        this.delegate=newPool;
    }

    @Override
    public String getJdbcUrl() {
        return delegate.getJdbcUrl();
    }

    @Override
    public void shutdownOldPool(ComboPooledDataSource oldPool) throws InterruptedException, SQLException {
        waitForPoolNotBusy(() -> {
            try {
                return oldPool.getNumBusyConnections();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 60000);
        oldPool.close();
    }

}
