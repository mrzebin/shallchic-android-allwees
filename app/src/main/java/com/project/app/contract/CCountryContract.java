package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.CountryCropBean;

public interface CCountryContract {

    interface Model{
        void fetchCountryRList(BaseModelResponeListener listener);
    }

    interface View extends BaseView{
        void fetchSuccess(CountryCropBean result);
        void fetchFail(String failReason);
    }

    interface Presenter{
        void fetchCountryRList();
    }


}
