package com.project.app.utils;

import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;
import com.hb.basemodel.config.BaseUrlConfig;
import com.project.app.MyApp;

import java.util.Map;

/**
 * af传递事件的方法
 */
public class AppsFlyEventUtils {

    public static void sendAppInnerEvent(Map<String,Object> events){
        AppsFlyerLib.getInstance().logEvent(MyApp.mContext, AFInAppEventType.PURCHASE ,events);
    }

    public static void sendAppInnerEvent(Map<String,Object> events,String eventType){
        if(BaseUrlConfig.DEBUG){
            return;
        }
        AppsFlyerLib.getInstance().logEvent(MyApp.mContext, eventType ,events);
    }
}
