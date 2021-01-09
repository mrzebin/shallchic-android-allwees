package com.project.app.utils;

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

}
