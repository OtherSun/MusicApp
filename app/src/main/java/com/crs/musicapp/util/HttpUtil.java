package com.crs.musicapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by qf on 2016/11/25.
 */

public class HttpUtil {
    /**
     * 编码中文字符为utf-8
     *
     * @param str 非utf-8编码中文字符
     * @return
     */
    public static String getSearchSongName(String str) {
        try {
            String keyWord = URLEncoder.encode(str, "utf-8");
            return keyWord;
        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
            Log.e(CommonConst.TAG, "getSearchSongName: ");
        }
        return null;
    }

    /**
     * 回调结果的接口
     */
    public static interface OnCallBackListener {
        public void callBack(String jsonBack);
    }

    /**
     * 在子线程中下载获取json字符串，并用接口回调返回结果
     *
     * @param path     获取不同的json字符串地址
     * @param listener 接口回调
     */
    public static void getTops(final String path, final OnCallBackListener listener) {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                InputStream inputStreamFromNet = getInputStreamFromNet(path);
                String jsonBack = getJson(inputStreamFromNet);
                listener.callBack(jsonBack);
            }
        });
    }

    /**
     * 获取图片资源
     *
     * @param path 地址
     * @return Bitmap图片资源
     */
    public static Bitmap getTopBitmap(String path) {
        InputStream inputStreamFromNet = getInputStreamFromNet(path);
        if (inputStreamFromNet != null) {
            Bitmap bitmap = BitmapFactory.decodeStream(inputStreamFromNet);
            return bitmap;
        }
        return null;
    }

    /**
     * 获取json字符串
     *
     * @param inputStream 下载获取的输入流
     * @return 通过对输入流的解析获取htf-8编码的json字符串
     */
    public static String getJson(InputStream inputStream) {
        if (inputStream ==null)
            return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] byteArray = new byte[1024];
        int length = -1;
        try {
            while ((length = inputStream.read(byteArray)) != -1) {
                baos.write(byteArray, 0, length);
            }
            String jsonBack = baos.toString("utf-8");
            System.out.println(jsonBack);
            return jsonBack;
        } catch (IOException e) {
//            e.printStackTrace();
            Log.e(CommonConst.TAG, "backStream: 获取不到返回的输入流");
        }
        return null;
    }

    /**
     * 通过http协议get请求到输入流
     *
     * @param path 需要获取的字符串路径
     * @return 通过http协议获取到输入流，并返回
     */
    public static InputStream getInputStreamFromNet(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(CommonConst.CONNECT_TIME_OUT);
            connection.setDoInput(true);
            connection.connect();

            int code = connection.getResponseCode();
            Log.e(CommonConst.TAG, "code: " + code);
            if (code == HttpURLConnection.HTTP_OK) {
                InputStream streamBack = connection.getInputStream();
                return streamBack;
            }
        } catch (MalformedURLException e) {
//            e.printStackTrace();
            Log.e(CommonConst.TAG, "getInputStreamFromNet: 路径出错");
        } catch (IOException e) {
//            e.printStackTrace();
            Log.e(CommonConst.TAG, "getInputStreamFromNet: url连接出错"+path);
        }
        return null;
    }

    /**
     * 获取相应Id榜单的json字符串<p>
     * 排行榜id<p>
     * 3=欧美<p>
     * 5=内地<p>
     * 6=港台<p>
     * 16=韩国<p>
     * 17=日本<p>
     * 18=民谣<p>
     * 19=摇滚<p>
     * 23=销量<p>
     * 26=热歌<p>
     *
     * @param path  地址前缀
     * @param topId 榜单id
     * @return json字符串
     */
    public static String getTop(String path, int topId) {
        InputStream inputStreamFromNet = getInputStreamFromNet(path + topId);
        if (inputStreamFromNet != null) {
            String json = getJson(inputStreamFromNet);
            return json;
        }
        return null;
    }

    /**
     * 搜索关键字，获得相应json字符串
     *
     * @param str  搜索关键字
     * @param page 页数
     * @return json字符串
     */
    public static String search(String str, int page) {
        String path = CommonConst.SearchPath + "&keyword=" + getSearchSongName(str) + "&page=" + page;
        InputStream inputStreamFromNet = getInputStreamFromNet(path);
        String json = getJson(inputStreamFromNet);
        return json;
    }
}
