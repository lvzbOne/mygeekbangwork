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
class DataSourceSelectTest {

    public static final String QUERY_ALL = "select * from bsc_dict_info_test";


    @Test
    void traditionNoPreparedStatementSelectTest() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            ConnectMySql.select(QUERY_ALL);
        }
        // 查询100次耗时：476ms, 查询1000次耗时：2244~2427毫秒, 查询1万次 耗时：16264ms~16701ms, 查询一万次时，实际关闭的速度太慢如果短时间内再启动查询，就各种异常,性能极差。
        System.out.println("耗时: " + (System.currentTimeMillis() - start) + "ms");
    }


    @Test
    void traditionPreparedStatementSelectTest() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            PreparedStatementTest.select(QUERY_ALL, null, null);
        }
        // 查询100次耗时：499ms 查询1000次耗时：2376~2391毫秒，查询1万次 耗时：16264ms~16444ms, 查询一万次时，实际关闭的速度太慢如果短时间内再启动查询，就各种异常,性能极差。
        System.out.println("耗时: " + (System.currentTimeMillis() - start) + "ms");
    }

    @Test
    void duridSelectTest() throws SQLException {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
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
        for (int i = 0; i < 10000; i++) {
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
        // 22~27m 的样子 单元测试耗时:330ms, 查询100次耗时:329ms, 查询1000次耗时:644-687ms,查询1万次耗时: 2006~2101ms
        System.out.println("耗时: " + (System.currentTimeMillis() - start) + "ms");
    }


}