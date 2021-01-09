package com.project.app.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.qmuiteam.qmui.arch.QMUIFragmentActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.project.app.utils.StatusBarUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;


public abstract class BaseFragmentActivity extends QMUIFragmentActivity {
    protected Context mContext;
    QMUITipDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(getLayoutId());
        ButterKnife.bind(this);

        if (registerEventBus())
            EventBus.getDefault().register(this);

        if(setStatusBarMode() == 0){
            QMUIStatusBarHelper.setStatusBarLightMode(this);
        }else{
            QMUIStatusBarHelper.setStatusBarDarkMode(this);
        }

        if(!hideStatuBar()){
            StatusBarUtils.setStatusBarView(this,getStatuBar());
        }
        initView();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected boolean registerEventBus() {
        return false;
    }

    protected abstract boolean hideStatuBar();

    protected abstract View getStatuBar();

    protected abstract int setStatusBarMode();

    @Override
    protected void onDestroy() {
        if (registerEventBus())
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
