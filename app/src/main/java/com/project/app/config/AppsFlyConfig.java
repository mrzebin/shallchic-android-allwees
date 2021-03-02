package com.project.app.config;

public class AppsFlyConfig {
    public static final String AF_EVENT_APP_OPEN = "app_opened";
    public static final String AF_EVENT_PURCHASE = "purchase";
    public static final String AF_EVENT_SEARCH   = "search";
    public static final String AF_EVENT_ADD_TO_CART = "add_to_cart";
    public static final String AF_EVENT_CATEGORY_HOME = "home_category";   //点击首页的分类
    public static final String AF_EVENT_BANNER_HOME = "banner_home";       //点击首页banner图
    public static final String AF_EVENT_SEARCH_BUTTON = "search_button";   //点击首页的分类

    public static final String AF_EVENT_NEW_USER_GIFT_MORE = "new_user_gift_more";
    public static final String AF_EVENT_NEW_USER_GIFT_VIEW_MORE = "new_user_gift_view_more";
    public static final String AF_EVENT_NEW_USER_GIFT_GOODS = "new_user_gift_goods";
    public static final String AF_EVENT_NEW_USER_GIFT_COUPON_GET = "new_user_gift_coupon_get";
    public static final String AF_EVENT_NEW_USER_GIFT_APPLY = "new_user_gift_apply";  //礼物选择应用
    public static final String AF_EVENT_NEW_USER_GIFT_BELOW5 = "new_user_gift_below5";
    public static final String AF_EVENT_NEW_USER_GIFT_INFLUENCE = "new_user_gift_influence";

    /*pragma mark flash sale*/
    public static final String AF_EVENT_FLASH_SALE_MORE      = "flash_sale_more";
    public static final String AF_EVENT_FLASH_SALE_VIEWMORE  = "flash_sale_view_more";
    public static final String AF_EVENT_FLASH_SALE_GOODSITEM = "flash_sale_goods_item";       //点击闪购的商品
    public static final String AF_EVENT_FLASH_SALE_BUY_NOW   = "flash_sale_buy_now";
    
    /*category*/
    public static final String AF_EVENT_CATEGORY_GOODS_ITEM  = "category_goods_item";     //点击分类商品

    /*cart 点击事件*/
    public static final String AF_EVENT_CART_CONTINUE_SHOPPING = "cart_continue_shopping";  //继续购物
    public static final String AF_EVENT_CART_ADD_MORE = "cart_add_more";  //点击更多商品
    public static final String AF_EVENT_CART_COUPON_APPLY = "cart_coupon_apply"; //点击应用
    public static final String AF_EVENT_CART_CHECKOUT = "cart_checkout";  //点击checkout按钮

    /*me 模块的事件*/
    public static final String AF_EVENT_ME_HEADER_ICON   = "me_header_icon";    //我的头像按钮
    public static final String AF_EVENT_ME_ORDER_PAID    = "me_order_paid";     //订单的paid
    public static final String AF_EVENT_ME_ORDER_SHIPPED = "me_order_shipped";  //订单的shipped
    public static final String AF_EVENT_ME_ORDER_REVIEW  = "me_order_review";   //订单的review
    public static final String AF_EVENT_ME_ORDER_REFUND  = "me_order_refund";   //订单的refund
    public static final String AF_EVENT_ME_ORDER_ALL     = "me_order_all";      //订单的all
    public static final String AF_EVENT_ME_ORDER_PENDING = "me_order_pending";  //订单的pending
    public static final String AF_EVENT_ME_REWARDS       = "me_rewards";
    public static final String AF_EVENT_ME_NOTIFICATION  = "me_notification";
    public static final String AF_EVENT_ME_HELP_CENTER   = "me_help_center";      //帮助中心
    public static final String AF_EVENT_ME_SETTING       = "me_setting";

    /*shallchic cash*/
    public static final String AF_EVENT_CASH_INFO        = "cash_info";
    public static final String AF_EVENT_CASH_HISTORY     = "cash_history";

    /*setting*/
    public static final String AF_EVENT_SETTINGS_ACCOUNT          = "setting_account";
    public static final String AF_EVENT_SETTINGS_ADDRESS_MANAGER  = "setting_address_manager";
    public static final String AF_EVENT_SETTINGS_LEGALANDTERMS    = "setting_legalandterms";
    public static final String AF_EVENT_SETTINGS_CONTACTUS        = "setting_contactus";
    public static final String AF_EVENT_SETTINGS_ABOUTUS          = "setting_aboutus";
    public static final String AF_EVENT_SETTINGS_LOGOUT           = "setting_logout";

    /*contact us*/
    public static final String AF_EVENT_CONTACTUS_INS             = "contactus_ins";
    public static final String AF_EVENT_CONTACTUS_YOUTUBE         = "contactus_youtube";
    public static final String AF_EVENT_CONTACTUS_FACEBOOK        = "contactus_facebook";
    public static final String AF_EVENT_CONTACTUS_TWITTER         = "contactus_twitter";
    public static final String AF_EVENT_CONTACTUS_EMAIL           = "contactus_email";

    /*注册登录*/
    public static final String AF_EVENT_LOGIN_SIGNUP              = "login_segnup";
    public static final String AF_EVENT_LOGIN_LOGINBTN            = "login_login_buton";
    public static final String AF_EVENT_LOGIN_APPLY               = "login_apply";
    public static final String AF_EVENT_LOGIN_FACEBOOK            = "login_facebook";
    public static final String AF_EVENT_LOGIN_GOOGLE              = "login_google";
    public static final String AF_EVENT_LOGIN_FORGET_PASSWORD     = "login_forget_password";

    /*商品详情*/
    public static final String AF_EVENT_GOODSDETAIL_BUY           = "goodsdetail_buy";
    public static final String AF_EVENT_GOODSDETAIL_COLLECTION    = "goodsdetail_conllection";
    public static final String AF_EVENT_GOODSDETAIL_SHARE         = "goodsdetail_share";
    public static final String AF_EVENT_GOODSDETAIL_CART          = "goodsdetail_cart";
    public static final String AF_EVENT_GOODSDETAIL_30DAYS_RETURN = "goodsdetail_days_return";
    public static final String AF_EVENT_GOODSDETAIL_COLOR_AND_SIZE= "goodsdetail_color_and_size";
    public static final String AF_EVENT_GOODSDETAIL_DESCRIPTION   = "goodsdetail_description";
    public static final String AF_EVENT_GOODSDETAIL_DELIVERY      = "goodsdetail_delivery_option";
}
