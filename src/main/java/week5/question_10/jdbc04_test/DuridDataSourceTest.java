package week5.question_10.jdbc04_test;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

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
}
