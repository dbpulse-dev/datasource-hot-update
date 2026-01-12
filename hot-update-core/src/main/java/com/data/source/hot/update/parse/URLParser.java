
package com.data.source.hot.update.parse;


public class URLParser {

    private static final String MYSQL_JDBC_URL_PREFIX = "jdbc:mysql";
    private static final String ORACLE_JDBC_URL_PREFIX = "jdbc:oracle";
    private static final String H2_JDBC_URL_PREFIX = "jdbc:h2";
    private static final String POSTGRESQL_JDBC_URL_PREFIX = "jdbc:postgresql";
    private static final String MARIADB_JDBC_URL_PREFIX = "jdbc:mariadb";
    private static final String MSSQL_JTDS_URL_PREFIX = "jdbc:jtds:sqlserver:";
    private static final String MSSQL_JDBC_URL_PREFIX = "jdbc:sqlserver:";

    public static ConnectionInfo parser(String url) {
        ConnectionURLParser parser = null;
        String lowerCaseUrl = url.toLowerCase();
        if (lowerCaseUrl.startsWith(MYSQL_JDBC_URL_PREFIX)) {
            parser = new MysqlURLParser(url);

        } else if (lowerCaseUrl.startsWith(ORACLE_JDBC_URL_PREFIX)) {
            parser = new OracleURLParser(url);
        } else if (lowerCaseUrl.startsWith(H2_JDBC_URL_PREFIX)) {
            parser = new H2URLParser(url);

        } else if (lowerCaseUrl.startsWith(POSTGRESQL_JDBC_URL_PREFIX)) {
            parser = new PostgreSQLURLParser(url);
        } else if (lowerCaseUrl.startsWith(MARIADB_JDBC_URL_PREFIX)) {
            parser = new MariadbURLParser(url);
        } else if (lowerCaseUrl.startsWith(MSSQL_JTDS_URL_PREFIX)) {
            parser = new MssqlJtdsURLParser(url);
        } else if (lowerCaseUrl.startsWith(MSSQL_JDBC_URL_PREFIX)) {
            parser = new MssqlJdbcURLParser(url);
        }
        ConnectionInfo parse = parser.parse();
        return parse;
    }
}
