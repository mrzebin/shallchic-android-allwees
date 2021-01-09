package com.hb.basemodel.config;

public class Constant {
    
    public static final long DELAY_LOADING_TIME_OUT = 3000;
    public static final int DIGIT_ZERO = 0;
    public static final String APP_VERSION_NAME = "1.0.0";  //如果获取app版本名失败备用
    public static final String APP_SOURCE_TYPE  = "ANDROID";
    public static final String DEVICE_RESOURCE  = "android";
    public static final String APP_PLATFROM     = "android";
    public static final String APP_MARKET       = "appstore";
    public static final String HTTP_REFER       = "referer";
    public static final String APP_ROOT_DIR_NAME= "allwees";
    public static final String SHARE_CODE       = "";
    public static final String PUSH_ID          = "111";    //小米的推送id
//    public static final String SHALLCHIC_ID     = "sca_allwees_1";            //本地包
//  public static final String CHANNEL_ID       = "sca_dl_1";              //渠道号 sca_play_1  -sca_dl_1本地包
    public static final String CHANNEL_ID       = "sca_play_1";                   //渠道号google sca_play_1   --本地包:sca_dl_1
//    public static final String CHANNEL_ID       = "sca_allwees_1";

    public static final String ASSERT_CATEGORY_TITLES_EN = "categorytitles.json";
    public static final String ASSERT_CATEGORY_TITLES_AR = "categorytitles_ar.json";

    public static final String ASSERT_CATEGORY_LOCALE_EN = "locale_en.json";
    public static final String ASSERT_CATEGORY_LOCALE_AR = "locale_ar.json";


    public static final String AUTHORIZATION_TOKEN = "Basic c2hhbGxjaGljLWNsaWVudDp7bm9vcH1zaGFsbGNoaWMtc2VjcmV0";
    public static final String mGlobalThumbnailStyle = "@600w_700h_0e_1l_100p.webp";

    public static final String SP_INIT = "SP_INIT";   //判断是否第一次登录
    public static final String SP_INIT_LOCALE = "SP_INIT_LOCALE";   //初始化国家语法
    public static final String SP_LOCALE_INFO = "SP_LOCALE_INFO";    //存储国家语法信息
    public static final String SP_APP_VERSION    = "SP_APP_VERSION"; //app的版本
    public static final String SP_DEVICE_ID_FLAG = "SP_DEVICE_ID_FLAG";
    public static final String SP_DEVICE_VERSIONCODE = "SP_DEVICE_VERSIONCODE";
    public static final String SP_DEVICE_MODEL = "SP_DEVICE_MODEL";     //手机信号
    public static final String SP_DEVICE_BRAND = "SP_DEVICE_BRAND";     //设备厂商
    public static final String SP_AWS_ACCESS_TOKEN_RFD = "SP_AWS_ACCESS_TOKEN_RFD"; //获取token
    public static final String SP_SAVE_CART_GOODS_NUM = "SP_SAVE_CART_GOODS_NUM"; //记录购物车数量
    public static final String SP_DOWNLOAD_ROOT = "file_update_path";             //更新的根路径
    public static final String SP_DEFAULT_LANGUAGE_TYPE = "SP_DEFAULT_LANGUAGE_TYPE";
    public static final String SP_HAS_WRITE_STORAGE_PERMISSION = "SP_HAS_WRITE_STORAGE_PERMISSION"; //是否有读写SD权限
    public static final String SP_RECODE_SEARCH_KEYWORD = "SP_RECODE_SEARCH_KEYWORD";  //存储每次的搜索记录


    /*locale*/
    public static final String SP_LOCALE_LANGUAGE             = "SP_LOCALE_LANGUAGE";            //对应的国家语言
    public static final String SP_LOCALE_COUNTRY              = "SP_LOCALE_COUNTRY";             //对应的国家
    public static final String SP_LOCALE_SYMBOL               = "SP_LOCALE_SYMBOL";              //对应的国家货币
    public static final String SP_LOCALE_HOST                 = "SP_LOCALE_HOST";                //服务器的host
    public static final String SP_LOCALE_COUNTRY_FLAG_COLUMN  = "SP_LOCALE_COUNTRY_FLAG_COLUMN";      //对应国家的国旗,用于切图column
    public static final String SP_LOCALE_COUNTRY_FLAG_ROW     = "SP_LOCALE_COUNTRY_FLAG_ROW";         //对应国际的国旗,row
    public static final String SP_LOCALE_ABBR                 = "SP_LOCALE_ABBR";                //是不是阿拉伯
    public static final String SP_LOCALE_CDN                  = "SP_LOCALE_CDN";
    public static final String SP_LOCALE_IP                   = "SP_LOCALE_IP";
    public static final String SP_LOCALE_NAME_EN              = "SP_LOCALE_NAME_EN";
    public static final String SP_LOCALE_AREA_CODE            = "SP_LOCALE_AREA_CODE";    //对应区号码
    public static final String SP_LOCALE_REGION               = "SP_LOCALE_REGION";
    public static final String SP_LOCALE_ISCOUSTOM            = "SP_LOCALE_ISCOUSTOM";
    public static final String SP_LOCALE_CUSTOM_REGION        = "SP_LOCALE_CUSTOM_REGION";
    public static final String SP_LOCALE_CUSTOM_REGIONNAME    = "SP_LOCALE_CUSTOM_REGIONNAME";
    public static final String SP_LOCALE_CUSTOM_FLAG_COLMU    = "SP_LOCALE_CUSTOM_COLMU";
    public static final String SP_LOCALE_CUSTOM_FLAG_ROW      = "SP_LOCALE_CUSTOM_ROW";
    public static final String SP_LOCALE_CUSTOM_AREA_CODE     = "SP_LOCALE_CUSTOM_AREA_CODE";
    public static final String SPLOCALE_CUSTOM_SYMBOL         = "SPLOCALE_CUSTOM_SYMBOL";


    /**
     * 传送eventBus
     */
    public static final String EVENT_LOGIN_SUCCESS = "EVENT_LOGIN_SUCCESS";     //发送登录成功
    public static final String EVENT_REFRESH_CAR   = "EVENT_REFRESH_CAR";       //刷新cart数据
    public static final String EVNET_LOGIN_EXIT    = "EVNET_LOGIN_EXIT";       //退出登录
    public static final String EVENT_CONTINUE_SHOPPING = "EVENT_CONTINUE_SHOPPING";
    public static final String EVENT_GO_HOME_CART   = "EVENT_GO_HOME_CART";
    public static final String EVENT_REFUND_SUCCESS = "EVENT_REFUND_SUCCESS";
    public static final String EVENT_PAY_SUCCESS         = "EVENT_PAY_SUCCESS";      //支付成功
    public static final String EVENT_ADD_ADDRESS_SURE    = "EVENT_ADD_ADDRESS_SURE";  //创建地址成功
    public static final String EVENT_EXIT_APP_SURE       = "EVENT_EXIT_APP_SURE";  //退出app
    public static final String REFRESH_WISHLIST_FAVORITE = "REFRESH_WISHLIST_FAVORITE"; //收藏的数据列表
    public static final String REFRESH_AFTER_PAY_CARDATA = "REFRESH_AFTER_PAY_CAR";
    public static final String EVENT_INTENT_COUNTRY_SELECT_INFO = "EVENT_INTENT_COUNTRY_SELECT_INFO"; //选择国家的通知
    public static final String EVENT_REFRESH_ADDRESS_LIST_DATA = "EVENT_REFRESH_ADDRESS_LIST_DATA"; //地址管理创建地址成功
    public static final String EVNET_CHANGE_CURRENT_CART_NUM = "EVNET_CHANGE_CURRENT_CART_NUM";
    public static final String EVENT_SPECITY_PCITY = "EVENT_SPECITY_PCITY";
    public static final String EVENT_INPUT_PC = "EVENT_INPUT_PC";  //手动输入省份或城市
    public static final String EVENT_DEEPLINK_HOME  = "EVENT_DEEPLINK_HOME";          //scheme 跳转到home

    /**
     * banner跳转
     */
    public static final String EVENT_BANNER_SKIP_HOME = "EVENT_BANNER_SKIP_HOME";
    public static final String EVENT_BANNER_SKIP_CATEGORY = "EVENT_BANNER_SKIP_CATEGORY";
    public static final String EVENT_BANNER_SKIP_CART = "EVENT_BANNER_SKIP_CART";
    public static final String EVENT_BANNER_SKIP_MINE = "EVENT_BANNER_SKIP_MINE";
    public static final String EVENT_BANNER_SKIP_WISH = "EVENT_BANNER_SKIP_WISH";
    public static final String EVENT_BANNER_SKIP_GOODSLIST = "EVENT_BANNER_SKIP_GOODSLIST";
    public static final String EVENT_BANNER_SKIP_GOODSDETAIL = "EVENT_BANNER_SKIP_GOODSDETAIL";
    public static final String EVENT_BANNER_SKIP_LOGIN = "EVENT_BANNER_SKIP_LOGIN";
    public static final String EVENT_BANNER_SKIP_CHANGE_PS = "EVENT_BANNER_SKIP_CHANGE_PS";
    public static final String EVENT_BANNER_SKIP_ADDRESS_MANAGER = "EVENT_BANNER_SKIP_ADDRESS_MANAGER";
    public static final String EVENT_BANNER_SKIP_ADDRESS_ADD = "EVENT_BANNER_SKIP_ADDRESS_ADD";
    public static final String EVENT_BANNER_SKIP_FACEBOOK = "EVENT_BANNER_SKIP_FACEBOOK";
    public static final String EVENT_BANNER_SKIP_NOTIFICATION = "EVENT_BANNER_SKIP_NOTIFICATION";
    public static final String EVENT_BANNER_SKIP_CONTRACT_US = "EVENT_BANNER_SKIP_CONTRACT_US";
    public static final String EVENT_BANNER_SKIP_LANGUAGE_SETUP = "EVENT_BANNER_SKIP_LANGUAGE_SETUP";
    public static final String EVENT_BANNER_SKIP_REFERRAL= "EVENT_BANNER_SKIP_REFERRAL";
    public static final String EVENT_BANNER_SKIP_CASH = "EVENT_BANNER_SKIP_CASH";
    public static final String EVENT_BANNER_SKIP_REWARDS = "EVENT_BANNER_SKIP_REWARDS";
    public static final String EVENT_BANNER_SKIP_WEB = "EVENT_BANNER_SKIP_REWARDS";


    public static final String SCHEME_HOST_DEEPLINK_HOME = "allwees://home";
    public static final String SCHEME_HOST_DEEPLINK_CATEGORY = "allwees://category";
    public static final String SCHEME_HOST_DEEPLINK_CART = "allwees://shoppingcart";
    public static final String SCHEME_HOST_DEEPLINK_WISHLIST = "allwees://wish";
    public static final String SCHEME_HOST_DEEPLINK_MINE = "allwees://mine";
    public static final String SCHEME_HOST_DEEPLINK_LOGIN = "allwees://login";
    public static final String SCHEME_HOST_DEEPLINK_GOODSDETAIL = "allwees://goodsdetail";
    public static final String SCHEME_HOST_DEEPLINK_ADDRESS = "allwees://address";
    public static final String SCHEME_HOST_DEEPLINK_CASH = "allwees://cash";
    public static final String SCHEME_HOST_DEEPLINK_REWARDS = "allwees://rewards";
    public static final String SCHEME_HOST_DEEPLINK_ORDERHISTORY = "allwees://orderhistory";
    public static final String SCHEME_HOST_DEEPLINK_ORDERHISTORYDETAIL = "allwees://orderhistorydetail";
    public static final String SCHEME_HOST_DEEPLINK_WEBVIEW = "allwees://webview";
    public static final String SCHEME_HOST_DEEPLINK_CATEGORY_ITEM = "allwees://goodslist";   //分类

    /**
     * 首页商品标题
     */
    public static final String HOME_DYNAMIC_CATEGORY_TITLES_EN = "HOME_DYNAMIC_CATEGORY_TITLES_EN";
    public static final String HOME_DYNAMIC_CATEGORY_TITLES_AR = "HOME_DYNAMIC_CATEGORY_TITLES_AR";

    /*
    * credit pay
    * */
    public static final String PAYMENT_CREDIT_URL_STATUS = "https://secure.oceanpayment.com/gateway/sendPay";




}
