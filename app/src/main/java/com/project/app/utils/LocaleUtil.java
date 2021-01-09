package com.project.app.utils;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.utils.JsonUtils;
import com.hb.basemodel.utils.LoggerUtil;
import com.hb.basemodel.utils.SPManager;
import com.project.app.bean.AppLocaleBean;
import com.project.app.bean.CountryCropBean;

/**
 * 国家语言管理工具类
 */
public class LocaleUtil {
    private static LocaleUtil sInstance;

    public AppLocaleBean localeBean;

    private LocaleUtil() {
        localeBean = new AppLocaleBean();
        updateLocaleInfo();
    }

    public static synchronized LocaleUtil getInstance() {
        if (sInstance == null) {
            sInstance = new LocaleUtil();
        }
        return sInstance;
    }

    private void updateLocaleInfo() {
        AppLocaleBean.CountryBean country = new AppLocaleBean.CountryBean();
        AppLocaleBean.CurrencyBean currency = new AppLocaleBean.CurrencyBean();
        country.setLanguage(SPManager.sGetString(Constant.SP_LOCALE_LANGUAGE));
        country.setNameEn(SPManager.sGetString(Constant.SP_LOCALE_COUNTRY));
        country.setRegion(SPManager.sGetString(Constant.SP_LOCALE_REGION));
        currency.setAbbr(SPManager.sGetString(Constant.SP_LOCALE_ABBR));
        currency.setSymbol(SPManager.sGetString(Constant.SP_LOCALE_SYMBOL));

        localeBean.setCdn(SPManager.sGetString(Constant.SP_LOCALE_CDN));
        localeBean.setHost(SPManager.sGetString(Constant.SP_LOCALE_HOST));
        localeBean.setIp(SPManager.sGetString(Constant.SP_LOCALE_IP));
        localeBean.setCountry(country);
        localeBean.setCurrency(currency);
    }

    //刷新国旗
    public void setDefaultCountry(CountryCropBean.CountryItem item){
        SPManager.sPutInt(Constant.SP_LOCALE_COUNTRY_FLAG_COLUMN,Integer.valueOf(item.getColNum()));
        SPManager.sPutInt(Constant.SP_LOCALE_COUNTRY_FLAG_ROW,Integer.valueOf(item.getRowNum()));
    }


    public void setLocaleBean(AppLocaleBean localeBean) {
        SPManager.sPutString(Constant.SP_LOCALE_LANGUAGE,localeBean.getCountry().getLanguage());
        SPManager.sPutString(Constant.SP_LOCALE_COUNTRY,localeBean.getCountry().getNameEn());
        SPManager.sPutString(Constant.SP_LOCALE_NAME_EN,localeBean.getCountry().getNameEn());
        SPManager.sPutString(Constant.SP_LOCALE_REGION,localeBean.getCountry().getRegion());
        SPManager.sPutString(Constant.SP_LOCALE_IP,localeBean.getIp());
        SPManager.sPutString(Constant.SP_LOCALE_HOST,localeBean.getHost());
        SPManager.sPutString(Constant.SP_LOCALE_CDN,localeBean.getCdn());
        SPManager.sPutString(Constant.SP_LOCALE_ABBR,localeBean.getCurrency().getAbbr());
        SPManager.sPutString(Constant.SP_LOCALE_SYMBOL,localeBean.getCurrency().getSymbol());
        SPManager.sPutString(Constant.SP_LOCALE_INFO, JsonUtils.serialize(localeBean));  //初始化语言环境


        if(localeBean.getCountry().getRegion().equals("53950000")){        //美国
            SPManager.sPutInt(Constant.SP_LOCALE_COUNTRY_FLAG_COLUMN,7);
            SPManager.sPutInt(Constant.SP_LOCALE_COUNTRY_FLAG_ROW,2);
        }else if(localeBean.getCountry().getRegion().equals("2610000")){  //阿拉伯
            SPManager.sPutInt(Constant.SP_LOCALE_COUNTRY_FLAG_COLUMN,8);
            SPManager.sPutInt(Constant.SP_LOCALE_COUNTRY_FLAG_ROW,2);
        }else if(localeBean.getCountry().getRegion().equals("")){         //沙特
            SPManager.sPutInt(Constant.SP_LOCALE_COUNTRY_FLAG_COLUMN,9);
            SPManager.sPutInt(Constant.SP_LOCALE_COUNTRY_FLAG_ROW,2);
        }
        updateLocaleInfo();
    }

    public String getCoungryName(){
        return SPManager.sGetString(Constant.SP_LOCALE_NAME_EN);
    }

    public String getLanguage(){
        return SPManager.sGetString(Constant.SP_LOCALE_LANGUAGE);
    }

    public String getCountry(){
        return SPManager.sGetString(Constant.SP_LOCALE_COUNTRY);
    }

    public String getSymbole(){
        return SPManager.sGetString(Constant.SP_LOCALE_SYMBOL);
    }


    public String getHost(){
        return SPManager.sGetString(Constant.SP_LOCALE_HOST);
    }


    public String getRegion(){
        return SPManager.sGetString(Constant.SP_LOCALE_REGION);
    }

    public void setLocalCustomNameEn(String nameEn) {
        SPManager.sPutString(Constant.SP_LOCALE_CUSTOM_REGIONNAME,nameEn);
    }

    public String getLocalCustomNameEn(){
        return SPManager.sGetString(Constant.SP_LOCALE_CUSTOM_REGIONNAME);
    }

    public void setLocaleCountryFlagColumn(String colNum) {
        SPManager.sPutInt(Constant.SP_LOCALE_COUNTRY_FLAG_COLUMN,Integer.valueOf(colNum));
    }

    public int getLocaleCountryFlagColumn(){
        return  SPManager.sGetInt(Constant.SP_LOCALE_COUNTRY_FLAG_COLUMN);
    }

    public void setLocaleCountryFlagRow(String rowNum) {
        SPManager.sPutInt(Constant.SP_LOCALE_COUNTRY_FLAG_ROW,Integer.valueOf(rowNum));
    }

    public int getLocaleCountryFlagRow(){
        return  SPManager.sGetInt(Constant.SP_LOCALE_COUNTRY_FLAG_ROW);
    }

    //国家区号
    public void setPhoneAreaCode(String areaCode){
        SPManager.sPutString(Constant.SP_LOCALE_AREA_CODE,areaCode);
        LoggerUtil.i("国家区号:" + areaCode);
    }

    public String getPhoneAreaCode(){
        return SPManager.sGetString(Constant.SP_LOCALE_AREA_CODE);
    }

    //自定义选择国家货币
    public void setLocalCustomSymbol(String region) {
        if(region.equals("48650000") || region.equals("2610000")){
            SPManager.sPutString(Constant.SPLOCALE_CUSTOM_SYMBOL,"SAR");
        }else{
            SPManager.sPutString(Constant.SPLOCALE_CUSTOM_SYMBOL,"$");
        }
    }
    //获取选择国家货币
    public String getLocalCustomSymbole(){
        return SPManager.sGetString(Constant.SPLOCALE_CUSTOM_SYMBOL);
    }

    public void setLocaleCustomCountryFlagCloumn(String colNum) {
        SPManager.sPutInt(Constant.SP_LOCALE_CUSTOM_FLAG_COLMU,Integer.valueOf(colNum));
    }

    public int getLocaleCustomCountryFlagCloumn() {
        return SPManager.sGetInt(Constant.SP_LOCALE_CUSTOM_FLAG_COLMU);
    }

    public void setLocaleCustomCountryFlagRow(String rowNum) {
        SPManager.sPutInt(Constant.SP_LOCALE_CUSTOM_FLAG_ROW,Integer.valueOf(rowNum));
    }

    public int getLocaleCustomCountryFlagRow() {
        return SPManager.sGetInt(Constant.SP_LOCALE_CUSTOM_FLAG_ROW);
    }

    public void setLocaleCustom(boolean isCustom){
        SPManager.sPutBoolean(Constant.SP_LOCALE_ISCOUSTOM,isCustom);
    }

    public boolean getLocaleCustom(){
        return SPManager.sGetBoolean(Constant.SP_LOCALE_ISCOUSTOM);
    }

    public void setLocalCustomRegion(String region) {
        SPManager.sPutString(Constant.SP_LOCALE_CUSTOM_REGION,region);
    }

    public String getLocalCustomRegion(){
        return SPManager.sGetString(Constant.SP_LOCALE_CUSTOM_REGION);
    }

    public void setCustomPhoneAreaCode(String phoneAreaCode) {
        SPManager.sPutString(Constant.SP_LOCALE_CUSTOM_AREA_CODE,phoneAreaCode);
    }

    public String getCustomPhoneAreaCode() {
        return SPManager.sGetString(Constant.SP_LOCALE_CUSTOM_AREA_CODE);
    }

    public void clearLocaleInfo() {
//        SPManager.sPutString(Constant.SP_LANGUAGE,"");   注意这里不要清掉,不然到登录界面会出现英文
//        SPManager.sPutString(Constant.SP_COUNTRY,"");
//        SPManager.sPutString(Constant.SP_NAME_EN,"");
//        SPManager.sPutString(Constant.SP_REGION,"");
//        SPManager.sPutString(Constant.SP_IP,"");
//        SPManager.sPutString(Constant.SP_HOST,"");
//        SPManager.sPutString(Constant.SP_CDN,"");
//        SPManager.sPutString(Constant.SP_ABBR,"");
//        SPManager.sPutString(Constant.SP_SYMBOL,"");
        SPManager.sPutBoolean(Constant.SP_INIT_LOCALE,false);
        SPManager.sPutBoolean(Constant.SP_LOCALE_ISCOUSTOM,false);
        SPManager.sPutString(Constant.SPLOCALE_CUSTOM_SYMBOL,"");
    }
}
