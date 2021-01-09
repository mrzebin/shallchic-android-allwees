package com.hb.basemodel.utils;

import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.hb.basemodel.config.BaseUrlConfig;

/**
 * @Description：web常用方法
 */
public class WebUtil {

    /**
     * 富文本适配
     */
    public static String getHtmlData(String bodyHTML) {
        String head = "<head>"
                + "<meta name=\"viewport\" content=\"maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,initial-scale=1.0\">\n" +
                "<meta name=\"format-detection\" content=\"telephone=no,email=no,date=no,address=no\">"
                + "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }


    /**
     * 同步cookie
     */
    public static void syncCookie(Context context) {
        syncWebCookie(context, BaseUrlConfig.WEB_URL);

    }

    /**
     * 同步cookie
     *
     * @param url 地址
     */
    public static void syncWebCookie(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, "Authorization="  );
        cookieManager.setCookie(url, "userSign=patientApp");
        cookieManager.setCookie(url, "HospitalId=");
        cookieManager.setCookie(url, "statueBarHeight=" );
        String cookies = cookieManager.getCookie(url);
        LoggerUtil.e("aaa CommonUtil syncCookie line:patient webview " + url + " " + cookies);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager.getInstance().sync();
        }
    }

}
