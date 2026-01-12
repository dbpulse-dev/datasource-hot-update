# 数据源热更新 (DataSource Hot Update)

## 项目简介

本项目提供了一套完整的数据源热更新解决方案，允许在应用程序运行过程中动态更新数据库连接配置，而无需重启应用。这对于生产环境中的数据库迁移、维护或配置调整非常有用。

## 核心特性

- **零停机时间**：在不中断服务的情况下更新数据源配置
- **多连接池支持**：支持主流数据源连接池的热更新
- **自动配置监听**：实时监听配置变化并自动应用更新
- **安全优雅关闭**：确保旧连接池中的连接被正确处理后才完全关闭
- **灵活的触发方式**：支持通过 API 调用或监听配置中心（如 Apollo、Nacos）变化来触发热更新
- **动态连接池参数调整**：支持动态调整连接池大小等参数

## 支持的数据源连接池

- **Druid** - 阿里巴巴开源的高性能数据库连接池
- **HikariCP** - 高性能 JDBC 连接池
- **C3P0** - 开源的 JDBC 连接池库

## 架构设计

### 核心组件

1. **HotUpdateDataSource 接口**
   - 定义可序列化连接地址并支持重建的代理接口
   - 提供数据源热更新的核心能力

2. **DataSourceHotUpdateManager**
   - 拦截并管理所有 HotUpdateDataSource 实例
   - 监听数据源配置变化
   - 执行数据源连接地址更新

3. **DataSourceWrapperProcessor**
   - 拦截并封装原始数据源连接池实例
   - 将其实例化为可热更新的代理对象

4. **各种 Wrapper 实现**
   - [DruidDataSourceWrapper](file:///D:/workspace/datasource-hot-update/hot-update-core/src/main/java/com/data/source/hot/update/datasource/wrapper/DruidDataSourceWrapper.java) - Druid 连接池包装器
   - [HikariDataSourceWrapper](file:///D:/workspace/datasource-hot-update/hot-update-core/src/main/java/com/data/source/hot/update/datasource/wrapper/HikariDataSourceWrapper.java) - HikariCP 连接池包装器
   - [C3P0DataSourceWrapper](file:///D:/workspace/datasource-hot-update/hot-update-core/src/main/java/com/data/source/hot/update/datasource/wrapper/C3P0DataSourceWrapper.java) - C3P0 连接池包装器

### 工作流程

1. **拦截与封装**
   - 启动时拦截并封装原始数据源连接池实例
   - 将其实例化为可序列化连接地址并可重建的代理对象

2. **实例管理**
   - 应用启动完成后，[DataSourceHotUpdateManager](file:///D:/workspace/datasource-hot-update/hot-update-core/src/main/java/com/data/source/hot/update/DataSourceHotUpdateManager.java) 拦截并保存所有 [HotUpdateDataSource](file:///D:/workspace/datasource-hot-update/hot-update-core/src/main/java/com/data/source/hot/update/datasource/HotUpdateDataSource.java) 的实例

3. **配置监听与更新**
   - 监听数据源配置地址变化 ([DataSourceHotUpdateManager](file:///D:/workspace/datasource-hot-update/hot-update-core/src/main/java/com/data/source/hot/update/DataSourceHotUpdateManager.java))
   - 如有发生变化，调用相应的 [HotUpdateDataSource](file:///D:/workspace/datasource-hot-update/hot-update-core/src/main/java/com/data/source/hot/update/datasource/HotUpdateDataSource.java) 实例更新数据源连接地址

## 使用方法

### 1. 添加依赖

项目已包含以下依赖：

```xml
<dependency>
    <groupId>com.zaxxer</groupId>
    <artifactId>HikariCP</artifactId>
    <version>3.4.5</version>
</dependency>
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.1.20</version>
</dependency>
<dependency>
    <groupId>com.mchange</groupId>
    <artifactId>c3p0</artifactId>
    <version>0.9.5.5</version>
</dependency>
```

### 2. 执更新示例

项目提供了 REST API 来控制数据源热更新示例,请看demo模块：

- **POST /data-source/update** - 更新数据源配置
- **GET /data-source/refresh** - 切换测试数据库（演示用途）

请求示例：
```json
{
  "oldValue": "jdbc:mysql://localhost:3306/old_db?...",
  "newValue": "jdbc:mysql://localhost:3306/new_db?..."
}
```

### 3. 配置中心集成

除了直接通过 API 触发热更新外，项目还可以与配置中心（如 Apollo、Nacos）集成，通过监听配置变化自动触发热更新：

1. **Apollo 集成**：监听 Apollo 配置变化，当数据库连接信息发生变更时自动执行热更新
2. **Nacos 集成**：监听 Nacos 配置中心中的数据库配置，实现动态更新
3. **自定义配置中心**：可根据需要扩展支持其他配置中心

### 4. 动态连接池参数调整

项目不仅支持更新数据库连接地址，还支持动态调整连接池的各种参数，如：

- 最小连接数
- 最大连接数
- 连接超时时间
- 空闲连接回收时间
- 其他连接池特定参数

配置示例：
```properties
# 连接池大小动态调整
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.druid.min-idle=5
spring.datasource.druid.max-active=20
```

### 5. 配置说明

在 [application.properties](file:///D:/workspace/datasource-hot-update/demo/src/main/resources/application.properties) 中配置数据库连接信息，项目会自动检测配置变化并触发热更新。

## 项目模块

- **hot-update-core** - 核心热更新功能实现
- **demo** - 示例应用程序，展示如何使用热更新功能

## 技术栈

- Spring Boot 2.3.3.RELEASE
- Java 8+
- Maven

## 安全性考虑

- 在更新过程中使用同步锁确保线程安全
- 优雅地关闭旧连接池，确保现有连接得到适当处理
- 提供超时机制防止连接池热更新过程无限期等待

## 应用场景

1. **数据库迁移**：在不停机的情况下将应用切换到新的数据库
2. **配置优化**：动态调整数据库连接参数以优化性能
3. **故障转移**：在主数据库出现故障时快速切换到备用数据库
4. **维护操作**：在维护期间临时切换到维护数据库
5. **动态扩缩容**：根据业务负载动态调整连接池大小

## 注意事项

- 确保新旧数据库结构兼容
- 在生产环境中进行热更新前先在测试环境中验证
- 监控热更新过程中的应用性能和错误日志
- 当动态调整连接池大小时，注意服务器资源限制

## 许可证

请参阅 LICENSE 文件获取更多信息。