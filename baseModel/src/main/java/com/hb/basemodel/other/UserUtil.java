package com.hb.basemodel.other;

import android.content.Context;

import com.hb.basemodel.base.BaseUserInfo;
import com.hb.basemodel.base.LocalUserInfo;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.utils.SPManager;

public class UserUtil {
    private static UserUtil sInstance;

    /**
     * user info
     */
    private final LocalUserInfo userInfo;
    private final BaseUserInfo baseUserInfo;

    private UserUtil() {
        userInfo = new LocalUserInfo();
        baseUserInfo = new BaseUserInfo();
        updateUserInfo();
        updateDetailUserInfo();
    }

    public static synchronized UserUtil getInstance() {
        if (sInstance == null) {
            sInstance = new UserUtil();
        }
        return sInstance;
    }

    public boolean isLogin() {
        return SPManager.sGetBoolean("login",false);
    }

    /**
     * Describe：获得user info 对象
     */
    public LocalUserInfo getUserInfo() {
        return userInfo;
    }


    /**
     * 获取BaseUserInfo
     */
    public BaseUserInfo getBaseUserInfo(){
        return baseUserInfo;
    }

    /**
     * 清空登录状态
     */
    public void loginKill(Context context) {
        SPManager.sGetBoolean("login", false);
        userInfo.clear();
    }

    public void outLogin(Context context) {
        SPManager.sGetBoolean("login", false);
        userInfo.clear();
        baseUserInfo.clear();
        loginLocalUserOut();
        loginOutBaseUserOut();
        SPManager.sPutInt(Constant.SP_SAVE_CART_GOODS_NUM,0);
//        WebUtil.removeCookie(context);
    }

    public void login() {
        SPManager.sPutBoolean("login", true);
    }

    private boolean checkLogin(){
        return SPManager.sGetBoolean("login", false);
    }

    /**
     * Describe：刷新userInfo 注意 这里的userinfo 从此类获取 不要自己new
     */
    public void setUserInfo(LocalUserInfo userInfo) {
        SPManager.sPutBoolean("login",userInfo.isLogin());
        SPManager.sPutString("access_token",userInfo.getAccess_token());
        SPManager.sPutString("token_type",userInfo.getToken_type());
        SPManager.sPutString("expires_in",userInfo.getExpires_in());
        SPManager.sPutString("scope",userInfo.getScope());
        SPManager.sPutString("userUuid",userInfo.getUserUuid());
        SPManager.sPutString("code",userInfo.getCode());
        SPManager.sPutString("username",userInfo.getUsername());
        updateUserInfo();
    }

    public String getUserAccessToken(){
        return SPManager.sGetString("access_token");
    }

    public void setUserLoginInfo(BaseUserInfo userInfo){
        SPManager.sPutString("uuid",userInfo.getUuid());
        SPManager.sPutString("email",userInfo.getEmail());
        SPManager.sPutString("firstName",userInfo.getFirstName());
        SPManager.sPutString("lastName",userInfo.getLastName());
        SPManager.sPutString("shareCode",userInfo.getShareCode());
        SPManager.sPutString("sourceType",userInfo.getShareCode());
        SPManager.sPutString("gender",userInfo.getGender());
        SPManager.sPutInt("identityVerify",userInfo.getIdentityVerify());
        SPManager.sPutInt("emailVerified",userInfo.getEmailVerified());
        SPManager.sPutInt("phoneVerified",userInfo.getPhoneVerified());
        SPManager.sPutString("createdAt",userInfo.getCreatedAt());
        SPManager.sPutString("updatedAt",userInfo.getUpdatedAt());
        SPManager.sPutString("cartUuid",userInfo.getCartUuid());
        SPManager.sPutInt("cartCount",userInfo.getCartCount());
        SPManager.sPutString("deviceSource",userInfo.getDeviceSource());
        SPManager.sPutString("deviceId",userInfo.getDeviceId());
        SPManager.sPutString("deviceModel",userInfo.getDeviceModel());
        SPManager.sPutString("systemVersion",userInfo.getSystemVersion());
        SPManager.sPutString("market",userInfo.getMarket());
        SPManager.sPutString("bundleId",userInfo.getBundleId());
        SPManager.sPutString("appName",userInfo.getAppName());
        SPManager.sPutString("appVersion",userInfo.getAppVersion());
        SPManager.sPutString("points",userInfo.getPoints());
        SPManager.sPutString("cashs",userInfo.getCashs());
        SPManager.sPutBoolean("third",userInfo.isThird());
        SPManager.sPutBoolean("hasOrder",userInfo.isHasOrder());
        SPManager.sPutString("promoCode",userInfo.getPromoCode());
    }

    private void updateDetailUserInfo(){
        baseUserInfo.setUuid(SPManager.sGetString("uuid"));
        baseUserInfo.setEmail(SPManager.sGetString("email"));
        baseUserInfo.setFirstName(SPManager.sGetString("firstName"));
        baseUserInfo.setLastName(SPManager.sGetString("lastName"));
        baseUserInfo.setShareCode(SPManager.sGetString("shareCode"));
        baseUserInfo.setSourceType(SPManager.sGetString("sourceType"));
        baseUserInfo.setGender(SPManager.sGetString("gender"));
        baseUserInfo.setIdentityVerify(SPManager.sGetInt("identityVerify"));
        baseUserInfo.setEmailVerified(SPManager.sGetInt("emailVerified"));
        baseUserInfo.setPhoneVerified(SPManager.sGetInt("phoneVerified"));
        baseUserInfo.setCreatedAt(SPManager.sGetString("createdAt"));
        baseUserInfo.setUpdatedAt(SPManager.sGetString("updatedAt"));
        baseUserInfo.setCartUuid(SPManager.sGetString("cartUuid"));
        baseUserInfo.setCartCount(SPManager.sGetInt("cartCount"));
        baseUserInfo.setDeviceSource(SPManager.sGetString("deviceSource"));
        baseUserInfo.setDeviceId(SPManager.sGetString("deviceId"));
        baseUserInfo.setDeviceModel(SPManager.sGetString("deviceModel"));
        baseUserInfo.setSystemVersion(SPManager.sGetString("systemVersion"));
        baseUserInfo.setMarket(SPManager.sGetString("market"));
        baseUserInfo.setBundleId(SPManager.sGetString("bundleId"));
        baseUserInfo.setAppName(SPManager.sGetString("appName"));
        baseUserInfo.setAppVersion(SPManager.sGetString("appVersion"));
        baseUserInfo.setPoints(SPManager.sGetString("points"));
        baseUserInfo.setCashs(SPManager.sGetString("cashs"));
        baseUserInfo.setThird(SPManager.sGetBoolean("third"));
        baseUserInfo.setHasOrder(SPManager.sGetBoolean("hasOrder"));
        baseUserInfo.setPromoCode(SPManager.sGetString("promoCode"));
    }

    private void updateUserInfo() {
        userInfo.setAccess_token(SPManager.sGetString("access_token"));
        userInfo.setToken_type(SPManager.sGetString("token_type"));
        userInfo.setExpires_in(SPManager.sGetString("expires_in"));
        userInfo.setScope(SPManager.sGetString("scope"));
        userInfo.setUserUuid(SPManager.sGetString("userUuid"));
        userInfo.setCode(SPManager.sGetString("code"));
        userInfo.setUsername(SPManager.sGetString("username"));
    }

    private void loginLocalUserOut(){
        SPManager.sPutBoolean("login",false);
        SPManager.sPutString("access_token","");
        SPManager.sPutString("token_type","");
        SPManager.sPutString("expires_in","");
        SPManager.sPutString("scope","");
        SPManager.sPutString("userUuid","");
        SPManager.sPutString("code","");
        SPManager.sPutString("username","");
    }

    private void loginOutBaseUserOut(){
        SPManager.sPutString("uuid","");
        SPManager.sPutString("email","");
        SPManager.sPutString("firstName","");
        SPManager.sPutString("lastName","");
        SPManager.sPutString("shareCode","");
        SPManager.sPutString("sourceType","");
        SPManager.sPutString("gender","");
        SPManager.sPutInt("identityVerify",0);
        SPManager.sPutInt("emailVerified",0);
        SPManager.sPutInt("phoneVerified",0);
        SPManager.sPutString("createdAt","");
        SPManager.sPutString("updatedAt","");
        SPManager.sPutString("cartUuid","");
        SPManager.sPutInt("cartCount",0);
        SPManager.sPutString("deviceSource","");
        SPManager.sPutString("deviceId","");
        SPManager.sPutString("deviceModel","");
        SPManager.sPutString("systemVersion","");
        SPManager.sPutString("market","");
        SPManager.sPutString("bundleId","");
        SPManager.sPutString("appName","");
        SPManager.sPutString("appVersion","");
        SPManager.sPutString("points","");
        SPManager.sPutString("cashs","");
        SPManager.sPutBoolean("third",false);
        SPManager.sPutBoolean("hasOrder",false);
        SPManager.sPutString("promoCode","");
    }



}
