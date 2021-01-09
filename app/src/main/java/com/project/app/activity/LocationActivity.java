package com.project.app.activity;

import android.os.Bundle;
import android.view.View;

import com.project.app.base.BaseMvpActivity;
import com.project.app.contract.LocationContract;
import com.project.app.presenter.LocationPresenter;


/**
 * 定位
 */
public class LocationActivity extends BaseMvpActivity<LocationPresenter> implements LocationContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new LocationPresenter();
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected boolean hideStatuBar() {
        return true;
    }

    @Override
    protected View getStatuBar() {
        return null;
    }

    @Override
    protected int setStatusBarMode() {
        return 0;
    }

    @Override
    public void loginSuccess(String result) {

    }

    @Override
    public void loginFail(String failReason) {

    }

    @Override
    public void startLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {

    }
}
