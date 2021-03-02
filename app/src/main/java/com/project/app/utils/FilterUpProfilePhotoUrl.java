package com.project.app.utils;

import android.text.TextUtils;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.utils.SPManager;

import java.util.ArrayList;
import java.util.List;

public class FilterUpProfilePhotoUrl {

    /**
     * 替换一下域名
     * @param filterDatas
     * @return
     */
    public static List<String> filterS3PhotoUrl(List<String> filterDatas){
        List<String> newUrls = new ArrayList<>();
        String replaceUrl = SPManager.sGetString(Constant.SP_LOCALE_CDN);
        if(!TextUtils.isEmpty(replaceUrl)){
            for(int i=0;i<filterDatas.size();i++){
                String oldUrl = filterDatas.get(i);
                if(oldUrl.contains(".com")){
                    int comIndex = oldUrl.lastIndexOf(".com");
                    newUrls.add(replaceUrl + oldUrl.substring(comIndex+4,oldUrl.length()));
                }else{
                    return filterDatas;
                }
            }
        }else{
            return filterDatas;
        }
        return newUrls;
    }

    /**
     * 替换单张的图片域名
     */
    public static String filterS3SinglePhotoUrl(String s3Url){
        String newUrl = s3Url;
        String replaceUrl = SPManager.sGetString(Constant.SP_LOCALE_CDN);
        if(!replaceUrl.isEmpty()){
            if(s3Url.contains(".com")){
                int comIndex = s3Url.lastIndexOf(".com");
                newUrl = replaceUrl + s3Url.substring(comIndex+4,s3Url.length());
            }
        }
        return newUrl;
    }
}
