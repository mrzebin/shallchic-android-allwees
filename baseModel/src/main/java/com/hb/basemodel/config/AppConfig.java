package com.hb.basemodel.config;

/**
 * create by zb
 */
public class AppConfig {
    public static final String RONGYUN_DEBUG_KE = "";

    public static final String RONGYUN_RELEASE = "";

    public static final String BUNDID = "com.project.app";

    public static String DEVICESOURCE = "android";

    public static String MARKET = "appstore";

    public static String SOURCETYPE = "andorid";

    public static String RONGYUN_KEY = BaseUrlConfig.DEBUG ? RONGYUN_DEBUG_KE:RONGYUN_RELEASE;


}
