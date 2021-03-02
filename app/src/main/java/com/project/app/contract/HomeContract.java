package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.AppUpdateBean;
import com.project.app.bean.CartBuyDataBean;
import com.project.app.bean.CategoryBean;
import com.project.app.bean.CountryCropBean;
import com.project.app.bean.MarketGiftInfoBean;

public interface HomeContract {

    interface Model{
        void checkAppersion(BaseModelResponeListener listener);
        void fetchCountryList(BaseModelResponeListener listener);
        void fetchCartData(BaseModelResponeListener listener);
        void fetchFreeGiftStatus(BaseModelResponeListener listener);
        void fetchContactInfo(BaseModelResponeListener listener);
        void bindMiuiRegistId(String registId, boolean isBind, BaseModelResponeListener listener);
        void fetchHomePopularTitles(BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void fetchUserCartData(CartBuyDataBean result);
        void fetchSuccess(CategoryBean result);
        void fetchFail(String failReason);
        void fetchAppVersionSuccess(AppUpdateBean result);
        void fetchCountrysSuccess(CountryCropBean result);
        void fetchFreeGiftsStatusSuccess(MarketGiftInfoBean result);
        void fetchContackInfoSuccess(String result);
        void bindMiuiPushIdSuccess();
        void fetchHomePopularTitlesSuccess(CategoryBean bean);
    }

    interface Presenter{
        void fetchFreeGiftStatus();
        void fetchCountryList();
        void checkAppersion();
        void fetchCartData();
        void fetchContactInfo();
        void bindMiuiPushId(String registId,boolean isBind);
        void fetchHomePopularTitles();
    }

}
