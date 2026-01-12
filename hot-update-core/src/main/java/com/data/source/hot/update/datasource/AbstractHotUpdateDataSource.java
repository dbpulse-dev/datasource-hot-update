package com.data.source.hot.update.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;


public abstract class AbstractHotUpdateDataSource<T extends DataSource> implements HotUpdateDataSource<T> {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractHotUpdateDataSource.class);

    /**
     * 被代理的连接池实例
     */
    protected volatile T delegate;

    private String name;

    private volatile boolean updateIng;

    private boolean asyncShutDown;

    protected String jdbcUrl;

    protected int maxPoolSize = -1;

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public T getDelegate() {
        return delegate;
    }

    public AbstractHotUpdateDataSource(T delegate) {
        this.delegate = delegate;
    }

    public AbstractHotUpdateDataSource(T delegate, boolean asyncShutDown) {
        this.delegate = delegate;
        this.asyncShutDown = asyncShutDown;
    }

    @Override
    public void setUpdateIng(boolean updateIng) {
        this.updateIng = updateIng;
    }

    @Override
    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (updateIng) {
            return getAfterUpdateFinish();
        } else {
            return delegate.getConnection();
        }
    }

    private Connection getAfterUpdateFinish() throws SQLException {
        logger.info("等待库更新完成...");
        synchronized (this) {
            return delegate.getConnection();
        }
    }

    @Override
    public ShutDownStrategy getShutDownStrategy() {
        if (asyncShutDown) {
            return new AsyncShutdown(new SyncShutdown(this));
        } else {
            return new SyncShutdown(this);
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return delegate.getConnection(username, password);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return delegate.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return delegate.isWrapperFor(iface);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return delegate.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        delegate.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        delegate.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return delegate.getLoginTimeout();
    }

    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return delegate.getParentLogger();
    }
}
