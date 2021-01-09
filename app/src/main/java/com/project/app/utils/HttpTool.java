package com.project.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.hb.basemodel.utils.LoggerUtil;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class HttpTool {
    private static final String TAG = "HttpTool";

//    public static void  requestPost(String tempUrl, HashMap<String, String> paramsMap, HttpCallbackListener listener) {
//        try {
//            String baseUrl = tempUrl;
//            //合成参数
//            StringBuilder tempParams = new StringBuilder();
//            int pos = 0;
//            for (String key : paramsMap.keySet()) {
//                if (pos > 0) {
//                    tempParams.append("&");
//                }
//                tempParams.append(String.format("%s=%s", key,  URLEncoder.encode(paramsMap.get(key),"utf-8")));
//                pos++;
//            }
//            String params =tempParams.toString();
//            LogUtils.i(TAG,"--url:" + tempUrl + "--params:" + params);
//            // 请求的参数转换为byte数组
//            byte[] postData = params.getBytes();
//            // 新建一个URL对象
//            URL url = new URL(baseUrl);
//            // 打开一个HttpURLConnection连接
//            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
//            // 设置连接超时时间
//            urlConn.setConnectTimeout(5 * 1000);
//            //设置从主机读取数据超时
//            urlConn.setReadTimeout(5 * 1000);
//            // Post请求必须设置允许输出 默认false
//            urlConn.setDoOutput(true);
//            //设置请求允许输入 默认是true
//            urlConn.setDoInput(true);
//            // Post请求不能使用缓存
//            urlConn.setUseCaches(false);
//            // 设置为Post请求
//            urlConn.setRequestMethod("POST");
//            //设置本次连接是否自动处理重定向
//            urlConn.setInstanceFollowRedirects(true);
//            // 配置请求Content-Type
//            urlConn.setRequestProperty("Content-Type", "application/json");
//            // 开始连接
//            urlConn.connect();
//            // 发送请求参数
//            DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
//            dos.write(postData);
//            dos.flush();
//            dos.close();
//            // 判断请求是否成功
//            if (urlConn.getResponseCode() == 200) {
//                // 获取返回的数据
//                String result = streamToString(urlConn.getInputStream());
//                listener.onFinish(result);
//                Log.e(TAG, "Post方式请求成功，result--->" + result);
//            } else {
//                listener.onError("失败");
//                Log.e(TAG, "Post方式请求失败");
//            }
//            // 关闭连接
//            urlConn.disconnect();
//        } catch (Exception e) {
//            Log.e(TAG, e.toString());
//        }
//    }


        public static void requestPost(String urlPath, String JsonString,HttpCallbackListener listener) {
            try {
                URL url = new URL(urlPath);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Charset", "UTF-8");
                conn.setRequestProperty("Authorization","Bearer 248aaf85-759a-4954-af33-cb0b3d90f16d");
                conn.setRequestProperty("Content-type", "application/json");
                conn.setDoOutput(true);

                conn.getOutputStream().write(JsonString.getBytes());

                if (conn.getResponseCode() == 200) {
                    InputStream is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line;
                    StringBuilder builder = new StringBuilder();
                    while ((line = reader.readLine())!=null){
                        builder.append(line);
                    }
                    String result = builder.toString();
                    listener.onFinish(result);
                }else{
                    String result = streamToString(conn.getErrorStream());
                    listener.onError(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    private static String getParams(HashMap<String, Object> paramsMap) {
        StringBuilder result = new StringBuilder();
        for (HashMap.Entry<String, Object> entity : paramsMap.entrySet()) {
            result.append("&").append(entity.getKey()).append("=").append(entity.getValue());
        }
        LoggerUtil.i("---" + result.substring(1));
        return result.substring(1);
    }

    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return
     */
    public static  String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public  static void requestGet(String tempUrl,HashMap<String, String> paramsMap , HttpCallbackListener listener) {
        try {
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key),"utf-8")));
                pos++;
            }

            String requestUrl = tempUrl + "?" + tempParams.toString();
            // 新建一个URL对象
            URL url = new URL(requestUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接主机超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // 设置是否使用缓存  默认是true
            urlConn.setUseCaches(true);
            // 设置为Post请求
            urlConn.setRequestMethod("GET");
            //urlConn设置请求头信息
            //设置请求中的媒体类型信息。
            urlConn.setRequestProperty("Content-Type", "application/json");
            //设置客户端与服务连接类型
            urlConn.addRequestProperty("Connection", "Keep-Alive");
            // 开始连接
            urlConn.connect();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                String result = streamToString(urlConn.getInputStream());
                listener.onFinish(result);
                Log.i(TAG, "Get方式请求成功，result--->" + result);
            } else {
                Log.i(TAG, "Get方式请求失败:" + urlConn.getResponseCode());
                listener.onError("Get方式请求异常");
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

     public static boolean isNetworkConnected(Context context) {
        if (context != null) {
             ConnectivityManager mConnectivityManager = (ConnectivityManager) context
            .getSystemService(Context.CONNECTIVITY_SERVICE);
             NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
             if (mNetworkInfo != null) {
                 return mNetworkInfo.isAvailable();
                 }
            }
         return false;
    }

}
