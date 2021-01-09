package com.hb.basemodel.base;

/**
 * create by zb
 */
public class BasePresenter<V extends BaseView> {
    protected V mView;

    public void attactView(V view){
        this.mView = view;
    }

    public void detachView(){
        this.mView = null;
    }

    public boolean isAttachView(){
        return mView != null;
    }


}
