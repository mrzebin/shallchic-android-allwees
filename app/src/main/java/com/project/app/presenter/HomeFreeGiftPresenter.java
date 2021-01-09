package com.project.app.presenter;

import com.project.app.base.BasePresenter;
import com.project.app.contract.HomeFreeGiftContract;
import com.project.app.model.HomeFreeGiftModel;

public class HomeFreeGiftPresenter extends BasePresenter<HomeFreeGiftContract.View> implements HomeFreeGiftContract.Presenter {
    private final HomeFreeGiftModel model;

    public HomeFreeGiftPresenter(){
        model = new HomeFreeGiftModel();
    }

    @Override
    public void fetchFreeGift(int page, String no, String url) {
        
    }
}
