package week5.question_10.jdbc04_test;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
class DataSourceInsertTest {

    public static final String QUERY_ALL = "select * from bsc_dict_info_test";
    private static final String ADD = "insert into bsc_dict_info_test values('bbbbbtest', '担保人/被担保人与上市公司关联关系', '999', '无关联关系', '0', 190, ?),('aaaaatest', '担保人/被担保人与上市公司关联关系', '999', '无关联关系', '0', 190, ?);";
    private static final String ADD_BATCH = "insert into bsc_dict_info_test(dict_id,dict_name,dict_sitm_id,dict_sitm_name,suse_flag,disp_orde,memo) values ('bbbbbtest', '担保人/被担保人与上市公司关联关系', '999', '无关联关系', '0', 190, ?)";
    private static final Integer NUMBER = 100000;
    private static final Integer MILLION = 100_0000;
    private static final Integer TEN_MILLION = 1000_0000;


    @Test
    void traditionNoPreparedStatementInsertTest() {
        long start = System.currentTimeMillis();
        ConnectMySql.MultiInsert(ADD, MILLION);
        // 插入1000次耗时：耗时: 3907 ~ 4638ms
        // 插入100W次耗时：
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
    void traditionBatchInsertTest() {
        long start = System.currentTimeMillis();
        ConnectMySql.batchInsert(ADD, MILLION);
        // 插入1000次耗时：耗时: 1134 ~ 1209
        // 插入100W次耗时：124000ms
        System.out.println("耗时: " + (System.currentTimeMillis() - start) + "ms");
    }

    @Test
    void traditionPreparedBatchInsertTest() {
        long start = System.currentTimeMillis();
        PreparedStatementTest.batchInsert(ADD_BATCH, MILLION);
        // 插入1000次耗时：耗时: 399ms~758ms
        // 插入10W次耗时：10451ms
        // 插人100W次耗时：104962ms
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
    void hikariCPInsertBatchTest() {
        long start = System.currentTimeMillis();
//        Statement statement = conn.createStatement();
        try (Connection conn = HikariDataSourceTest.ds.getConnection();
             PreparedStatement statement = conn.prepareStatement(ADD_BATCH)) {
            // 开启事务
            conn.setAutoCommit(false);
            for (int i = 0; i < NUMBER; i++) {
                /// 打开下面这行需要注释掉下面这行下的再下2行
//            statement.addBatch(ADD.replaceAll("\\?", "'" + i + "'"));
                statement.setString(1, "'" + i + "'");
                statement.addBatch();
            }
            // 执行SQL语句
            statement.executeBatch();
            // 提交事务
            conn.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        // 批处理 插入1000次耗时：耗时: 524 ~ 578ms, 性能稍差于预编译的情况 非预编译批量插入
        // 批处理 插入1000次耗时：耗时: 481 ~ 538ms, 预编译批量插入
        // 批处理 插入10W次耗时：10581ms
        // 批处理插人100W次耗时：
        System.out.println("耗时: " + (System.currentTimeMillis() - start) + "ms");
    }

    @Test
    void test() {
        // 入参是正则表达时，特殊字符需要进行转义
        String target = ADD.replaceAll("\\?", "3");
        System.out.println(target);
    }


}