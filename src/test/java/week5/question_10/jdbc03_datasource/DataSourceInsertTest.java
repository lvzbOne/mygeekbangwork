package week5.question_10.jdbc03_datasource;

import org.junit.jupiter.api.Test;
import week5.question_10.jdbc01_protogenetic.ConnectMySql;
import week5.question_10.jdbc02_transcation.PreparedStatementTest;

import java.sql.*;

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
        // 插入1000次耗时：耗时: 3907 ~ 4071ms
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
        Connection conn = DuridDataSourceTest.ds.getConnection();
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
        Connection conn = HikariDataSourceTest.ds.getConnection();
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