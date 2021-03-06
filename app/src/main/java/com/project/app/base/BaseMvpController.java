package com.project.app.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout;
import com.project.app.R;
import com.project.app.ui.dialog.LoadingDialog;
import com.project.app.ui.dialog.TipDialogUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class BaseMvpController<P extends BasePresenter> extends QMUIWindowInsetLayout {
    public final TipDialogUtil mTipDialogUtil;
    public P mPresenter;
    protected LoadingDialog dialog;
    private Context mContext;

    final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    };

    public BaseMvpController(Context context) {
        super(context);
        mContext = context;
        mTipDialogUtil = new TipDialogUtil(context,mHandler);
    }

    public BaseController.HomeControlListener mHomeControlListener;

    protected void startFragment(QMUIFragment fragment) {
        if (mHomeControlListener != null) {
            mHomeControlListener.startFragment(fragment);
        }
    }

    protected void startFragment(@NonNull Class<? extends BaseQmuiFragment> fragment) {
        if (mHomeControlListener != null) {
            mHomeControlListener.startFragment(fragment);
        }
    }

    protected void startFragment(@NonNull Class<? extends BaseQmuiFragment> fragment, @Nullable Bundle fragmentArgs) {
        if (mHomeControlListener != null) {
            mHomeControlListener.startFragment(fragment, fragmentArgs);
        }
    }

    public void setHomeControlListener(BaseController.HomeControlListener homeControlListener) {
        mHomeControlListener = homeControlListener;
    }

    public void showProgressDialog(){
        if(mHomeControlListener != null){
            mHomeControlListener.showProgressDialog();
        }
    }

    public void hideProgressDialog(){
        if(mHomeControlListener != null){
            mHomeControlListener.hideProgressDialog();
        }
    }

    public View getEmptyDataView(){
        View view = View.inflate(mContext,R.layout.layout_empty_nodata,null);
        return view;
    }


    public interface HomeControlListener {
        void startIntent();

        void startFragment(QMUIFragment fragment);

        void startFragment(@NonNull Class<? extends QMUIFragment> fragment);

        void startFragment(@NonNull Class<? extends QMUIFragment> fragment, @Nullable Bundle fragmentArgs);

        void showProgressDialog();

        void hideProgressDialog();
    }

}
