package com.project.app.ui.dialog;

import android.content.Context;
import android.os.Handler;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.project.app.R;

public class TipDialogUtil {
    private QMUITipDialog tipDialog;
    private final Context mContext;
    private final Handler mHandler;
    private final long DELATY_DEFAULT_TIME = 1000;

    public TipDialogUtil(Context context, Handler mHandler) {
        this.mContext = context;
        this.mHandler = mHandler;
    }

    public void showTipSuccess(){
        String hintMsg = mContext.getResources().getString(R.string.hint_success);
        tipDialog = new QMUITipDialog.Builder(mContext)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord(hintMsg)
                .create();
        show();
    }

    public void showTopFail(){
        String hintMsg = mContext.getResources().getString(R.string.hint_fail);
        tipDialog = new QMUITipDialog.Builder(mContext)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord(hintMsg)
                .create();
        show();
    }

    public void showWarn(){
        tipDialog = new QMUITipDialog.Builder(mContext)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                .setTipWord("Do not repeat operation")
                .create();
        show();
    }

    public void show(){
        if(tipDialog != null){
            tipDialog.show();
        }
        mHandler.postDelayed(() -> tipDialog.dismiss(),DELATY_DEFAULT_TIME);
    }
}
