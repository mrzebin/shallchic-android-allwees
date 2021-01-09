package com.project.app.ui;

public class ClickValidUtil {
    private static final long vInterval = 500;
    private static long RclickTime = 0;

    public static boolean clickLoop(){
        long clickTime = System.currentTimeMillis();
        if(clickTime - RclickTime < vInterval){
            return false;
        }else{
            RclickTime = clickTime;
            return true;
        }
    }
}
