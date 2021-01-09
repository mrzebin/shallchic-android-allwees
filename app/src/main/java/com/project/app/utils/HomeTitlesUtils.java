package com.project.app.utils;

import android.text.TextUtils;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.utils.JsonUtils;
import com.hb.basemodel.utils.SPManager;
import com.project.app.bean.CategoryBean;

public class HomeTitlesUtils {
    private static HomeTitlesUtils mInstance;

    public static CategoryBean getAppCategorys(){
        String title = "";
        CategoryBean bean ;
        String languageId = SPManager.sGetString(Constant.SP_LOCALE_LANGUAGE);
        if(languageId.equals("ar")){
            title = SPManager.sGetString(Constant.HOME_DYNAMIC_CATEGORY_TITLES_AR);
            if(TextUtils.isEmpty(title)){
                title =  FileManager.readAssetFile(Constant.ASSERT_CATEGORY_TITLES_AR);
            }
        }else if(languageId.equals("en")){
            title = SPManager.sGetString(Constant.HOME_DYNAMIC_CATEGORY_TITLES_EN);
            if(TextUtils.isEmpty(title)){
                title =  FileManager.readAssetFile(Constant.ASSERT_CATEGORY_TITLES_EN);
            }
        }
        bean = JsonUtils.deserialize(title,CategoryBean.class);
        return bean;
    }

    public static synchronized HomeTitlesUtils getInstance(){
        if(mInstance == null){
            mInstance = new HomeTitlesUtils();
        }
        return mInstance;
    }

    public void setmCategoryBean(CategoryBean mCategoryBean) {
        if(LocaleUtil.getInstance().getLanguage().equals("ar")){
            SPManager.sPutString(Constant.HOME_DYNAMIC_CATEGORY_TITLES_AR, JsonUtils.serialize(mCategoryBean));  //分类的数据
        }else{
            SPManager.sPutString(Constant.HOME_DYNAMIC_CATEGORY_TITLES_EN, JsonUtils.serialize(mCategoryBean));  //分类的数据
        }
    }

    public void clear() {
        if(mInstance != null){
            mInstance = null;
        }
    }
}
