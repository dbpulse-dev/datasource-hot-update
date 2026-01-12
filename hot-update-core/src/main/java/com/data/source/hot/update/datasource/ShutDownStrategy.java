package com.data.source.hot.update.datasource;

import java.sql.SQLException;

/**
 * 数据源关闭模式
 */
public interface ShutDownStrategy {

    void shutdown() throws InterruptedException, SQLException;
}
