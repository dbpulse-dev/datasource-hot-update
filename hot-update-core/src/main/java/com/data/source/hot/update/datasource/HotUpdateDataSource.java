package com.data.source.hot.update.datasource;


import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.function.Supplier;

/**
 * 热更新数据源接口
 * 该接口的实现具体热更新的功能
 * @param <T>
 */
public interface HotUpdateDataSource<T extends DataSource> extends DataSource {

    void setUpdateIng(boolean updateIng);

    void setJdbcUrl(String jdbcUrl);

    String getJdbcUrl();
    /**
     * 据配置重建新的连接池
     */
    void rebuild();

    /**
     * 关闭旧的连接池
     * @param oldPool
     * @throws InterruptedException
     * @throws SQLException
     */
    void shutdownOldPool(T oldPool) throws InterruptedException, SQLException;

    /**
     * 关闭连接池方式
     * @return
     */
    ShutDownStrategy getShutDownStrategy();


    /**
     * 等旧连接池所有连接都释放
     * @param poolS
     * @param waitTimeout
     * @throws InterruptedException
     */
    default void waitForPoolNotBusy(Supplier<Integer> poolS, long waitTimeout) throws InterruptedException {
        long waitTime = 0;
        while (poolS.get() > 0) {
            Thread.sleep(10);
            waitTime += 10;
            if (waitTime > waitTimeout) {
                throw new RuntimeException("连接池热更超时");
            }
        }
    }

    void setName(String name);

    T getDelegate();

    void setMaxPoolSize(int maxPoolSize);

    int getMaxPoolSize();
}
