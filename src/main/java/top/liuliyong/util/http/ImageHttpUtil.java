package com.yitutech.olive.sdd3501.base.util.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

/**
 * @Author: Liyong.liu
 * @Date: 2019/8/29 下午7:27
 */

public class ImageHttpUtil {
    /**
     * 读取网络图片
     *
     * @param urlPath 图片url
     * @return 图片byte数组
     * @throws Exception exception
     */
    public static byte[] getImageBytes(String urlPath) throws IOException {
//        log.debug("图片的路径为:" + urlPath);
        URL url = new URL(urlPath);
        //打开链接
        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) url.openConnection();
            //设置请求方式为"GET"
            conn.setRequestMethod("GET");
            //超时响应时间为60秒
            conn.setConnectTimeout(60 * 1000);
            //通过输入流获取图片数据
            InputStream inStream = conn.getInputStream();
            //得到图片的二进制数据，以二进制封装得到数据，具有通用性
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while ((len = inStream.read(buffer)) != -1) {
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
            //关闭输入流
            inStream.close();
            byte[] data = outStream.toByteArray();
            return data;
        } catch (Exception e) {
            throw e;
        }
    }

    public static String getImageBase64(String urlPath) throws IOException {
        byte[] data = getImageBytes(urlPath);
        return Base64.getEncoder().encodeToString(data);
    }
}
