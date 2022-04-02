package week5.question_10.jdbc01_protogenetic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/1
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

    public static void main(String[] args) throws IOException {
        // 这种加载配置文件的方式真妙啊
        Properties properties = new Properties();
        InputStream ins = JdbcUtils.class.getClassLoader().getResourceAsStream("middleware.properties");
        properties.load(ins);
        System.out.println(properties.getProperty("driver"));
        System.out.println(properties.getProperty("url"));
        System.out.println(properties.getProperty("user"));
        System.out.println(properties.getProperty("password"));
    }
}
