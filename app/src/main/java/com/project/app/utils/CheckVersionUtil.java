package com.project.app.utils;

import android.content.Context;

import com.hb.basemodel.utils.AppUtils;

/**
 * create by hb
 */
public class CheckVersionUtil {
    /**
     * 版本对比
     * @param context
     * @param onLineVersion
     * @return
     */
    public static boolean isNeedUpdate(Context context,String onLineVersion){
        boolean isResult = false;
        String localVersion =  AppUtils.getVersionName(context);
        String[] o_ver_cell = onLineVersion.split("\\.");
        String[] l_ver_cell = localVersion.split("\\.");

        if(onLineVersion.equals(localVersion)){
            return isResult;
        }
        for(int i=0;i<o_ver_cell.length;i++){
            int split_ov = Integer.parseInt(o_ver_cell[i]);
            int split_lv = Integer.parseInt(l_ver_cell[i]);
            if(split_ov > split_lv){
                return true;
            }
        }
        return isResult;
    }

}
