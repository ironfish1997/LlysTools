package top.liuliyong.util.http.impl;

import com.alibaba.fastjson.JSON;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.FieldInfo;
import com.google.api.client.util.Lists;
import top.liuliyong.util.http.HttpUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/***
 * 基于google httpClient的http请求工具包
 */
public class GenericHttpUtil implements HttpUtil {
    private static final HttpRequestFactory REQUEST_FACTORY = new NetHttpTransport().createRequestFactory();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    protected GenericHttpUtil() {

    }

    public static Map<String, Object> doGet(String url, Map<String, Object> headerFields, Map<String, Object> params, Integer timeout) throws IOException {
        try {
            url = getParamsUrl(url, params);
            HttpRequest request = REQUEST_FACTORY.buildGetRequest(new GenericUrl(url));
            setRequestHeaders(headerFields, request, timeout);
            return getResponseResult(request);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }


    public static Map<String, Object> doPost(String url, Map<String, Object> headerFields, Map<String, Object> bodyParams, Integer timeout) throws IOException {
        try {
            HttpContent content = new JsonHttpContent(JSON_FACTORY, bodyParams);
            HttpRequest request = REQUEST_FACTORY.buildPostRequest(new GenericUrl(url), content);
            setRequestHeaders(headerFields, request, timeout);
            return getResponseResult(request);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static Map<String, Object> doPost(String url, Map<String, Object> headerFields, String bodyParams, Integer timeout) throws IOException {
        try {
            Map body = JSON.parseObject(bodyParams, HashMap.class);
            HttpContent content = new JsonHttpContent(JSON_FACTORY, body);
            HttpRequest request = REQUEST_FACTORY.buildPostRequest(new GenericUrl(url), content);
            setRequestHeaders(headerFields, request, timeout);
            return getResponseResult(request);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static Map<String, Object> doPut(String url, Map<String, Object> headerFields, Map<String, Object> bodyParams, Integer timeout) throws IOException {
        try {
            HttpContent content = new JsonHttpContent(JSON_FACTORY, bodyParams);
            HttpRequest request = REQUEST_FACTORY.buildPutRequest(new GenericUrl(url), content);
            setRequestHeaders(headerFields, request, timeout);
            return getResponseResult(request);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static Map<String, Object> doPut(String url, Map<String, Object> headerFields, String bodyParams, Integer timeout) throws IOException {
        try {
            Map body = JSON.parseObject(bodyParams, HashMap.class);
            HttpContent content = new JsonHttpContent(JSON_FACTORY, body);
            HttpRequest request = REQUEST_FACTORY.buildPutRequest(new GenericUrl(url), content);
            setRequestHeaders(headerFields, request, timeout);
            return getResponseResult(request);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static Map<String, Object> getHead(String url, Map<String, Object> headerFields, Map<String, Object> params, Integer timeout) throws IOException {
        try {
            if (url == null) throw new NullPointerException("URL cannot be null");
            url = getParamsUrl(url, params);
            HttpRequest request = REQUEST_FACTORY.buildHeadRequest(new GenericUrl(url));
            setRequestHeaders(headerFields, request, timeout);
            HttpResponse response = request.execute();
            if (response.getStatusCode() != 200)
                throw new IOException(response.getStatusMessage());
            return response.getHeaders();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static Map<String, Object> doDelete(String url, Map<String, Object> params, Integer timeout) throws IOException {
        try {
            url = getParamsUrl(url, params);
            HttpRequest request = REQUEST_FACTORY.buildDeleteRequest(new GenericUrl(url));
            setRequestHeaders(null, request, timeout);
            return getResponseResult(request);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static Map<String, Object> getResponseResult(HttpRequest request) throws IOException {
        HttpResponse response = request.execute();
        if (response.getStatusCode() != 200) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", response.getStatusCode());
            error.put("message", response.getStatusMessage());
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(response.getContent()));
        String s = "";
        while ((s = br.readLine()) != null) {
            sb.append(s);
        }
        JsonParser parser = JSON_FACTORY.createJsonParser(sb.toString());
        Map result = parser.parse(Map.class);
        return result;
    }

    private static String getParamsUrl(String url, Map<String, Object> params) throws UnsupportedEncodingException {
        if (params != null && params.size() != 0) {
            Iterator inter = params.entrySet().iterator();
            url += "?";
            StringBuilder urlBuilder = new StringBuilder(url);
            while (inter.hasNext()) {
                Map.Entry entry = (Map.Entry) inter.next();
                String key = java.net.URLEncoder.encode(entry.getKey() + "",
                        "utf-8");
                String val = java.net.URLEncoder.encode(entry.getValue() + "",
                        "utf-8");
                urlBuilder.append(key).append("=").append(val).append("&");
            }
            url = urlBuilder.toString();
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    private static void setRequestHeaders(Map<String, Object> headerFields, HttpRequest request, Integer timeout) {
        HttpHeaders headers = new HttpHeaders();
        //请求头中设置页面编码为utf-8
        headers.setContentEncoding("utf-8");
        if (timeout > 0) {
            request.setConnectTimeout(timeout);
            request.setReadTimeout(timeout);
        }
        if (headerFields != null && headerFields.size() != 0) {
            for (Object o : headerFields.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                String key = (String) entry.getKey();
                Object val = entry.getValue();
                FieldInfo fieldInfo = headers.getClassInfo().getFieldInfo(key);
                if (fieldInfo != null) {
                    List valList = Lists.newArrayList();
                    valList.add(val);
                    headers.set(key, valList);
                } else {
                    headers.set(key, val);
                }
            }
            request.setHeaders(headers);
        }
    }

}