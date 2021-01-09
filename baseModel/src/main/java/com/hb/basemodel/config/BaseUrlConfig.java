package com.hb.basemodel.config;


import com.hb.basemodel.config.api.debugUrl;
import com.hb.basemodel.config.api.releaseUrl;
import com.hb.basemodel.utils.SPManager;

/**
 * create by zb
 */
public class BaseUrlConfig{
    public static boolean DEBUG      = true;
    public static String WEB_URL     = DEBUG ? debugUrl.WEB_URL_DEBUG: releaseUrl.WEB_RELEASE_URL;

    public static String getRootHost(){
        String prefixHost = "";
        if(DEBUG){
            prefixHost = DEBUG?debugUrl.URL_ROOT_DEBUG: releaseUrl.RELEASE_ROOT_URL;
        }else{
            if(SPManager.sGetBoolean(Constant.SP_INIT_LOCALE)){
                prefixHost = SPManager.sGetString("host");
                if(prefixHost.equals("")){
                    prefixHost = DEBUG?debugUrl.URL_ROOT_DEBUG: releaseUrl.RELEASE_ROOT_URL;
                }
            }else{
                prefixHost = DEBUG?debugUrl.URL_ROOT_DEBUG: releaseUrl.RELEASE_ROOT_URL;
            }
        }
        return prefixHost;
    }
}
