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
import com.project.app.utils.MathUtil;
import com.project.app.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class CouponUseAdapter extends BaseQuickAdapter<RewardDashbBean.CouponItem, BaseViewHolder> {
    private final List<RewardDashbBean.CouponItem> mDatas;
    private final ScaleAnimation mScaleAnima;
    private int mType;

    public CouponUseAdapter(@Nullable List<RewardDashbBean.CouponItem> data,int type) {
        super(R.layout.item_coupon_recode, data);
        this.mDatas = data;
        this.mType = type;
        mScaleAnima = new ScaleAnimation(0.9f,1f,0.9f,1f, 0.5f,0.5f);
        mScaleAnima.setDuration(300);
        mScaleAnima.setFillAfter(false);
        mScaleAnima.setRepeatCount(0);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, RewardDashbBean.CouponItem bean) {
        ImageView iv_copy = helper.getView(R.id.tv_cUse);
        TextView tv_no    = helper.getView(R.id.tv_cValueRate);
        StringBuffer bonusInfo   = new StringBuffer();
        StringBuffer expiresTime = new StringBuffer();

        if(!TextUtils.isEmpty(bean.getValueText())){
            tv_no.setText(bean.getValueText());
        }

        if(!TextUtils.isEmpty(bean.getNo())){
            helper.setText(R.id.tv_cCode,bean.getNo());
        }

        if(!TextUtils.isEmpty(bean.getName())){
            bonusInfo.append(bean.getName() + " - ");
        }
        bonusInfo.append(MathUtil.Companion.formatPrice(bean.getMaxDeductAmt()) + " ");
        bonusInfo.append(getContext().getString(R.string.str_max));
        helper.setText(R.id.tv_deductName,bonusInfo.toString());

        expiresTime.append(getContext().getString(R.string.expires_one) + " ");
        expiresTime.append(StringUtils.getEnMDFormat(bean.getOverdueTime()));

        if(mType == 0) {
            helper.setText(R.id.tv_cActionPts,expiresTime.toString());
        }else if(mType == 1){
            helper.setText(R.id.tv_cActionPts,getContext().getResources().getString(R.string.str_use));
            helper.setTextColor(R.id.tv_cActionPts,getContext().getResources().getColor(R.color.color_ccc));
        }

        iv_copy.setOnClickListener(view -> {
            iv_copy.setAnimation(mScaleAnima);
            iv_copy.startAnimation(mScaleAnima);

            ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText("Label", bean.getNo());
            cm.setPrimaryClip(mClipData);
        });
    }
}
