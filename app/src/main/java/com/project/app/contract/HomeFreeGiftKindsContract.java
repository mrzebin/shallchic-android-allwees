package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.bean.FreeGiftBean;

public interface HomeFreeGiftKindsContract {

    interface Model{
        void fetchFreeGift(int page, int pageSize, String url, BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void fetchFreeGiftSuccess(FreeGiftBean result);
        void fetchFail(String failReason);
    }

    interface Presenter{
        void fetchFreeGift(int page,int size,String url);
    }


}
