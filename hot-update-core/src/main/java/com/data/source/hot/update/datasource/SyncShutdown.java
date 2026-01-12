package com.data.source.hot.update.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 同步方式关闭数据源
 */
public class SyncShutdown implements ShutDownStrategy {

    protected static final Logger logger = LoggerFactory.getLogger(SyncShutdown.class);

    private HotUpdateDataSource dataSource;

    private DataSource delegate;

    public SyncShutdown(HotUpdateDataSource dataSource) {
        this.dataSource = dataSource;
        this.delegate = dataSource.getDelegate();
    }

    @Override
    public void shutdown() throws InterruptedException, SQLException {
        final long l = System.currentTimeMillis();
        try {
            dataSource.shutdownOldPool(this.delegate);
        } finally {
            logger.info("关闭连接池耗时:{}", (System.currentTimeMillis() - l));
        }
    }
}
