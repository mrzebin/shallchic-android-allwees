package com.project.app.presenter;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.utils.SPManager;
import com.project.app.base.BasePresenter;
import com.project.app.bean.AppLocaleBean;
import com.project.app.bean.CategoryBean;
import com.project.app.contract.LauncherContract;
import com.project.app.model.LauncherModel;

public class LauncherPresenter extends BasePresenter<LauncherContract.View> implements LauncherContract.Presenter {
    private final LauncherContract.Model model;

    public LauncherPresenter(){
        model = new LauncherModel();
    }

    @Override
    public void fetchNormTitles() {
        if(mView == null){
            return;
        }
        String platform = Constant.APP_PLATFROM;
        String device = SPManager.sGetString(Constant.SP_DEVICE_ID_FLAG);
        String version = SPManager.sGetString(Constant.SP_APP_VERSION);

        model.fetchNormTitles(platform, device, version, new BaseModelResponeListener<CategoryBean>() {
            @Override
            public void onSuccess(CategoryBean data) {
                mView.fetchHomeTitleSuccess(data);
            }
            @Override
            public void onFail(String msg) {
                mView.fetchLocaleInfoFail(msg);
            }
        });
    }

    @Override
    public void fetchLocaleInfo() {
        model.fetchLocaleInfo(new BaseModelResponeListener<AppLocaleBean>() {
            @Override
            public void onSuccess(AppLocaleBean data) {
                mView.fetchLocaleInofSuccess(data);
            }
            @Override
            public void onFail(String msg) {
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

}
