package com.project.app.utils;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;
import com.hb.basemodel.config.BaseUrlConfig;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.utils.SPManager;
import com.project.app.MyApp;

import java.util.HashMap;
import java.util.Map;

/**
 * af传递事件的方法
 */
public class AppsFlyEventUtils {

    public static void sendAppInnerEvent(Map<String,Object> events){
        AppsFlyerLib.getInstance().logEvent(MyApp.mContext, AFInAppEventType.PURCHASE ,events);
    }

    /**
     * 需要传事件的自定义数据
     * @param events
     * @param eventType
     */
    public static void sendAppInnerEvent(Map<String,Object> events,String eventType){
        String deviceId = SPManager.sGetString(Constant.SP_DEVICE_MODEL);
        if(BaseUrlConfig.DEBUG){
            return;
        }
        AppsFlyerLib.getInstance().logEvent(MyApp.mContext, eventType ,events);
    }

    /**
     * 不需要传值
     * @param eventType
     */
    public static void sendAppInnerEvent(String eventType){
        if(BaseUrlConfig.DEBUG){
            return;
        }
        String deviceId = SPManager.sGetString(Constant.SP_DEVICE_MODEL);
        Map<String,Object> eventsMap = new HashMap<>();
        eventsMap.put(AFInAppEventParameterName.CUSTOMER_USER_ID,deviceId);
        AppsFlyerLib.getInstance().logEvent(MyApp.mContext, eventType ,eventsMap);
    }


}
