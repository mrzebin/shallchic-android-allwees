package com.hb.basemodel.config.api;

public class UrlConfig {
    //分类接口
    public static final String CATEGORY_GOODS_URL = "/api/v2/app/init";
    //注册接口
    public static final String LOGIN_REGIST_URL = "/api/v1/user/register";
    //登录接口
    public static final String LOGIN_LOGIN_ULR = "/oauth/token";
    //三方登录facebook
    public static final String LOGIN_THIRD_FACEBOOK_URL = "/api/v1/user/thirdPart/login";

    //首页的商品接口
    public static final String HOME_CLASSIFY_GOODS = "/api/v1/product/";
    public static final String HOME_CLASSIFY_POPULAR_MAX = "/api/home/mix";
    public static final String HOME_ACTION_BUY_FREE_ONE = "/api/v1/marketing/buyAndFree";  //买一送一活动

    //商品详情
    public static final String GOODS_DETAILS_URL = "/api/v1/product/info/";
    //商品关联接口
    public static final String GOODS_RELATION_URL = "/api/v1/product/related/";
    //取消收藏
    public static final String GOODS_FAVORITE_CANCEL_URL = "/api/v1/user/collection/product/cancel";
    //关注收藏
    public static final String GOODS_FAVORITE_ADD_URL = "/api/v1/user/collection/product/add";
    //wishlist
    public static final String WISH_LIST_FAVORITE_URL = "/api/v1/user/collection/product";
    //添加购物车
    public static final String GOODS_ADD_TO_CART_URL = "/api/v1/cart/add-or-reduce";
    //cart might like
    public static final String CART_MIGHT_LIKE_URL = "/api/v1/product/P01";
    //cart might data
    public static final String CART_DATA_URL = "/api/v1/cart/";
    //用户信息
    public static final String USER_INFO_URL = "/api/v1/user/me";
    //add more
    public static final String ADD_MORE_GOODS_URL = "/api/v1/product/05";
    //国家列表
    public static final String FETCH_COUNTRY_LIST = "/api/v1/countries";
    //我的cash数据
    public static final String FETCH_ME_CASH_URL  = "/api/v1/user/me/cash";
    //我的point数据
    public static final String FETCH_ME_RED_POING_URL = "/api/v1/user/me/point";
    //redeem数据
    public static final String FETCH_REDEEM_INFO_URL = "/api/v1/user/coupon/point-exchange-list";
    public static final String FETCH_REDEEM_POINT_PREFIX = "/api/v1/user/";
    //dashboard 数据
    public static final String FETCH_RECODE_DASHBOARD_A_URL = "/api/v1/user/coupons/AVAILABLE";
    public static final String FETCH_RECODE_DASHBOARD_U_URL = "/api/v1/user/coupons/USED";
    public static final String FETCH_RECODE_DASHBOARD_H_URL = "/api/v1/user/point/HIS";
    //home banner
    public static final String FETCH_HOME_BANNER_URL = "/api/v1/ads";
    public static final String FETCH_HOME_FREE_GIFT  = "/api/v1/marketing/gifts";
    public static final String FETCH_HOME_FLASH_SALE = "/api/v1/marketing/flash";

    /**
     * cart
     */
    public static final String CART_APPLY_PROMO_CODE_URL = "/api/v1/cart/";
    public static final String CART_DELETE_BUY_GOODS_URL = "/api/v1/cart/delete";
    public static final String REQUEST_ORDER_PAY_URL     = "/api/v1/user/orders";
    public static final String CART_ADDRESS_LIST_MANAGER = "/api/v1/user/shippingAddress";
    public static final String ADDRESS_DELETE_ITEM_URL   = "/api/v1/user/shippingAddress/";
    public static final String ADDRESS_CHANGE_SELECT_URL = "/api/v1/user/shippingAddress/";
    public static final String CART_USER_COUPON_URL      = "/api/v1/cart/";
    public static final String PAY_GET_PAYPAL_WEB_URL    = "/api/v1/payment/paypal/create";
    public static final String PAY_CHANNEL_CREDIT_URL    = "/api/v1/payment/app/oceanpay";
    public static final String CART_MODIFY_BUY_COUNT_URL = "/api/v1/cart/add-or-reduce";
    public static final String CART_USER_VALID_COUPON_URL= "/api/v1/user/coupon";

    /**
     * order
     */
    public static final String ORDER_COMMON_URL = "/api/v1/user/orders";
    public static final String ORDER_DETAIL_URL = "/api/v1/user/orders/";
    public static final String ORDER_LOGISTIC_TRACK_URL = "/api/v1/orders/tracking-detail/"; //物流

    /*
    * 选择省份
    */
    public static final String STATE_PROVINCE_URL = "/api/address/region/";

    /**
     * 分享链接(详情的分享链接)
     */
    public static final String APP_PREFIX_SHARE_URL = "https://api.allwees.com/api/v1/share/facebook/product/";


    /**
     * h5
     */
    public static final String HELPER_CENTER_URL = "file:///android_asset/help.html";             //帮助中心
    public static final String ABOUT_US_URL      = "file:///android_asset/aboutus.html";          //关于我们
    public static final String TERMS_OF_USE      = "file:///android_asset/termsofuse.html";
    public static final String PRIVACY_POLICY    = "file:///android_asset/privacypolicy.html";
    public static final String SHIPPING_POLICY   = "file:///android_asset/shippingPolicy.html";
    public static final String RETURN_POLICY     = "file:///android_asset/returnPolicy.html";
    public static final String PAYMENT_POLICY    = "file:///android_asset/privacypolicy.html";

    /**
     * h5-en
     */
    public static final String HELPER_CENTER_URL_EN = "file:///android_asset/help_en.html";             //帮助中心
    public static final String ABOUT_US_URL_EN      = "file:///android_asset/aboutus_en.html";          //关于我们
    public static final String TERMS_OF_USE_EN      = "file:///android_asset/termsofuse_en.html";
    public static final String PRIVACY_POLICY_EN    = "file:///android_asset/privacypolicy_en.html";
    public static final String SHIPPING_POLICY_EN   = "file:///android_asset/shippingPolicy_en.html";
    public static final String RETURN_POLICY_EN     = "file:///android_asset/returnPolicy_en.html";
    public static final String PAYMENT_POLICY_EN    = "file:///android_asset/privacypolicy_en.html";

    /**
     * 支付流程
     */
    public static final String PAYMENT_CHECK_CODE_URL = "/api/v1/payment/check_cod";
    public static final String PAYMENT_SEND_SMS = "/api/v1/sms_code/send";
    public static final String PAYMENT_PAY_ORDER = "/api/v1/payment/cod";
    public static final String PAYMENT_PAY_METHOD_CREDIT = "/api/v1/payment/oceanpay";             //信用卡支付
    public static final String PAYMENT_CANCEL_ORDER = "/api/v1/user/orders/";                      //取消订单
    public static final String PAYMENT_PAY_CREDIT_GATE_WAY_TEST = "https://secure.oceanpayment.com/gateway/service/test";   //信用卡测试支付
    public static final String PAYMENT_PAY_CREDIT_GATE_WAY_REALSE = "https://secure.oceanpayment.com/gateway/service/pay";  //信用卡正式测试支付

    /**
     * upload access token
     */
    public static final String ACCESS_UPLOAD_TOKEN = "/api/upload/token/";

    /**
     * cash
     */
    public static final String EARN_SHCALL_CASH_HISTORY = "/api/v1/user/cash/HIS";

    /*
    * search
    * */
    public static final String SEARCH_TRENDING_ITEMS_URL = "/api/v1/product/hws";    //热门搜索
    public static final String SEARCH_GOODS_RESULT_URL   = "/api/v1/product/search";


    /**
     * Gift
     */
    public static final String FREE_GIFTS_BLOW_FIVE = "/api/v1/marketing/below5";
    public static final String FREE_GIFTS_INFLUS = "/api/v1/marketing/influs";
    public static final String FREE_GIFTS_MARKETING_INFO = "/api/v1/marketing/gift/info";
    public static final String FREE_GIFTS_MARKETING_COUPON = "/api/v1/marketing/coupon/get";

    /**
     * me
     */
    public static final String LOGIN_FORGET_PASSWORD = "/api/v1/user/forget-password";
    /**
     * app 升级
     */
    public static final String APP_CHECK_VERSION = "/api/v2/app/check";

    /**
     * app 语言
     */
    public static final String FETCH_LANGUAGE_ENVIRONMENT_URL = "/api/country/resolve";

}
