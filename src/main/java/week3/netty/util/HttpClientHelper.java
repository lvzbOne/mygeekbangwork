package week3.netty.util;

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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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
//        CloseableHttpResponse response = null;
//        try {
//            response = httpclient.execute(httpGet);
//            System.out.println("see see what get this: " + response.getStatusLine());
//            HttpEntity entity = response.getEntity();
//            return EntityUtils.toString(entity, "UTF-8");
//        } finally {
//            if (null != response) {
//                response.close();
//            }
//        }
    }

    public static String postAsString(String url) throws UnsupportedEncodingException {
        String result = null;

        HttpPost httpPost = new HttpPost(url);
        // TODO: 这步操作转成请求头和请求体要如何如何灵活应用？
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

    public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
        StringBuffer buffer = null;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod(requestMethod);
            conn.connect();
            //往服务器端写内容 也就是发起http请求需要带的参数
            if (null != outputStr) {
                OutputStream os = conn.getOutputStream();
                os.write(outputStr.getBytes("utf-8"));
                os.close();
            }

            //读取服务器端返回的内容
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            buffer = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }


    public static void main(String[] args) throws IOException {

        String url = "http://www.baidu.com";
        String url1 = "http://localhost:8801";
        String url2 = "http://localhost:8802";
        String url3 = "http://localhost:8803";

        // 客户端请求 8801单线程服务：必定失败，然后会自动重新执行请求后面几次同样失败 ： Software caused connection abort: recv failed
        String resultString1 = HttpClientHelper.getAsString(url1);
        log.info("get调用8801返回结果:{}", resultString1);
        // 客户端请求 8802多线程的服务端： 第一次必定失败，然后会自动重新执行请求第二次就成功了
//        String resultString2 = HttpClientHelper.getAsString(url2);
//        log.info("get调用8802返回结果:{}", resultString2);
//        // 客户端请求 8803线程池的服务端： 第一次必定失败，然后会自动重新执行请求第二次就成功了，同多线程的结果一样
//        String resultString3 = HttpClientHelper.getAsString(url3);
//        log.info("get调用8803返回结果:{}", resultString3);

//        String resultString = HttpClientHelper.getAsString(url1);
//        log.info("get调用返回结果:{}", resultString);

//        String httpResult = HttpClientHelper.httpRequest(url, "GET", null);
//        log.info("原生 http get调用返回结果:{}", httpResult);

//        String postResult = HttpClientHelper.postAsString(url);
//        log.info("post调用返回结果:{}", postResult);
    }
}
