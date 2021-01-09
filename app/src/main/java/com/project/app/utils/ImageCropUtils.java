package com.project.app.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.project.app.MyApp;
import com.project.app.R;

public class ImageCropUtils {

    public static Bitmap cropBitmap(Bitmap bitmap,int widthNum,int heightNum){
        Bitmap cropBitmp = null;
        int bitmap_w = bitmap.getWidth();
        int bitmap_h = bitmap.getHeight();

        int item_w = bitmap_w/10;
        int item_h = bitmap_h/3;

        cropBitmp = Bitmap.createBitmap(bitmap,item_w*widthNum,item_h*heightNum,item_w,item_h);
        return cropBitmp;
    }

    /**
     * 获取国家的国旗
     * @return
     */
    public static Bitmap getCropCountryFlag(){
        Bitmap cBitmap = BitmapFactory.decodeResource(MyApp.mContext.getResources(), R.mipmap.ic_country);
        boolean isCustom = LocaleUtil.getInstance().getLocaleCustom();
        int itemWidth  = 0;
        int itemHeight = 0;
        if(isCustom){
            itemWidth  = LocaleUtil.getInstance().getLocaleCustomCountryFlagCloumn();
            itemHeight = LocaleUtil.getInstance().getLocaleCustomCountryFlagRow();
        }else{
            itemWidth  = LocaleUtil.getInstance().getLocaleCountryFlagColumn();
            itemHeight = LocaleUtil.getInstance().getLocaleCountryFlagRow();
        }
        Bitmap flagBitmap = ImageCropUtils.cropBitmap(cBitmap,itemWidth,itemHeight);
        return flagBitmap;
    }





}
