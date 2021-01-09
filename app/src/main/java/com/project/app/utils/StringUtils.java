package com.project.app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class StringUtils {
    private static final SimpleDateFormat format = new SimpleDateFormat("dd,yyyy HH:mm:ss");
    private static final SimpleDateFormat mMonthFormat = new SimpleDateFormat("MM");
    private static final SimpleDateFormat mDayFormat = new SimpleDateFormat("dd");
    private static final SimpleDateFormat mHsFormat = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat mArFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

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

    public static String getEnDateFormat(long time){
       String symbol = LocaleUtil.getInstance().getLanguage();
       Date transferT =  new Date(time);
       String result = "";
       String allT   = "";
        String monthIndex  = mMonthFormat.format(transferT);
       if(symbol.equals("en")){
           allT = format.format(transferT);
           String enMonth = GetMonthEN(Integer.parseInt(monthIndex));
           result = allT + " " + enMonth;
       }else if(symbol.equals("ar")){
           allT = format.format(transferT);
//           String enMonth = getMonthAr(Integer.parseInt(monthIndex));
           String dateTime = mArFormat.format(transferT);
           result = dateTime;
       }
       return result;
    }

    public static String getEnMDFormat(long time){
        String result = "";
        String month  = "";
        String day    = "";
        String symbol = LocaleUtil.getInstance().getLanguage();

        Date transferT =  new Date(time);
        String monthIndex   = mMonthFormat.format(transferT);

        if(symbol.equals("en")){
            month =  GetMonthEN(Integer.parseInt(monthIndex));
            day   = mDayFormat.format(transferT);
            result = month + " " + day;
        }else if(symbol.equals("ar")){
            month =  getMonthAr(Integer.parseInt(monthIndex));
            day   = mDayFormat.format(transferT);
            result = month + " " + day;
        }
        return result;
    }

    public static String getHourMS(long time){
        Date transferT =  new Date(time);
        return mHsFormat.format(transferT);
    }

    private static String GetMonthEN(int InMonth){
        String strReturn;
        String strParaMonthn = "Jan_Feb_Mar_Apr_May_Jun_Jul_Aug_Sep_Oct_Nov_Dec";
        String[] strSubMonth = strParaMonthn.split("_");
        strReturn = strSubMonth[InMonth - 1];
        return strReturn;
    }

    private static String getMonthAr(int inMonth){
        String month = "";
        if(inMonth ==1){
            month = "يناير";
        }else if(inMonth == 2){
            month = "فبراير";
        }else if(inMonth == 3){
            month = "مارس";
        }else if(inMonth == 4){
            month = "ابريل";
        }else if(inMonth == 5){
            month = "مايو";
        }else if(inMonth == 6){
            month = "يونيو";
        }else if(inMonth == 7){
            month = "يوليو";
        }else if(inMonth == 8){
            month = "اغسطس";
        }else if(inMonth == 9){
            month = "سبتمبر";
        }else if(inMonth == 10){
            month = "ٱكتوبر";
        }else if(inMonth == 11){
            month = "نوفمبر";
        }else if(inMonth == 12){
            month = "ديسمبر";
        }

        return month;
    }

}
