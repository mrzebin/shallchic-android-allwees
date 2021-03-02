package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.AppLocaleBean;
import com.project.app.bean.CountryCropBean;

public interface SwitchLanguateContract {

    interface Model{
        void fetchCountryList(BaseModelResponeListener listener);
        void fetchLocaleInfo(String countryId, BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void fetchCountryListSuccess(CountryCropBean result);
        void fetchCountryListFail(String failReason);
        void fetchLocaleInofSuccess(AppLocaleBean bean);
        void fetchLocaleInfoFail(String msg);
    }

    interface Presenter{
        void fetchCountryList();
        void fetchLocaleInfo(String countryId);
    }


}
