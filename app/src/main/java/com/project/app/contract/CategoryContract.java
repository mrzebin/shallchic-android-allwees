package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.CategoryAllBean;
import com.project.app.bean.CategoryBean;

public interface CategoryContract {

    interface Model{
        void fetchCateofyAll(String platform, String device, String version, BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void fetchSuccess(CategoryBean result);
        void fetchCategoryAllSuccess(CategoryAllBean result);
        void fetchFail(String failReason);
        void fetchNetWorkState(boolean isValidNet);
        void fetchExceptModel(int model);
    }

    interface Presenter{
        void fetchCateofyAll(String platform,String device,String version);
    }


}
