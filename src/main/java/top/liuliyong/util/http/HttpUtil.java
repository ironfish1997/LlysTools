package util.http;

import java.io.IOException;
import java.util.Map;

public interface HttpUtil {

    /**
     * 发送Get请求
     *
     * @param url
     * @param headerFields 请求头参数
     * @param params       查询字符串
     * @param timeout      超时时间，如果传入值小于等于0则设置为默认
     * @return
     * @throws IOException
     */static String doGet(String url, Map<String, Object> headerFields, Map<String, Object> params, Integer timeout) throws IOException {
        return null;
    }

    /**
     * 发送Post请求
     *
     * @param url
     * @param headerFields 请求头参数
     * @param bodyParams   请求体参数
     * @param timeout      超时时间，如果传入值小于等于0则设置为默认
     * @return
     * @throws IOException
     */
    static String doPost(String url, Map<String, Object> headerFields, Map<String, Object> bodyParams, Integer timeout) {
        return null;
    }

    /**
     * 发送put请求
     *
     * @param url
     * @param headerFields 请求头参数
     * @param bodyParams   请求体参数
     * @param timeout      超时时间，如果传入值小于等于0则设置为默认
     * @return
     * @throws IOException
     */
    static String doPut(String url, Map<String, Object> headerFields, Map<String, Object> bodyParams, Integer timeout) {
        return null;
    }

    /**
     * 发送Head请求，接受请求头
     *
     * @param headerFields 请求头参数
     * @param params       查询字符串
     * @param timeout      超时时间，如果传入值小于等于0则设置为默认
     * @return
     * @throws IOException
     */
    static String getHead(String url, Map<String, Object> headerFields, Map<String, Object> params, Integer timeout) {
        return null;
    }

    /**
     * 发送Delete请求，删除目标服务器的相应资源
     *
     * @param url
     * @param params  查询字符串
     * @param timeout 超时时间，如果传入值小于等于0则设置为默认
     * @return
     * @throws IOException
     */
    static String doDelete(String url, Map<String, Object> params, Integer timeout) {
        return null;
    }

    static String doPatch(String url, Map<String, Object> params) {
        return null;
    }
}
