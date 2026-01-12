

package com.data.source.hot.update.parse;


/**
 * {@link ConnectionInfo} stored the jdbc connection info, the connection info contains db type, host, port, database
 * name.
 */
public class ConnectionInfo {
    /**
     * DB type, such as mysql, oracle, h2.
     */
    private final String dbType;
    /**
     * Operation database name.
     */
    private String databaseName;

    private String databasePeer;
    private String host;
    private int port;

    /**
     * Component
     */


    public ConnectionInfo(String dbType, String host, int port, String databaseName) {
        this.dbType = dbType;
        this.databasePeer = host + ":" + port;
        this.databaseName = databaseName;
        this.host = host;
        this.port = port;
    }

    public ConnectionInfo(String dbType, String hosts, String databaseName) {
        this.dbType = dbType;
        this.databasePeer = hosts;
        this.databaseName = databaseName;
    }

    public String getDBType() {
        return dbType;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getDatabasePeer() {
        return databasePeer;
    }


    public void setDatabaseName(String dataBaseName) {
        this.databaseName = dataBaseName;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
