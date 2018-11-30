package util.http.impl;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.FieldInfo;
import com.google.api.client.util.Lists;
import util.http.HttpUtil;
import util.logging.LogWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/***
 * 基于google httpClient的http请求工具包
 */
public class GenericHttpUtil implements HttpUtil {
    private static final HttpRequestFactory REQUEST_FACTORY = new NetHttpTransport().createRequestFactory();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    public GenericHttpUtil() {
    }

    public static String doGet(String url, Map<String, Object> headerFields, Map<String, Object> params, Integer timeout) throws IOException {
        try {
            url = getParamsUrl(url, params);
            HttpRequest request = REQUEST_FACTORY.buildGetRequest(new GenericUrl(url));
            HttpHeaders headers = new HttpHeaders();
            //请求头中设置页面编码为utf-8
            headers.setContentEncoding("utf-8");
            setRequestHeaders(headerFields, request, headers);
            if (timeout > 0) {
                request.setConnectTimeout(timeout);
                request.setReadTimeout(timeout);
            }
            HttpResponse response = request.execute();
            if (response.getStatusCode() != 200) {
                return response.getStatusMessage();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getContent(), StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            recordLog(e);
            throw e;
        }
    }

    public static String doPost(String url, Map<String, Object> headerFields, Map<String, Object> bodyParams, Integer timeout) throws IOException {
        try {
            HttpContent content = new JsonHttpContent(JSON_FACTORY, bodyParams);
            HttpRequest request = REQUEST_FACTORY.buildPostRequest(new GenericUrl(url), content);
            HttpHeaders headers = new HttpHeaders();
            //请求头中设置页面编码为utf-8
            headers.setContentEncoding("utf-8");
            setRequestHeaders(headerFields, request, headers);
            if (timeout > 0) {
                request.setConnectTimeout(timeout);
                request.setReadTimeout(timeout);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.execute().getContent(), StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            recordLog(e);
            throw e;
        }
    }

    public static String doPut(String url, Map<String, Object> headerFields, Map<String, Object> bodyParams, Integer timeout) throws IOException {
        try {
            HttpContent content = new JsonHttpContent(JSON_FACTORY, bodyParams);
            HttpRequest request = REQUEST_FACTORY.buildPutRequest(new GenericUrl(url), content);
            HttpHeaders headers = new HttpHeaders();
            //请求头中设置页面编码为utf-8
            headers.setContentEncoding("utf-8");
            headers.setContentType("application/json");
            setRequestHeaders(headerFields, request, headers);
            if (timeout > 0) {
                request.setConnectTimeout(timeout);
                request.setReadTimeout(timeout);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.execute().getContent(), StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            recordLog(e);
            throw e;
        }
    }

    public static Map getHead(String url, Map<String, Object> headerFields, Map<String, Object> params, Integer timeout) throws IOException {
        try {
            if (url == null) throw new NullPointerException("URL cannot be null");
            url = getParamsUrl(url, params);
            HttpRequest request = REQUEST_FACTORY.buildHeadRequest(new GenericUrl(url));
            HttpHeaders headers = new HttpHeaders();
            //请求头中设置页面编码为utf-8
            headers.setContentEncoding("utf-8");
            headers.setContentType("application/json");
            setRequestHeaders(headerFields, request, headers);
            if (timeout > 0) {
                request.setConnectTimeout(timeout);
                request.setReadTimeout(timeout);
            }
            HttpResponse response = request.execute();
            if (response.getStatusCode() != 200)
                throw new IOException(response.getStatusMessage());
            return response.getHeaders();
        } catch (Exception e) {
            recordLog(e);
            throw e;
        }
    }

    public static String doDelete(String url, Map<String, Object> params, Integer timeout) throws IOException {
        try {
            url = getParamsUrl(url, params);
            HttpRequest request = REQUEST_FACTORY.buildDeleteRequest(new GenericUrl(url));
            if (timeout > 0) {
                request.setConnectTimeout(timeout);
                request.setReadTimeout(timeout);
            }
            HttpResponse response = request.execute();
            if (response.getStatusCode() != 200)
                throw new IOException(response.getStatusMessage());
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getContent(), StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            recordLog(e);
            throw e;
        }
    }

    private static String getParamsUrl(String url, Map<String, Object> params) throws UnsupportedEncodingException {
        if (params != null && params.size() != 0) {
            Iterator inter = params.entrySet().iterator();
            url += "?";
            StringBuilder urlBuilder = new StringBuilder(url);
            while (inter.hasNext()) {
                Map.Entry entry = (Map.Entry) inter.next();
                String key = java.net.URLEncoder.encode((String) entry.getKey(),
                        "utf-8");
                String val = java.net.URLEncoder.encode((String) entry.getValue(),
                        "utf-8");
                urlBuilder.append(key).append("=").append(val).append("&");
            }
            url = urlBuilder.toString();
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    private static void recordLog(Exception e) throws IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String errLog = String.format("%s: DoHeadError==>%s", df.format(new Date()), e.getMessage());
        System.err.println(errLog);
        LogWriter.writeLog(errLog);
    }

    private static void setRequestHeaders(Map<String, Object> headerFields, HttpRequest request, HttpHeaders headers) {
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