package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.AppUpdateBean;
import com.project.app.bean.CartBuyDataBean;
import com.project.app.bean.CategoryBean;
import com.project.app.bean.CountryCropBean;

public interface HomeContract {

    interface Model{
        void fetchUploadToken(String code,BaseModelResponeListener listener);
        void checkAppersion(BaseModelResponeListener listener);
        void fetchCountryList(BaseModelResponeListener listener);
        void fetchCartData(BaseModelResponeListener listener);
    }

    interface View extends BaseView{
        void fetchUserCartData(CartBuyDataBean result);
        void fetchSuccess(CategoryBean result);
        void fetchAccessUploadToken(String result);
        void fetchFail(String failReason);
        void fetchAppVersionSuccess(AppUpdateBean result);
        void fetchCountrysSuccess(CountryCropBean result);
    }

    interface Presenter{
        void fetchUploadToken(String code);
        void fetchCountryList();
        void checkAppersion();
        void fetchCartData();
    }

}
