# 普通JDBC操作对比数据库连接池对比Hikari和Durid初体验

> 前言：最近在学习`JDBC`这块的内容，JDBC全称 `Java Database Connectivity `即Java数据库连接，它是一种可以执行SQL语句的 `Java API` , `API`是接口没有具体实现（这样我们就可以面向接口编程，不用管底层实现，通过切换配置文件就能轻松切换数据库驱动程序特别方便），具体的实现由数据库厂商提供，这些实现类就是驱动程序，可以到对应的数据库官网下载，如果是maven项目也可以同过pom 引入依赖。下面是使用的mysql 驱动的示例。对比了查询和插入时，原生的 和使用`Hikari`和`Durid`数据库之间的耗时差异 本文仅是个人学习体验参考。
>
> **如果要直接看耗时结果，直接跳到最后五耗时运行结果图**

## 一、引入依赖

```java
<!-- mysql jdbc 驱动-->
<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
</dependency>
<!-- 引入这个目前主要是加HikariCP 20220401 -->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<!-- 单元测试 -->    
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-test</artifactId>
	<scope>test</scope>
</dependency>
<!-- druid 连接池-->
<dependency>
	<groupId>com.alibaba</groupId>
	<artifactId>druid</artifactId>
	<version>1.0.29</version>
</dependency>    
```

## 二、配置文件

> `hikari` 的参数配置基本没有配置用的默认值，具体参考：[https://github.com/brettwooldridge/HikariCP](https://github.com/brettwooldridge/HikariCP)
>
> `durid `的参数我大概按照`hikari`的几个默认值来设置的作为对比 。具体参考：[https://github.com/alibaba/druid](https://github.com/alibaba/druid)

### （一）hikari.properties

```java
driver=com.mysql.cj.jdbc.Driver
url=jdbc:mysql://127.0.0.1:3306/gefrm?useUnicode=true&characterEncoding=utf-8&useSSL=false&useInformationSchema=true&serverTimezone=Asia/Shanghai&autoReconnect=true&failOverReadOnly=false
user=root
password=123456
```

### （二）middleware.properties

```java

driverClassName=com.mysql.cj.jdbc.Driver
url=jdbc:mysql://127.0.0.1:3306/gefrm?useUnicode=true&characterEncoding=utf-8&useSSL=false&useInformationSchema=true&serverTimezone=Asia/Shanghai&autoReconnect=true&failOverReadOnly=false
username=root
password=123456

#初始化的时候，连接池中放多少个连接
initialSize=10

# 最大存货的连接数量
#maxActive=50
maxActive=10

#最小空闲数量
#minIdle=5
minIdle=10

#配置获取连接等待超时的时间
#maxWait=10000
maxWait=30000

#验证连接池中的连接是否有效的sql语句
validationQuery='select 1'

#在获取连接的时候，验证拿到连接是否为有效连接
testOnBorrow=false

#在归还连接的时候，验证是否为有效连接
test-on-return=false

#空闲的时候验证是否有效
#test-while-idle=true
```

### （三）数据库表结构

```sql
CREATE TABLE `bsc_dict_info_test` (
  `dict_id` varchar(10) NOT NULL DEFAULT ' ' COMMENT '字典标识',
  `dict_name` varchar(256) NOT NULL DEFAULT ' ' COMMENT '字典名称',
  `dict_sitm_id` varchar(32) NOT NULL DEFAULT ' ' COMMENT '字典子项标识',
  `dict_sitm_name` varchar(256) NOT NULL DEFAULT ' ' COMMENT '字典子项名称',
  `suse_flag` char(1) NOT NULL DEFAULT '0' COMMENT '启用标志',
  `disp_orde` int(11) NOT NULL DEFAULT '0' COMMENT '展示顺序',
  `memo` varchar(4000) NOT NULL DEFAULT ' ' COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

## 三、代码

### （一）JdbcUtils

```java
package week5.question_10.jdbc04_test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author lvzb31988
 * @description: TODO
 * @date 2022/4/1
 */
public class JdbcUtils {
    public static Properties getProperty(String filePath) throws IOException {
        Properties properties = new Properties();
        try (InputStream ins = JdbcUtils.class.getClassLoader().getResourceAsStream(filePath)) {
            properties.load(ins);
            return properties;
        }
    }

    public static String getPropertyValue(String filePath, String key) throws IOException {
        return getProperty(filePath).getProperty(key);
    }
}

```

### （二）ConnectMySql

```java
package week5.question_10.jdbc04_test;

import java.io.IOException;
import java.sql.*;

/**
 * @author lvzb31988
 * @description: TODO
 * @date 2022/4/1
 */
public class ConnectMySql {
    /// com.mysql.cj.jdbc.Driver com.mysql.jdbc.Driver
    private static String DRIVER;
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    private static final String HIKARI_FILE_PATH = "hikari.properties";

    static {
        try {
            DRIVER = JdbcUtils.getPropertyValue(HIKARI_FILE_PATH,"driver");
            URL = JdbcUtils.getPropertyValue(HIKARI_FILE_PATH,"url");
            USER = JdbcUtils.getPropertyValue(HIKARI_FILE_PATH,"user");
            PASSWORD = JdbcUtils.getPropertyValue(HIKARI_FILE_PATH,"password");
            /// 初始化参数时就进行驱动预加载,现在新版本的mysql-jdbc 驱动已经可以省去这一步了
            /// Class.forName(DRIVER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 简单的查询sql语句
     *
     * @param sql 查询sql
     * @throws ClassNotFoundException
     */
    public static void select(String sql) {
        // 1. 加载驱动类
        // Class.forName(DRIVER);
        // 2. 使用DriverManager 获取数据库连接 其中 返回的 Connection 就代表了Java程序和数据库的连接
        // 3. 不同数据库的URL写法需要查询驱动文档
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             // 4. 使用 Connection 来创建一个Statement对象
             /**
              * Statement 有三种执行SQL语句的方法
              * 1. execute() 可执行任何SQL语句返回一个 boolean值 -> 如果第一个执行结果是 ResultSet 返回true 否则返回false
              * 2. executeQuery() 可执行 select 语句 -> 返回查询到的结果集
              * 3. executeUpdate() 用于执行 DML 语句 -> 返回一个整数 代表被SQL语句影响的记录数
              */
             Statement statement = conn.createStatement();
             // 5. 执行SQL语句
             ResultSet rs = statement.executeQuery(sql);
        ) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                StringBuilder builder = new StringBuilder();
                for (int i = 1; i <= columnCount; i++) {
                    builder.append(rs.getString(i)).append("\t");
                }
                System.out.println(builder);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void MultiInsert(String sql, Integer number) {

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = conn.createStatement();
        ) {
            for (int i = 0; i < number; i++) {
                String target = sql.replaceAll("\\?", "'" + i + "'");
                // 5. 执行SQL语句
                statement.executeUpdate(target);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
```

### （三）PreparedStatementTest

```java
package week5.question_10.jdbc04_test;

import java.io.IOException;
import java.sql.*;

/**
 * @author lvzb31988
 * @description: TODO
 * @date 2022/4/1
 */
public class PreparedStatementTest {
    private static String DRIVER;
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    private static final String HIKARI_FILE_PATH = "hikari.properties";
    static {
        try {
            DRIVER = JdbcUtils.getPropertyValue(HIKARI_FILE_PATH,"driver");
            URL = JdbcUtils.getPropertyValue(HIKARI_FILE_PATH,"url");
            USER = JdbcUtils.getPropertyValue(HIKARI_FILE_PATH,"user");
            PASSWORD = JdbcUtils.getPropertyValue(HIKARI_FILE_PATH,"password");
            // 初始化参数时就进行驱动预加载
            Class.forName(DRIVER);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 简单的查询sql语句
     *
     * @param sql 查询sql
     * @throws ClassNotFoundException
     */
    public static void select(String sql, Integer[] intArr, String[] clonumArr) {
        // 1. 加载驱动类
        // Class.forName(DRIVER);
        // 2. 使用DriverManager 获取数据库连接 其中 返回的 Connection 就代表了Java程序和数据库的连接
        // 3. 不同数据库的URL写法需要查询驱动文档
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             // 4. 使用 Connection 来创建一个Statement对象
             /**
              * Statement 有三种执行SQL语句的方法
              * 1. execute() 可执行任何SQL语句返回一个 boolean值 -> 如果第一个执行结果是 ResultSet 返回true 否则返回false
              * 2. executeQuery() 可执行 select 语句 -> 返回查询到的结果集
              * 3. executeUpdate() 用于执行 DML 语句 -> 返回一个整数 代表被SQL语句影响的记录数
              */
             PreparedStatement statement = conn.prepareStatement(sql);
             // 5. 执行SQL语句
             ResultSet rs = statement.executeQuery();
        ) {
            if (null != intArr && null != clonumArr) {
                // in 查询只能这么干，真不友好！
                for (int i = 0; i < intArr.length; i++) {
                    int index = i;
                    statement.setString(index + 1, clonumArr[index]);
                }
            }
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                StringBuilder builder = new StringBuilder();
                for (int i = 1; i <= columnCount; i++) {
                    builder.append(rs.getString(i)).append("\t");
                }
                System.out.println(builder);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void MultiInsert(String sql, Integer number) {

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = conn.prepareStatement(sql);
        ) {
            for (int i = 0; i < number; i++) {
                statement.setString(1, "'" + i + "'");
                statement.setString(2, "'" + i + "'");
                // 5. 执行SQL语句
                statement.executeUpdate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static void batchInsert(String[] sqlArr) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // 保存当前自动提交的状态
            boolean autoCommit = conn.getAutoCommit();
            // 关闭自动提交，就是开启事务 如果不设置，默认是开启自动提交的，不需要显示执行commit操作
            // 如果吧下面1、2、3注释下的代码注释掉，那么批量操作就不会有事务支持，会出现部分成功部分失败！
            // 1
            conn.setAutoCommit(false);
            try (Statement statement = conn.createStatement()) {
                for (String sql : sqlArr) {
                    statement.addBatch(sql);
                }
                statement.executeBatch();
                // 2
                conn.commit();
                // 3
                conn.setAutoCommit(autoCommit);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

```

### （四）DuridDataSourceTest

```java
package week5.question_10.jdbc04_test;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

/**
 * @author lvzb31988
 * @description: TODO
 * @date 2022/4/1
 */
public class DuridDataSourceTest {
    public static DruidDataSource ds;

    public static final String QUERY_ALL = "select * from bsc_dict_info_test";
    private static final String HIKARI_FILE_PATH = "hikari.properties";
    private static final String FILE_PATH = "middleware.properties";

    static {
        try {
            ds = (DruidDataSource) DruidDataSourceFactory.createDataSource(JdbcUtils.getProperty(FILE_PATH));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

```

### （五）HikariDataSourceTest

```java
package week5.question_10.jdbc04_test;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;

/**
 * @author lvzb31988
 * @description: TODO
 * @date 2022/4/1
 */
public class HikariDataSourceTest {
    public static final HikariDataSource ds;

    public static final String QUERY_ALL = "select * from bsc_dict_info_test";
    private static final String HIKARI_FILE_PATH = "hikari.properties";

    static {
        HikariConfig config = new HikariConfig();
        try {
            config.setDriverClassName(JdbcUtils.getPropertyValue(HIKARI_FILE_PATH, "driver"));
            config.setUsername(JdbcUtils.getPropertyValue(HIKARI_FILE_PATH, "user"));
            config.setPassword(JdbcUtils.getPropertyValue(HIKARI_FILE_PATH, "password"));
            config.setJdbcUrl(JdbcUtils.getPropertyValue(HIKARI_FILE_PATH, "url"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ds = new HikariDataSource(config);
    }
}
```

## 四、运行耗时结果

> 1. 不用线程池和用了线程池的对比
> 2. 用了线程池，`Hikari`和 `Durid` 对比

### （一）单元测试 插入测试结果

```java
package week5.question_10.jdbc04_test;

import org.junit.jupiter.api.Test;
import week5.question_10.jdbc04_test.ConnectMySql;
import week5.question_10.jdbc04_test.PreparedStatementTest;
import week5.question_10.jdbc04_test.DuridDataSourceTest;
import week5.question_10.jdbc04_test.HikariDataSourceTest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author lvzb31988
 * @description: TODO
 * @date 2022/4/1
 */
class DataSourceInsertTest {

    public static final String QUERY_ALL = "select * from bsc_dict_info_test";
    private static final String ADD = "insert into bsc_dict_info_test values('bbbbbtest', '担保人/被担保人与上市公司关联关系', '999', '无关联关系', '0', 190, ?),('aaaaatest', '担保人/被担保人与上市公司关联关系', '999', '无关联关系', '0', 190, ?);";
    private static final Integer NUMBER = 1000;

    @Test
    void traditionNoPreparedStatementInsertTest() {
        long start = System.currentTimeMillis();
        ConnectMySql.MultiInsert(ADD, NUMBER);
        // 插入1000次耗时：耗时: 3907 ~ 4638ms
        System.out.println("耗时: " + (System.currentTimeMillis() - start) + "ms");
    }


    @Test
    void traditionPreparedStatementInsertTest() {
        long start = System.currentTimeMillis();
        PreparedStatementTest.MultiInsert(ADD, NUMBER);
        // 插入1000次耗时：耗时: 3822 ~ 4145ms
        System.out.println("耗时: " + (System.currentTimeMillis() - start) + "ms");
    }

    @Test
    void duridInsertTest() throws SQLException {
        long start = System.currentTimeMillis();
        Connection conn = week5.question_10.jdbc03_datasource.DuridDataSourceTest.ds.getConnection();
        // 开启事务
        conn.setAutoCommit(false);
        PreparedStatement statement = conn.prepareStatement(ADD);
        for (int i = 0; i < NUMBER; i++) {
            statement.setString(1, String.valueOf(i));
            statement.setString(2, String.valueOf(i));
            // 执行SQL语句
            statement.executeUpdate();
        }
        // 提交事务
        conn.commit();
        conn.close();
        // 插入1000次耗时：耗时: 535 ~ 543ms
        System.out.println("耗时: " + (System.currentTimeMillis() - start) + "ms");
    }

    @Test
    void hikariCPInsertTest() throws SQLException {
        long start = System.currentTimeMillis();
        Connection conn = week5.question_10.jdbc03_datasource.HikariDataSourceTest.ds.getConnection();
        // 开启事务
        conn.setAutoCommit(false);
        PreparedStatement statement = conn.prepareStatement(ADD);
        for (int i = 0; i < NUMBER; i++) {
            statement.setString(1, String.valueOf(i));
            statement.setString(2, String.valueOf(i));
            // 执行SQL语句
            statement.executeUpdate();
        }
        // 提交事务
        conn.commit();
        conn.close();
        // 插入1000次耗时：耗时: 449 ~ 479ms
        System.out.println("耗时: " + (System.currentTimeMillis() - start) + "ms");
    }

    @Test
    void duridInsertBatchTest() throws SQLException {
        long start = System.currentTimeMillis();
        Connection conn = DuridDataSourceTest.ds.getConnection();
        // 开启事务
        conn.setAutoCommit(false);
        Statement statement = conn.createStatement();
        for (int i = 0; i < NUMBER; i++) {
            statement.addBatch(ADD.replaceAll("\\?", "'" + i + "'"));
        }
        // 执行SQL语句
        statement.executeBatch();
        // 提交事务
        conn.commit();
        conn.close();
        // 批处理 插入1000次耗时：耗时: 629 ~ 861ms, 性能稍差于预编译的情况
        System.out.println("耗时: " + (System.currentTimeMillis() - start) + "ms");
    }

    @Test
    void hikariCPInsertBatchTest() throws SQLException {
        long start = System.currentTimeMillis();
        Connection conn = HikariDataSourceTest.ds.getConnection();
        // 开启事务
        conn.setAutoCommit(false);
        Statement statement = conn.createStatement();
        for (int i = 0; i < NUMBER; i++) {
            statement.addBatch(ADD.replaceAll("\\?", "'" + i + "'"));
        }
        // 执行SQL语句
        statement.executeBatch();
        // 提交事务
        conn.commit();
        conn.close();
        // 批处理 插入1000次耗时：耗时: 524 ~ 578ms, 性能稍差于预编译的情况
        System.out.println("耗时: " + (System.currentTimeMillis() - start) + "ms");
    }

    @Test
    void test() {
        // 入参是正则表达时，特殊字符需要进行转义
        String target = ADD.replaceAll("\\?", "3");
        System.out.println(target);
    }

}
```

### （二）单元测试 查询测试结果

```java
package week5.question_10.jdbc04_test;

import org.junit.jupiter.api.Test;
import week5.question_10.jdbc04_test.ConnectMySql;
import week5.question_10.jdbc04_test.PreparedStatementTest;
import week5.question_10.jdbc04_test.DuridDataSourceTest;
import week5.question_10.jdbc04_test.HikariDataSourceTest;

import java.sql.*;

/**
 * @author lvzb31988
 * @description: TODO
 * @date 2022/4/1
 */
class DataSourceSelectTest {

    public static final String QUERY_ALL = "select * from bsc_dict_info_test";
    public static final Integer NUMBER = 10000;


    @Test
    void traditionNoPreparedStatementSelectTest() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < NUMBER; i++) {
            ConnectMySql.select(QUERY_ALL);
        }
        // 数据库中2条记录数据, 查询1万次 耗时：15798ms~16701ms, 查询一万次时，实际关闭的速度太慢如果短时间内再启动查询，就各种异常,性能极差。
        System.out.println("耗时: " + (System.currentTimeMillis() - start) + "ms");
    }


    @Test
    void traditionPreparedStatementSelectTest() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < NUMBER; i++) {
            PreparedStatementTest.select(QUERY_ALL, null, null);
        }
        // 查询1万次 耗时：16092ms~16444ms, 查询一万次时，实际关闭的速度太慢如果短时间内再启动查询，就各种异常,性能极差。
        System.out.println("耗时: " + (System.currentTimeMillis() - start) + "ms");
    }

    @Test
    void duridSelectTest() throws SQLException {
        long start = System.currentTimeMillis();
        for (int i = 0; i < NUMBER; i++) {
            Connection conn = DuridDataSourceTest.ds.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(QUERY_ALL);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                StringBuilder builder = new StringBuilder();
                for (int j = 0; j < columnCount; j++) {
                    builder.append(rs.getString(j + 1)).append("\t");
                }
                System.out.println(builder);
            }
            conn.close();
        }
        // 查询1万次耗时: 2080~2186ms
        System.out.println("耗时: " + (System.currentTimeMillis() - start) + "ms");
    }

    @Test
    void hikariCPSelectTest() throws SQLException {
        long start = System.currentTimeMillis();
        for (int i = 0; i < NUMBER; i++) {
            Connection conn = HikariDataSourceTest.ds.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(QUERY_ALL);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                StringBuilder builder = new StringBuilder();
                for (int j = 0; j < columnCount; j++) {
                    builder.append(rs.getString(j + 1)).append("\t");
                }
                System.out.println(builder);
            }
            conn.close();
        }
        // 查询1万次耗时: 1892~2101ms
        System.out.println("耗时: " + (System.currentTimeMillis() - start) + "ms");
    }

}
```

## 五、耗时运行结果图

### （一）查询一万次结果对比

> 2条记录数，查询采用不用连接池的预编译和非预编译的方式查询1万次对比、`Durid `连接池、`Hikari`连接池的结果
>
> 查询一万次耗时大概结论如下：
>
> 1. 不用连接池非预编译耗时：15798ms~16701ms
> 2. 不用连接池预编译耗时：16092ms~16444ms
> 3.  采用`Durid` 连接池耗时：2080~2186ms
> 4. 采用`Hikari`连接池耗时：1892~2101ms

![在这里插入图片描述](https://img-blog.csdnimg.cn/33f6b75f34924f9ba87bacdaf2b85483.png)

![在这里插入图片描述](https://img-blog.csdnimg.cn/fad3fc0a29694a3489fe31025e4cc7a1.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATHZRaUZlbg==,size_20,color_FFFFFF,t_70,g_se,x_16)

![在这里插入图片描述](https://img-blog.csdnimg.cn/ae49c7d4988a4c5fa7326463dc8dc81b.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATHZRaUZlbg==,size_20,color_FFFFFF,t_70,g_se,x_16)

![在这里插入图片描述](https://img-blog.csdnimg.cn/52bcb4ba837344ee8691d79761e83638.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATHZRaUZlbg==,size_20,color_FFFFFF,t_70,g_se,x_16)

![在这里插入图片描述](https://img-blog.csdnimg.cn/0ba32c7aff694b24bfbd4c39eed3fcb7.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATHZRaUZlbg==,size_20,color_FFFFFF,t_70,g_se,x_16)

### （二）插入一千条数据对比

> 插入采用不用连接池的预编译和非预编译的方式插入1千条数据对比、`Durid `连接池、`Hikari`连接池的结果
>
> 插入1千条数据耗时大概结论如下：
>
> 1. 不用连接池非预编译耗时：3907 ~ 4638ms
> 2. 不用连接池预编译耗时：3822 ~ 4145ms
> 3.  采用`Durid` 连接池耗时：535 ~ 543ms
> 4. 采用`Hikari`连接池耗时：449 ~ 480ms

![在这里插入图片描述](https://img-blog.csdnimg.cn/60c8016898544c86bc278e39c0dbbae8.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATHZRaUZlbg==,size_20,color_FFFFFF,t_70,g_se,x_16)

![在这里插入图片描述](https://img-blog.csdnimg.cn/a87519f762ea4d89a092923d9f5a8bad.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATHZRaUZlbg==,size_20,color_FFFFFF,t_70,g_se,x_16)

![在这里插入图片描述](https://img-blog.csdnimg.cn/db3ad59b4d1c49a793065abf4584000e.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATHZRaUZlbg==,size_20,color_FFFFFF,t_70,g_se,x_16)

![在这里插入图片描述](https://img-blog.csdnimg.cn/550886c1b97d44ad92fa76e3307c9f88.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATHZRaUZlbg==,size_20,color_FFFFFF,t_70,g_se,x_16)

## 参考文章

[https://www.cnblogs.com/dadian/p/11936056.html](https://www.cnblogs.com/dadian/p/11936056.html)

[https://www.cnblogs.com/dadian/p/11938707.html](https://www.cnblogs.com/dadian/p/11938707.html)

[https://www.cnblogs.com/yszzu/p/10192206.html](https://www.cnblogs.com/yszzu/p/10192206.html)