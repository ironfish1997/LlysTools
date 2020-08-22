package top.liuliyong.util;

import com.alibaba.fastjson.JSON;
import okhttp3.*;

import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * http请求工具类
 */
public class HttpUtil {

    private static final MediaType JSONTYPE = MediaType.parse("application/json;charset=utf-8");

    private static final Headers headers = Headers.of(
            "encoding", "utf-8",
            "Accept", "application/json",
            "Content-Type", "application/json");

    private static final MyTrustManager mMyTrustManager = new MyTrustManager();

    private static final OkHttpClient okHttpClient = getTrustAllClient();


    /**
     * 实现X509TrustManager接口
     */
    private static class MyTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static OkHttpClient getTrustAllClient() {
        OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
        mBuilder.sslSocketFactory(createSSLSocketFactory(), mMyTrustManager)
                .hostnameVerifier(new TrustAllHostnameVerifier())
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(1000, 1000, TimeUnit.SECONDS)).build();
        return mBuilder.build();
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{mMyTrustManager}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        return ssfFactory;
    }

    /**
     * 实现HostnameVerifier接口(信任所有的 https 证书。)
     */
    private static class TrustAllHostnameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }


    /**
     * 发送post请求
     *
     * @param url  请求地址
     * @param json 请求body json
     * @return 返回结果
     * @throws IOException IOException
     */
    public static String postResponse(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSONTYPE, json);
        Request request = new Request.Builder().headers(headers).url(url).post(body).build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    /**
     * 发送put请求
     *
     * @param url  请求地址
     * @param json 请求body json
     * @return 返回结果
     * @throws IOException IOException
     */
    public static String putResponse(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSONTYPE, json);
        Request request = new Request.Builder().headers(headers).url(url).put(body).build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    /**
     * 发送delete请求
     *
     * @param url  请求地址
     * @param json 请求body json
     * @return 返回结果
     * @throws IOException IOException
     */
    public static String deleteResponse(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSONTYPE, json);
        Request request = new Request.Builder().headers(headers).url(url).delete(body).build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    public static String postResponse(String url, Map<String, Object> formData) throws IOException {
        MultipartBody.Builder multipart = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        for (Map.Entry<String, Object> entry : formData.entrySet()) {
            multipart.addFormDataPart(entry.getKey(), JSON.toJSONString(entry.getValue()));
        }
        Headers headers = Headers.of(
                "encoding", "utf-8",
                "Accept", "application/json",
                "Content-Type", "multipart/form-data");
        Request request = new Request.Builder().headers(headers).url(url).post(multipart.build()).build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    /**
     * 发送get请求
     *
     * @param url 请求地址
     * @return 请求结果
     * @throws IOException IOException
     */
    public static String getResponse(String url, String sessionId) throws IOException {
        Headers headers = Headers.of(
                "encoding", "utf-8",
                "Accept", "application/json",
                "Content-Type", "application/json",
                "session_id", sessionId);
        Request request = new Request.Builder().headers(headers).url(url).build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    /**
     * 获取图片
     *
     * @param url 请求地址
     * @return 请求结果
     * @throws IOException IOException
     */
    public static byte[] getImage(String url, String sessionId) throws IOException {
        Headers headers = Headers.of(
                "encoding", "utf-8",
                "Accept", "image/jpeg");
        Request request = new Request.Builder().url(url).method("GET", null).build();
        Response response = okHttpClient.newCall(request).execute();
        InputStream imageStream = response.body().byteStream();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = imageStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.flush();
        outStream.close();
        imageStream.close();
        byte[] res = outStream.toByteArray();
        return res;
    }

    /**
     * 获取图片base64
     *
     * @param url
     * @param sessionId
     * @return
     * @throws IOException
     */
    public static String getImageBase64(String url, String sessionId) throws IOException {
        return Base64.getEncoder().encodeToString(getImage(url, sessionId));
    }
}
