package week5.question_10.jdbc02_transcation;

import week5.question_10.jdbc01_protogenetic.JdbcUtils;

import java.io.IOException;
import java.sql.*;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/1
 */
public class PreparedStatementTest {
    private static String DRIVER;
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    private static final String HIKARI_FILE_PATH = "hikari.properties";
    private static final String FILE_PATH = "middleware.properties";

    /**
     * DDL
     */
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `bsc_dict_info_test` AS SELECT * FROM `bsc_dict_info`";

    /**
     * DML
     */
    private static final String ADD = "insert into bsc_dict_info_test values(?,?,?,?,?,?,?),(?,?,?,?,?,?,?);";
    private static final String QUERY = "select * from bsc_dict_info_test where dict_id in (?,?) ";
    private static final String DELETE = "delete from bsc_dict_info_test where dict_id in (?,?)";
    private static final String QUERY_ALL = "select * from bsc_dict_info_test";


    private static final String[] BATCH_ADD = {"insert into bsc_dict_info_test values('ccccc', '担保人/被担保人与上市公司关联关系', '999', '无关联关系', '0', 190, ' ');", "insert into bsc_dict_info_test values('ddddd','担保人/被担保人与上市公司关联关系', '999', '无关联关系', '0', 190, ' ');"};
    private static final String[] BATCH_ADD_ERROR = {"insert into bsc_dict_info_test values('eeeee', '担保人/被担保人与上市公司关联关系', '999', '无关联关系', '0', 190, ' ');", "insert into bsc_dict_info_test values('fffff', ,'担保人/被担保人与上市公司关联关系', '999', '无关联关系', '0', 190, ' ');"};

    static {
        try {
            DRIVER = JdbcUtils.getPropertyValue(HIKARI_FILE_PATH, "driver");
            URL = JdbcUtils.getPropertyValue(HIKARI_FILE_PATH, "url");
            USER = JdbcUtils.getPropertyValue(HIKARI_FILE_PATH, "user");
            PASSWORD = JdbcUtils.getPropertyValue(HIKARI_FILE_PATH, "password");
            // 初始化参数时就进行驱动预加载
            Class.forName(DRIVER);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化数据库连接参数
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static void initConnectionParam() throws IOException, ClassNotFoundException {
        DRIVER = JdbcUtils.getPropertyValue(HIKARI_FILE_PATH, "driver");
        URL = JdbcUtils.getPropertyValue(HIKARI_FILE_PATH, "url");
        USER = JdbcUtils.getPropertyValue(HIKARI_FILE_PATH, "user");
        PASSWORD = JdbcUtils.getPropertyValue(HIKARI_FILE_PATH, "password");

        // 初始化参数时就进行驱动预加载
        Class.forName(DRIVER);
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

    private static void insert(String sql) {
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
        ) {
            // 5. 执行SQL语句
            /// ('bbbbb', '担保人/被担保人与上市公司关联关系', '999', '无关联关系', '0', 190, ' '),('aaaaa', '担保人/被担保人与上市公司关联关系', '999', '无关联关系', '0', 190, ' ')
            statement.setString(1, "bbbbb");
            statement.setString(2, "担保人/被担保人与上市公司关联关系");
            statement.setString(3, "999");
            statement.setString(4, "无关联关系");
            statement.setString(5, "0");
            statement.setInt(6, 190);
            statement.setString(7, " ");

            statement.setString(8, "aaaaa");
            statement.setString(9, "担保人/被担保人与上市公司关联关系");
            statement.setString(10, "999");
            statement.setString(11, "无关联关系");
            statement.setString(12, "0");
            statement.setInt(13, 190);
            statement.setString(14, " ");


            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void MultiInsert(String sql, Integer number) {
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

    private static void delete(String sql) throws ClassNotFoundException {
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
        ) {
            // in 只能这么干，真不友好！
            statement.setString(1, "aaaaa");
            statement.setString(2, "bbbbb");
            // 5. 执行SQL语句
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * 批量添加，支持事务回滚
     *
     * @param sqlArr insert的sql语句数组
     */
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

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // initConnectionParam();
        insert(ADD);
        select(QUERY, new Integer[]{1, 2}, new String[]{"aaaaa", "bbbbb"});
        // 删除可以先关闭
        // delete(DELETE);
        System.out.println("=============== 分割线 抛异常事务回滚 =============");
        batchInsert(BATCH_ADD_ERROR);
        select(QUERY_ALL, null, null);
        batchInsert(BATCH_ADD);
        System.out.println("=============== 分割线 正确事务提交 =============");
        select(QUERY_ALL, null, null);
    }
}
