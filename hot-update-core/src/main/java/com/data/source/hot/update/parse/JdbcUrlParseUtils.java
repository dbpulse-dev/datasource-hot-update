package com.data.source.hot.update.parse;


import com.data.source.hot.update.DataSourceHolder;

public class JdbcUrlParseUtils {


    public static void main(String[] args) {
        DataSourceHolder mysql = parse("jdbc:mysql://localhost:3306/abc");
        DataSourceHolder mysql2 = parse("jdbc:mysql://localhost/abc");
        DataSourceHolder mysql3 = parse("jdbc:mysql://mysql-dev-master-cygnet.test.cn:3306/ops_board?useSSL=false");
        DataSourceHolder mysql4 = parse("jdbc:mysql://mysql-dev-master-cygnet.test.cn/ops_board?useSSL=false");
        DataSourceHolder mysql5 = parse("jdbc:mysql://192.168.163.45:3306/test?useSSL=false");
        DataSourceHolder mysql6 = parse("jdbc:mysql://192.168.163.45/test?useSSL=false");
        System.out.println("mysql:" + mysql);
        System.out.println("mysql2:" + mysql2);
        System.out.println("mysql3:" + mysql3);
        System.out.println("mysql4:" + mysql4);
        System.out.println("mysql5:" + mysql5);
        System.out.println("mysql6:" + mysql6);
        DataSourceHolder pg = parse("jdbc:postgresql://localhost:5432/abc");
        DataSourceHolder pg2 = parse("jdbc:postgresql://localhost/abc");
        DataSourceHolder pg3 = parse("jdbc:postgresql://mysql-dev-master-cygnet.test.cn:5432/ops_board?useSSL=false");
        DataSourceHolder pg4 = parse("jdbc:postgresql://mysql-dev-master-cygnet.test.cn/ops_board?useSSL=false");
        System.out.println("pg:" + pg);
        System.out.println("pg2:" + pg2);
        System.out.println("pg3:" + pg3);
        System.out.println("pg4:" + pg4);

        DataSourceHolder or = parse("jdbc:oracle:thin:@//mydbserver:1521/orcl");
        DataSourceHolder or2 = parse("jdbc:oracle:thin:@mydbserver:1521:ORCL");
        DataSourceHolder or3 = parse("jdbc:oracle:thin:@192.168.163.166:1521/TOPTEST");
        System.out.println("or:" + or);
        System.out.println("or2:" + or2);
        System.out.println("or3:" + or3);
    }

    public static DataSourceHolder parse(String url) {
        ConnectionInfo connectionInfo = URLParser.parser(url);
        DataSourceHolder holder = new DataSourceHolder();
        holder.setUrl(url);
        holder.setHost(connectionInfo.getHost());
        holder.setPort(connectionInfo.getPort());
        holder.setDbName(connectionInfo.getDatabaseName());
        holder.setDbType(connectionInfo.getDBType());
        return holder;
    }

}

