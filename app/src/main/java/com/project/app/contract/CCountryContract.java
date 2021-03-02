package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.AppLocaleBean;
import com.project.app.bean.CountryCropBean;

public interface CCountryContract {

    interface Model{
        void fetchCountryRList(BaseModelResponeListener listener);
        void fetchLocaleInfo(String countryId, BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void fetchSuccess(CountryCropBean result);
        void fetchFail(String failReason);
        void fetchLocaleInofSuccess(AppLocaleBean bean);
        void fetchLocaleInfoFail(String msg);
    }

    interface Presenter{
        void fetchCountryRList();
        void fetchLocaleInfo(String countryId);
    }


}
