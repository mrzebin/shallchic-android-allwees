package com.hb.basemodel.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.hb.basemodel.R;

import androidx.annotation.NonNull;

/**
 * 简易可设置提示信息的加载对话框
 */
public class LoadingDialog extends Dialog {
    private TextView contentTv;
    private String   loadingInfo;
    private Context mContext;

    public LoadingDialog(@NonNull Context context) {
        super(context);
        mContext = context;
        initView();
    }

    private void initView() {
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.common_dialog_loading,null);
        contentTv = contentView.findViewById(R.id.common_dialog_tv_information);
        if (TextUtils.isEmpty(loadingInfo)) {
            contentTv.setVisibility(View.GONE);
        } else {
            contentTv.setText(loadingInfo);
        }
        setCancelable(false);
        setContentView(contentView);
    }

    /**
     * 设置加载时提示
     *
     * @param info
     */
    public void setLoadingInformation(String info) {
        loadingInfo = info;
        if (!TextUtils.isEmpty(loadingInfo) && contentTv != null) {
            contentTv.setText(info);
            contentTv.setVisibility(View.VISIBLE);
        }
    }

}
