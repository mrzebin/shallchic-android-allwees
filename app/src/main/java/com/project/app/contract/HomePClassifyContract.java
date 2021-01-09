package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.ActionFreeOneBean;
import com.project.app.bean.ClassifyListBean;
import com.project.app.bean.HomeBannerBean;
import com.project.app.bean.HomeFLashSaleBean;
import com.project.app.bean.HomeFreeGiftBean;

public interface HomePClassifyContract {

    interface Model{
        void fetchFreeGift(BaseModelResponeListener listener);
        void fetchGoodsInfo(int page,int size,String no,BaseModelResponeListener listener);
        void fetchBannerInfo(String url,BaseModelResponeListener listener);
        void fetchFlashSale(int page,int size,BaseModelResponeListener listener);
        void fetchActionFreeOne(BaseModelResponeListener listener);
        void fetchHomePopularMax(BaseModelResponeListener listener);
    }

    interface View extends BaseView{
        void fetchFreeGiftSuccess(HomeFreeGiftBean result);
        void fetchCategoryList(ClassifyListBean result);
        void fetchBannerSuccess(HomeBannerBean result);
        void fetchFlashSaleSuccess(HomeFLashSaleBean result);
        void fetchFlashSaleFail(String result);
        void fetchActionFreeOneSuccess(ActionFreeOneBean result);
        void fetchHomePopularMaxSuccess(String result);
        void fetchFail(String failReason);
        void fetchNetWorkState(boolean unknowNet);
    }

    interface Presenter{
        void fetchFreeGift();
        void fetchGoodsInfo(int page,int size,String no);
        void fetchBannerInfo();
        void fetchFlashSale(int page,int size);
        void fetchActionFreeOne();    //买一送一活动
        void fetchHomePopularMax();
    }

}
