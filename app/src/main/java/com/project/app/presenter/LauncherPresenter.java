package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.project.app.base.BasePresenter;
import com.project.app.bean.AppLocaleBean;
import com.project.app.bean.CategoryBean;
import com.project.app.contract.LauncherContract;
import com.project.app.model.LauncherModel;
import com.project.app.net.NetStateUtils;

public class LauncherPresenter extends BasePresenter<LauncherContract.View> implements LauncherContract.Presenter {
    private final LauncherContract.Model model;

    public LauncherPresenter(){
        model = new LauncherModel();
    }

    @Override
    public void fetchNormTitles() {
        model.fetchNormTitles(new BaseModelResponeListener<CategoryBean>() {
            @Override
            public void onSuccess(CategoryBean data) {
                if(mView == null){
                    return;
                }
                mView.fetchHomeTitleSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.fetchLocaleInfoFail(msg);
            }
        });
    }

    @Override
    public void fetchLocaleInfo() {
        if(mView == null){
            return;
        }
        if(!NetStateUtils.isHasNet()){
            mView.exceptNetError();
            return;
        }
        model.fetchLocaleInfo(new BaseModelResponeListener<AppLocaleBean>() {
            @Override
            public void onSuccess(AppLocaleBean data) {
                if(mView == null){
                    return;
                }
                mView.fetchLocaleInofSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                if(mView == null){
                    return;
                }
                mView.fetchLocaleInfoFail(msg);
            }
        });
    }

    @Override
    public void fetchAdvInfo(int advType) {
        model.fetchAdvInfo(advType, new BaseModelResponeListener() {
            @Override
            public void onSuccess(Object data) {

            }
            @Override
            public void onFail(String msg) {

            }
        });
    }

    @Override
    public void onDestoryView() {
        mView = null;
        System.gc();
    }
}
