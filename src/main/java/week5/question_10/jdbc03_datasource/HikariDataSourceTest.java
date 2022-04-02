package week5.question_10.jdbc03_datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import week5.question_10.jdbc01_protogenetic.JdbcUtils;

import java.io.IOException;
import java.sql.*;

/**
 * 数据库连接池示例
 *
 * @author 起凤
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

    public static void main(String[] args) throws SQLException {

        long start = System.currentTimeMillis();
        Connection conn = ds.getConnection();
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(QUERY_ALL);
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < columnCount; i++) {
                builder.append(rs.getString(i + 1)).append("\t");
            }
            System.out.println(builder);
        }
        // 要记得关闭连接池
        conn.close();

        // 耗时: 204ms 的样子
        System.out.println("耗时: " + (System.currentTimeMillis() - start) + "ms");
    }
}
