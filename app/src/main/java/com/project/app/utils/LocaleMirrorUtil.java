package com.project.app.utils;

/**
 * 判断获取对象
 */
public class LocaleMirrorUtil {

    //判断是不是阿拉伯国家
    public static boolean isAbbr() {
        boolean isAbbrCountry = false;
        String currentRegion = LocaleUtil.getInstance().getRegion();
        if(currentRegion.equals("48650000") || currentRegion.equals("2610000")){
            isAbbrCountry = true;
        }else{
            isAbbrCountry = false;
        }
        return isAbbrCountry;
    }

}
