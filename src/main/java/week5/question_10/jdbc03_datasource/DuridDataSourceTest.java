package week5.question_10.jdbc03_datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import week5.question_10.jdbc01_protogenetic.JdbcUtils;

import java.sql.*;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/2
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

        // 耗时: 427ms的样子
        System.out.println("耗时: " + (System.currentTimeMillis() - start) + "ms");
    }


}
