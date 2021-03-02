package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.FreeGiftBean;
import com.project.app.contract.HomeFreeGiftKindsContract;
import com.project.app.model.HomeFreeGiftKindsModel;

public class HomeFreeGiftKindsPresenter extends BasePresenter<HomeFreeGiftKindsContract.View> implements HomeFreeGiftKindsContract.Presenter {
    private final HomeFreeGiftKindsModel model;

    public HomeFreeGiftKindsPresenter(){
        model = new HomeFreeGiftKindsModel();
    }

    @Override
    public void fetchFreeGift(int page, int pageSize, String url) {
        model.fetchFreeGift(page, pageSize, url, new BaseModelResponeListener<FreeGiftBean>() {
            @Override
            public void onSuccess(FreeGiftBean data) {
                if(mView == null){
                    return;
                }
                mView.fetchFreeGiftSuccess(data);
            }

            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.fetchFail(msg);
            }
        });
    }
}
