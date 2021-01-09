package com.project.app.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.project.app.R;
import com.project.app.bean.RewardDashbBean;
import com.project.app.utils.LocaleUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class CouponUseAdapter extends BaseQuickAdapter<RewardDashbBean.CouponItem, BaseViewHolder> {
    private final List<RewardDashbBean.CouponItem> mDatas;
    private final ScaleAnimation mScaleAnima;

    public CouponUseAdapter(@Nullable List<RewardDashbBean.CouponItem> data) {
        super(R.layout.item_coupon_recode, data);
        this.mDatas = data;
        mScaleAnima = new ScaleAnimation(1,1.2f,1,1.2f, 0.5f,0.5f);
        mScaleAnima.setDuration(300);
        mScaleAnima.setFillAfter(false);
        mScaleAnima.setRepeatCount(0);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, RewardDashbBean.CouponItem bean) {
        ImageView iv_copy = helper.getView(R.id.tv_cUse);
        TextView tv_no    = helper.getView(R.id.tv_cValueRate);
        String moneySymbol = LocaleUtil.getInstance().getSymbole();

//        String str_max    = getContext().getResources().getString(R.string.str_max);

        if(!TextUtils.isEmpty(bean.getValueText())){
            tv_no.setText(bean.getValueText());
        }

        if(!TextUtils.isEmpty(bean.getNo())){
            helper.setText(R.id.tv_cCode,bean.getNo());
        }

        if(!TextUtils.isEmpty(bean.getName())){
            helper.setText(R.id.tv_deductName,bean.getName());
        }

        helper.setText(R.id.tv_deductMoney,bean.getMaxDeductAmt() + moneySymbol);

        iv_copy.setOnClickListener(view -> {
            iv_copy.setAnimation(mScaleAnima);
            iv_copy.startAnimation(mScaleAnima);

            ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText("Label", bean.getNo());
            cm.setPrimaryClip(mClipData);
        });
    }
}
