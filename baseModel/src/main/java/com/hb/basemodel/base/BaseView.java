package com.hb.basemodel.base;

/**
 * create by zb
 */
public interface BaseView {
    void startLoading();
    void stopLoading();
    void showErrorTip(String msg);
}
