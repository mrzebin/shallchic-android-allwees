package com.hb.basemodel.http;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.internal.$Gson$Types;
import com.hb.basemodel.base.ApplicationHolder;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.config.api.UrlConfig;
import com.hb.basemodel.utils.JsonUtils;
import com.hb.basemodel.utils.LoggerUtil;
import com.hb.basemodel.utils.SPManager;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OkHttpUtils {
    private static OkHttpUtils mInstance;
    private final OkHttpClient mOkHttpClient;
    private final Handler mDelivery;
    private final long cacheSize = 1024 * 1024 * 20;//缓存文件最大限制大小20M
    private final Cache cache;
    private boolean isDebug = true;

    private OkHttpUtils() {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        File httpCacheDirectory = new File(ApplicationHolder.getApplicationContext().getExternalCacheDir(), "hongyun");
        cache = new Cache(httpCacheDirectory, cacheSize);
        mOkHttpClient.setCache(cache);
        mDelivery = new Handler(Looper.getMainLooper());
    }

    private synchronized static OkHttpUtils getmInstance() {
        if (mInstance == null) {
            mInstance = new OkHttpUtils();
        }
        return mInstance;
    }

    /**
     * Get 方法的请求拼接
     * @param url
     * @param params
     * @return
     */
    public static String attachHttpGetParams(String url, Map<String,String> params){
        Iterator<String> keys = params.keySet().iterator();
        Iterator<String> values = params.values().iterator();
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("?");
        for (int i=0;i<params.size();i++ ) {
            String value= String.valueOf(values.next());
            stringBuffer.append(keys.next()).append("=").append(value);
            if (i!=params.size()-1) {
                stringBuffer.append("&");
            }
        }
        return url + stringBuffer.toString();
    }

    private void getRequest(String url,String type, Map<String, String> params, final ResultCallback callback){
        Request.Builder builder = new Request.Builder().url(url);
        builder.addHeader("Content-Type","application/x-www-form-urlencoded");
        builder.addHeader("x-channel",Constant.CHANNEL_ID);
        builder.addHeader("x-platform",Constant.APP_SOURCE_TYPE);
        builder.addHeader("x-appversion",SPManager.sGetString(Constant.SP_APP_VERSION));

        Request request = null;
        boolean isLogin = SPManager.sGetBoolean("login");
        String accessToken = SPManager.sGetString("access_token");

        if(isLogin){
            builder.addHeader("Authorization", "Bearer " + accessToken);
            builder.addHeader("Accept", "application/json");
            builder.addHeader("Content-Type", "application/json");
        }
        LoggerUtil.i("url:"+ url + " Authorization:" + accessToken);

        if(type.equals("0")){     //只有
            request = builder.build();
        }else if(type.equals("1")){
            for(String paramKey: params.keySet()){
                builder.addHeader(paramKey,params.get(paramKey));
            }
            request = builder.build();
        }
        deliveryResult(callback, request);
    }

    private void postRequest(String url, String type,final ResultCallback callback, List<Param> params) {
        Request request = buildPostRequest(url,type, params);
        deliveryResult(callback, request);
    }

    /**
     * delete 请求
     * @param url
     * @param type
     * @param params
     * @param callback
     */
    private void deleteRequest(String url,String type,List<Param> params,final ResultCallback callback){
        Request.Builder builder = new Request.Builder().url(url);
        RequestBody body;
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        LoggerUtil.i("===delete request " + "key:" + url);
        builder.addHeader("x-channel",Constant.CHANNEL_ID);
        builder.addHeader("x-platform",Constant.APP_SOURCE_TYPE);

        boolean isLogin = SPManager.sGetBoolean("login");
        String accessToken = SPManager.sGetString("access_token");
        if(isLogin){
            builder.addHeader("Authorization", "Bearer " + accessToken);
            builder.addHeader("Accept", "application/json");
            builder.addHeader("Content-Type", "application/json");
        }
        JSONObject paramObj = new JSONObject();
        for (Param param : params) {
            try {
                paramObj.put(param.key,param.value);
                if(isDebug){
                    LoggerUtil.i("===parma===" + "key:" + param.key + "-value:" + param.value);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        body = RequestBody.create(JSON,String.valueOf(paramObj));
        Request request = builder.delete(body).build();
        deliveryResult(callback,request);
    }

    /**
     * delete 请求
     * @param url
     * @param type
     * @param params
     * @param callback
     */
    private void putRequest(String url,String type,List<Param> params,final ResultCallback callback){
        Request.Builder builder = new Request.Builder().url(url);
        RequestBody body;
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        LoggerUtil.i("===put request " + "key:" + url);
        builder.addHeader("x-channel",Constant.CHANNEL_ID);
        builder.addHeader("x-platform",Constant.APP_SOURCE_TYPE);

        boolean isLogin = SPManager.sGetBoolean("login");
        String accessToken = SPManager.sGetString("access_token");
        if(isLogin){
            builder.addHeader("Authorization", "Bearer " + accessToken);
            builder.addHeader("Accept", "application/json");
            builder.addHeader("Content-Type", "application/json");
        }
        JSONObject paramObj = new JSONObject();
        for (Param param : params) {
            try {
                paramObj.put(param.key,param.value);
                if(isDebug){
                    LoggerUtil.i("===parma===" + "key:" + param.key + "-value:" + param.value);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        body = RequestBody.create(JSON,String.valueOf(paramObj));
        Request request = builder.put(body).build();
        deliveryResult(callback,request);
    }

    private void deliveryResult(final ResultCallback callback, Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                sendFailCallback(callback, e);
            }
            @Override
            public void onResponse(Response response) {
                try {
                    String result = response.body().string();
                    if(isDebug){
                        LoggerUtil.i("==result:" + result);
                    }
                    if (callback.mType == String.class) {
                        sendSuccessCallBack(callback, result);
                    }else{
                        Object object = JsonUtils.deserialize(result, callback.mType);
                        sendSuccessCallBack(callback, object);
                    }
                } catch (final Exception e) {
                    sendFailCallback(callback, e);
                }
            }
        });
    }

    private void sendFailCallback(final ResultCallback callback, final Exception e) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onFailure(e);
                }
            }
        });
    }

    private void sendSuccessCallBack(final ResultCallback callback, final Object obj) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    try {
                        callback.onSuccess(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private Request buildPostRequest(String url,String type, List<Param> params) {
        Request postRequest = null;
        boolean isLogin = SPManager.sGetBoolean("login");
        String accessToken = SPManager.sGetString("access_token");
        switch (type) {
            case "0": {                         //标准的post请求
                RequestBody requestBody;
                JSONObject paramObj = new JSONObject();
                Request.Builder builder = new Request.Builder().url(url);
                if (isLogin) {
                    builder.addHeader("Authorization", "Bearer " + accessToken);
                    builder.addHeader("Accept", "application/json");
                    builder.addHeader("Content-Type", "application/json");
                    LoggerUtil.i("Authorization:" + accessToken);
                } else {
                    builder.addHeader("Accept", "application/json");
                    builder.addHeader("Content-Type", "application/json");
                }
                builder.addHeader("x-channel", Constant.CHANNEL_ID);
                builder.addHeader("x-platform", Constant.APP_SOURCE_TYPE);
                
                for (Param param : params) {
                    try {
                        paramObj.put(param.key, param.value);
                        if (BaseUrlConfig.DEBUG) {
                            LoggerUtil.i("===parma===" + "key:" + param.key + "-value:" + param.value);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                LoggerUtil.i("===" + String.valueOf(paramObj));
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                requestBody = RequestBody.create(JSON, String.valueOf(paramObj));
                postRequest = builder.post(requestBody).build();
                break;
            }
            case "1": {
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                Request.Builder builder = new Request.Builder().url(url);
                RequestBody requestBody;

                if (url.equals(BaseUrlConfig.getRootHost() + UrlConfig.LOGIN_LOGIN_ULR)   || url.contains(UrlConfig.LOGIN_THIRD_FACEBOOK_URL)) {                     //登录接口
                    MediaType MEDIA_TYPE_NORMAL_FORM = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");
                    builder.addHeader("Authorization", Constant.AUTHORIZATION_TOKEN);
                    builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                    builder.addHeader("Accept", "application/json");
                    builder.addHeader("x-channel", Constant.CHANNEL_ID);
                    builder.addHeader("x-platform", Constant.APP_SOURCE_TYPE);
                    StringBuilder paramStr = new StringBuilder();
                    for (int i = 0; i < params.size(); i++) {
                        paramStr.append(params.get(i).key).append("=").append(params.get(i).value).append("&");
                    }
                    paramStr = new StringBuilder(paramStr.substring(0, paramStr.length() - 1));
                    requestBody = RequestBody.create(MEDIA_TYPE_NORMAL_FORM, paramStr.toString());
                } else {
                    if (isLogin) {
                        builder.addHeader("Authorization", "Bearer " + accessToken);
                        builder.addHeader("Accept", "application/json");
                        builder.addHeader("Content-Type", "application/json");
                        builder.addHeader("x-channel", Constant.CHANNEL_ID);
                        builder.addHeader("x-platform", Constant.APP_SOURCE_TYPE);
                    }
                    JSONObject paramObj = new JSONObject();
                    for (Param param : params) {
                        try {
                            paramObj.put(param.key, param.value);
                            if (BaseUrlConfig.DEBUG) {
                                LoggerUtil.i("===parma===" + "key:" + param.key + "-value:" + param.value);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    requestBody = RequestBody.create(JSON, String.valueOf(paramObj));
                }
                postRequest = builder.post(requestBody).build();
                break;
            }
            case "2": {
                Request.Builder builder = new Request.Builder().url(url);
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                builder.addHeader("x-channel", Constant.CHANNEL_ID);
                builder.addHeader("x-platform", Constant.APP_SOURCE_TYPE);
                if (isLogin) {
                    builder.addHeader("Authorization", "Bearer " + accessToken);
                    builder.addHeader("Accept", "application/json");
                    builder.addHeader("Content-Type", "application/json");
                }
                JSONObject paramObj = new JSONObject();
                for (Param param : params) {
                    try {
                        paramObj.put(param.key, param.value);
                        if (BaseUrlConfig.DEBUG) {
                            LoggerUtil.i("===parma===" + "key:" + param.key + "-value:" + param.value);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
//            postRequest = builder.post(requestBody).build();
                break;
            }
            case "3": {
                RequestBody requestBody;
                JSONObject paramObj = new JSONObject();
                Request.Builder builder = new Request.Builder().url(url);
                if (isLogin) {
                    builder.addHeader("Authorization", "Bearer " + accessToken);
                }
                builder.addHeader("x-channel", Constant.CHANNEL_ID);
                builder.addHeader("x-platform", Constant.APP_SOURCE_TYPE);
                builder.addHeader("Accept", "application/json");
                builder.addHeader("Content-Type", "application/json");
                Param param = params.get(0);
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                requestBody = RequestBody.create(JSON, String.valueOf(param.value));
                postRequest = builder.post(requestBody).build();
                break;
            }
            case "4": {
                RequestBody requestBody;
                Request.Builder builder = new Request.Builder().url(url);
                if (isLogin) {
                    builder.addHeader("Authorization", "Bearer " + accessToken);
                }
                try {
                    builder.addHeader("x-channel", Constant.CHANNEL_ID);
                    builder.addHeader("x-platform", Constant.APP_SOURCE_TYPE);
                    builder.addHeader("Accept", "application/json");
                    builder.addHeader("Content-Type", "application/json");
                    Param param = params.get(0);
                    JSONObject paramObj = new JSONObject(String.valueOf(param.value));
                    String xplatform = paramObj.getString("Xplatform");
                    builder.addHeader("x-platform", xplatform);
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    requestBody = RequestBody.create(JSON, String.valueOf(param.value));
                    postRequest = builder.post(requestBody).build();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return postRequest;
    }

    /**
     * get请求
     *
     * @param url      请求url
     * @param callback 请求回调
     */
    public static void get(String url, String type, Map<String,String> headParams, ResultCallback callback) {
        if(type.equals("0") && headParams.size() >0){
            url = attachHttpGetParams(url,headParams);
        }
        getmInstance().getRequest(url,type,headParams,callback);
    }

    /**
     * delete请求
     */
    public static void delete(String url,String type,List<Param> params,final ResultCallback callback){
        getmInstance().deleteRequest(url,type,params,callback);
    }

    /**
     * put请求
     */
    public static void put(String url,String type,List<Param> params,final ResultCallback callback){
        getmInstance().putRequest(url,type,params,callback);
    }


    /**
     * post请求
     *
     * @param url      请求url
     * @param callback 请求回调
     * @param params   请求参数
     */
    public static void post(String url,String type,final ResultCallback callback, List<Param> params) {
        getmInstance().postRequest(url,type, callback, params);
    }

    /**
     * http请求回调类,回调方法在UI线程中执行
     *
     * @param <T>
     */
    public static abstract class ResultCallback<T> {
        final Type mType;
        public ResultCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }
        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        /**
         * 请求成功回调
         *
         * @param response
         */
        public abstract void onSuccess(T response) throws JSONException;
        /**
         * 请求失败回调
         *
         * @param e
         */
        public abstract void onFailure(Exception e);
    }

    /**
     * post请求参数类
     */
    public static class Param {
        String key;
        Object value;

        public Param() {
        }
        public Param(String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }
}
