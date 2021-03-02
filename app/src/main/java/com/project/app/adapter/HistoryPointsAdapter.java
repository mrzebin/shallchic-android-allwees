package com.project.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.project.app.R;
import com.project.app.bean.RewardDashbBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class HistoryPointsAdapter extends BaseQuickAdapter<RewardDashbBean.CouponItem, BaseViewHolder> {


    public HistoryPointsAdapter(@Nullable List<RewardDashbBean.CouponItem> data, int type) {
        super(R.layout.item_coupon_recode, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, RewardDashbBean.CouponItem bean) {

    }
}
