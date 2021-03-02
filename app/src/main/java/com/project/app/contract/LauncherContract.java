package com.project.app.contract;

import com.hb.basemodel.base.BaseModelResponeListener;
import com.hb.basemodel.base.BaseView;
import com.project.app.base.BasePresenter;
import com.project.app.bean.AppLocaleBean;
import com.project.app.bean.CategoryBean;

public interface LauncherContract {

    interface Model{
        void fetchNormTitles(BaseModelResponeListener listener);
        void fetchLocaleInfo(BaseModelResponeListener listener);
        void fetchAdvInfo(int advType, BaseModelResponeListener listener);
    }

    interface View extends BaseView {
        void fetchLocaleInofSuccess(AppLocaleBean bean);
        void fetchLocaleInfoFail(String msg);
        void fetchHomeTitleSuccess(CategoryBean bean);
        void fetchAdvInfoSuccess();
        void fetchAdvInfoFail();
        void fetchHomeTitleFail(String msg);
        void exceptNetError();
    }

    interface Presenter extends BasePresenter.IBasePresenter {
        void fetchNormTitles();
        void fetchLocaleInfo();
        void fetchAdvInfo(int advType);
    }


}
