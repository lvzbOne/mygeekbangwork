package week2.six_question;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 起凤
 * @description: HttpClient 小试牛刀
 * @date 2022/3/13
 */
@Slf4j
public class HttpClientHelper {
    public static CloseableHttpClient httpclient = HttpClients.createDefault();

    public static String getAsString(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        // 这种写法是try-with-resources resources对象需要是 AutoCloseable接口的子类
        try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
            System.out.println("see see what get this: " + response.getStatusLine());
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, "UTF-8");
        }
    }

    public static String postAsString(String url) throws UnsupportedEncodingException {
        String result = null;

        HttpPost httpPost = new HttpPost(url);
        // TODO： 这步操作转成请求头和请求体要如何如何灵活应用？
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("username", "vip"));
        nvps.add(new BasicNameValuePair("password", "secret"));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));

        try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
            System.out.println("see see what　post this: " + response.getStatusLine());
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static void main(String[] args) throws IOException {
        String url = "http://localhost:8801";
        String url1 = "http://www.baidu.com";
        String url2 = "http://www.baidu.com";


        String resultString = HttpClientHelper.getAsString(url1);
        log.info("get调用返回结果:{}", resultString);

        String postResult = HttpClientHelper.postAsString(url1);
        log.info("post调用返回结果:{}", postResult);
    }
}
