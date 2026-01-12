package com.data.source.hot.update.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 异步方式关闭数据源
 */
public class AsyncShutdown implements ShutDownStrategy {

    protected static final Logger logger = LoggerFactory.getLogger(AsyncShutdown.class);
    private ShutDownStrategy shutDownMode;

    public AsyncShutdown(SyncShutdown syncShutdown) {
        this.shutDownMode = syncShutdown;
    }

    @Override
    public void shutdown() {
        logger.info("正在异步关闭连接池...");
        new Thread("data-source-pool-shutdown") {
            @Override
            public void run() {
                try {
                    shutDownMode.shutdown();
                } catch (Exception e) {
                    logger.error("关闭数据源连接池异常", e);
                }
            }
        }.start();
    }
}
