package week5.question_10.jdbc04_test;

import java.io.IOException;
import java.sql.*;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/2
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
            DRIVER = JdbcUtils.getPropertyValue(HIKARI_FILE_PATH, "driver");
            URL = JdbcUtils.getPropertyValue(HIKARI_FILE_PATH, "url");
            USER = JdbcUtils.getPropertyValue(HIKARI_FILE_PATH, "user");
            PASSWORD = JdbcUtils.getPropertyValue(HIKARI_FILE_PATH, "password");
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

    public static void batchInsert(String sql, Integer number) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = conn.createStatement();
        ) {
            // 开启事务
            conn.setAutoCommit(false);
            for (int i = 0; i < number; i++) {
                statement.addBatch(sql.replaceAll("\\?", "'" + i + "'"));
            }
            // 执行SQL语句
            statement.executeBatch();
            // 提交事务
            conn.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
