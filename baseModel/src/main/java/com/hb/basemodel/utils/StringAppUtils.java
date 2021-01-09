package com.hb.basemodel.utils;

import java.text.SimpleDateFormat;
import java.util.Random;

public class StringAppUtils {
    private static final SimpleDateFormat format = new SimpleDateFormat("dd,yyyy HH:mm:ss");
    private static final SimpleDateFormat mMonthFormat = new SimpleDateFormat("MM");
    private static final SimpleDateFormat mDayFormat = new SimpleDateFormat("dd");
    private static final SimpleDateFormat mHsFormat = new SimpleDateFormat("HH:mm:ss");

    public static String filterValidAddress(String addressLable){
        String infilterAddress = "";
        if(addressLable.contains("#")){
            int position_s = addressLable.indexOf("#");
            infilterAddress = addressLable.substring(0,position_s);
        }else{
            infilterAddress = addressLable;
        }
        return infilterAddress;
    }

    public static String filterZipCode(String addressLable){
        String zipCode = "";
        if(addressLable.contains("#")){
            int lastPosition = addressLable.lastIndexOf("#");
            zipCode = addressLable.substring(lastPosition+1,addressLable.length());
        }else{
            zipCode = addressLable;
        }
        return zipCode;
    }


    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }





}
