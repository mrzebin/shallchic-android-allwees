package com.project.app.utils;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.utils.SPManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String fetchDateMonth(long dateTime){
        SimpleDateFormat format = new SimpleDateFormat("MMM d");
        Date date = new Date(dateTime);
        return format.format(date);
    }

    public static String fetchDateHS(long dateTime){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(dateTime);
        return format.format(date);
    }

    public static void fetchSystemDayId(){
        SimpleDateFormat format = new SimpleDateFormat("dd");
        String id = format.format(new Date(System.currentTimeMillis()));
        String recodeDayId = SPManager.sGetString(Constant.SP_DIALOG_TOADY_DAY_ID);

        if(!id.equals(recodeDayId)){
            SPManager.sPutBoolean(Constant.SP_DIALOG_TODAY_RUNOOB_GIFT,false);    //不是同一天,让其弹窗显示
            SPManager.sPutBoolean(Constant.SP_DIALOG_DAILY_CHECK_UPDATE,false);   //一天弹一次
            SPManager.sPutString(Constant.SP_DIALOG_TOADY_DAY_ID,id);
        }
    }

}
