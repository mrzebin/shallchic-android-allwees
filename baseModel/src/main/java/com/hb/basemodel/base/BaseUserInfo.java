package com.hb.basemodel.base;

/**
 * 用户信息
 */
public class BaseUserInfo {
    String uuid;
    String email;
    String firstName;
    String lastName;
    String shareCode;
    String sourceType;
    String gender;
    int identityVerify;
    int emailVerified;
    int phoneVerified;
    String createdAt;
    String updatedAt;
    String cartUuid;
    int cartCount;
    String deviceSource;
    String deviceId;
    String deviceModel;
    String systemVersion;
    String market;
    String bundleId;
    String appName;
    String appVersion;
    String points;
    String cashs;
    boolean third;
    boolean hasOrder;
    String promoCode;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getIdentityVerify() {
        return identityVerify;
    }

    public void setIdentityVerify(int identityVerify) {
        this.identityVerify = identityVerify;
    }

    public int getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(int emailVerified) {
        this.emailVerified = emailVerified;
    }

    public int getPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(int phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCartUuid() {
        return cartUuid;
    }

    public void setCartUuid(String cartUuid) {
        this.cartUuid = cartUuid;
    }

    public int getCartCount() {
        return cartCount;
    }

    public void setCartCount(int cartCount) {
        this.cartCount = cartCount;
    }

    public String getDeviceSource() {
        return deviceSource;
    }

    public void setDeviceSource(String deviceSource) {
        this.deviceSource = deviceSource;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getCashs() {
        return cashs;
    }

    public void setCashs(String cashs) {
        this.cashs = cashs;
    }

    public boolean isThird() {
        return third;
    }

    public void setThird(boolean third) {
        this.third = third;
    }

    public boolean isHasOrder() {
        return hasOrder;
    }

    public void setHasOrder(boolean hasOrder) {
        this.hasOrder = hasOrder;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public void clear() {
        uuid = "";
        email = "";
        firstName = "";
        lastName = "";
        shareCode = "";
        sourceType = "";
        gender = "";
        identityVerify  = 0 ;
        emailVerified = 0;
        phoneVerified  = 0;
        createdAt = "";
        updatedAt = "";
        cartUuid = "" ;
        cartCount = 0;
        deviceSource  = "";
        deviceId = "";
        deviceModel = "";
        systemVersion = "";
        market = "";
        bundleId = "";
        appName = "";
        appVersion = "";
        points = "";
        cashs = "";
        third = false ;
        hasOrder = false ;
        promoCode  = "";
    }
}
