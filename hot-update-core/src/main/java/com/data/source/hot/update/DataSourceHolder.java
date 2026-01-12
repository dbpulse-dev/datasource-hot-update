package com.data.source.hot.update;

import com.data.source.hot.update.datasource.HotUpdateDataSource;

public class DataSourceHolder {

    private HotUpdateDataSource dataSource;

    private String host;
    private Integer port;

    private String dbName;
    private String dbType;

    private String url;



    public void setDataSource(HotUpdateDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public HotUpdateDataSource getDataSource() {
        return dataSource;
    }

    public  int getMaxPoolSize() {
        return dataSource.getMaxPoolSize();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
