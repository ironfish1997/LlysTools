package com.yitutech.olive.sdd3501.base.util.http;

import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * http请求工具类
 */
public class HttpUtil {

    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(1000, 1000, TimeUnit.SECONDS)).build();

    /**
     * 发送post请求
     *
     * @param url       请求地址
     * @param json      请求body json
     * @param sessionId sessionId
     * @return 返回结果
     * @throws IOException IOException
     */
    public static String postResponse(String url, String json, String sessionId) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Headers headers = Headers.of(
                "encoding", "utf-8",
                "Accept", "application/json",
                "Content-Type", "application/json",
                "session_id", sessionId);
        Request request = new Request.Builder().headers(headers).url(url).post(body).build();
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
}
