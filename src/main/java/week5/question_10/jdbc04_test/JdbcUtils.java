package week5.question_10.jdbc04_test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/2
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
