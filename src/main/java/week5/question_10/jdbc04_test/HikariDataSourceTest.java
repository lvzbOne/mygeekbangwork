package week5.question_10.jdbc04_test;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/2
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