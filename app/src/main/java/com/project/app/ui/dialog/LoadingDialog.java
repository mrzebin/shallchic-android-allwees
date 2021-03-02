package com.project.app.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.project.app.R;

import androidx.annotation.NonNull;

/**
 * create by hb
 * 全局加载dialog
 */
public class LoadingDialog extends Dialog {
    private Context mContext;

    public LoadingDialog(@NonNull Context context) {
        super(context,R.style.loadingStyle);
        mContext = context;
        initView();
    }

    private void initView() {
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_main_loading,null);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(contentView);
    }
}
