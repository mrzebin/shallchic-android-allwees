package com.project.app.base;

import com.hb.basemodel.base.BaseView;

public class BasePresenter<V extends BaseView>{
    public V mView;

    public void attachView(V view){
        this.mView = view;
    }

    public void detachView(){
        if(mView != null){
            mView = null;
        }
    }

    public boolean isViewAttach(){
        return mView != null;
    }


    public interface IBasePresenter{
        void onDestoryView();
    }


}
