package week2.six_question;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author 起凤
 * @description: TODO:时间来不及，只能对着参考代码敲一遍理解
 * @date 2022/3/13
 */
@Slf4j
public class OkHttpUtils {

    public static OkHttpClient client = new OkHttpClient();

    public static String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        // 这种写法是try-with-resources resources对象需要是 AutoCloseable接口的子类
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static void main(String[] args) {
        String url = "http://localhost:8801";
        String url1 = "http://www.baidu.com";

        try {
            String resultString = OkHttpUtils.run(url1);
            log.info("返回结果:{}", resultString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
