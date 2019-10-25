package top.liuliyong.util.http;

import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * http请求工具类
 */
public class HttpUtil {

    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");

    private static OkHttpClient okHttpClient;

    /**
     * 发送post请求
     *
     * @param url       请求地址
     * @param json      请求body json
     * @param sessionId sessionId, 无则传""
     * @return 返回结果
     * @throws IOException IOException
     */
    public static String postResponse(String url, String json, String sessionId) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Headers headers = Headers.of(
                "encoding", "utf-8",
                "Accept", "application/json",
                "Content-Type", "application/json",
                "session_id", sessionId
//                "target_cluster_id", clusterId
        );
        Request request = new Request.Builder().headers(headers).url(url).post(body).build();
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new RuntimeException("Incorrect status code + " + response.code() + " message: " + response.body().string());
        }
        return response.body().string();
    }

    public static CompletableFuture postResponseAsync(String url, String json) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        RequestBody body = RequestBody.create(JSON, json);
        Headers headers = Headers.of(
                "Content-Type", "application/json"
//                "session_id", sessionId,
//                "target_cluster_id", clusterId
        );
        Request request = new Request.Builder().headers(headers).url(url).post(body).build();
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                completableFuture.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    completableFuture.complete(response.body().string());
                } else {
                    completableFuture.completeExceptionally(new IOException("Incorrect status code + " + response.code() + " message: " + response.body().string()));
                }
            }
        });

        return completableFuture;
    }

    /**
     * 发送get请求
     *
     * @param url       请求地址
     * @param sessionId sessionId, 无则传""
     * @param clusterId 集群id, 针对FP相关接口, 无则传""
     * @return 请求结果
     * @throws IOException IOException
     */
    public static String getResponse(String url, String sessionId, String clusterId) throws IOException {
        Headers headers = Headers.of(
                "encoding", "utf-8",
                "Accept", "application/json",
                "Content-Type", "application/json",
                "session_id", sessionId,
                "target_cluster_id", clusterId);
        Request request = new Request.Builder().headers(headers).url(url).build();
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    /**
     * 发送put请求
     *
     * @param url       请求地址
     * @param json      请求body json
     * @param sessionId sessionId, 无则传""
     * @param clusterId 集群id, 针对FP相关接口, 无则传""
     * @return 返回结果
     * @throws IOException IOException
     */
    public static String putResponse(String url, String json, String sessionId, String clusterId) throws IOException {
        Headers headers = Headers.of(
                "encoding", "utf-8",
                "Accept", "application/json",
                "Content-Type", "application/json",
                "session_id", sessionId,
                "target_cluster_id", clusterId);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().headers(headers).url(url).put(body).build();
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    /**
     * 发送delete请求
     *
     * @param url       请求地址
     * @param json      请求body json
     * @param sessionId sessionId, 无则传""
     * @param clusterId 集群id, 针对FP相关接口, 无则传""
     * @return 返回结果
     * @throws IOException IOException
     */
    public static String deleteResponse(String url, String json, String sessionId, String clusterId) throws IOException {
        Headers headers = Headers.of(
                "encoding", "utf-8",
                "Accept", "application/json",
                "Content-Type", "application/json",
                "session_id", sessionId,
                "target_cluster_id", clusterId);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().headers(headers).url(url).delete(body).build();
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }
}
