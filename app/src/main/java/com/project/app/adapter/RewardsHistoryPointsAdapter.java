package com.project.app.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.project.app.R;
import com.project.app.bean.HistoryPointsBean;
import com.project.app.utils.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RewardsHistoryPointsAdapter extends BaseQuickAdapter<HistoryPointsBean.HistoryPointsItem, BaseViewHolder> {
    private final List<HistoryPointsBean.HistoryPointsItem> mDatas;

    public RewardsHistoryPointsAdapter(@Nullable List<HistoryPointsBean.HistoryPointsItem> data) {
        super(R.layout.item_rewards_pionts, data);
        this.mDatas = data;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, HistoryPointsBean.HistoryPointsItem cashItem) {
        if(!TextUtils.isEmpty(cashItem.getUseType())){
            helper.setText(R.id.tv_rewardItemState,cashItem.getUseType());
        }
        if(cashItem.getCreatedAt() >0){
            helper.setText(R.id.tv_rewardDate, StringUtils.getEnMDFormat(cashItem.getCreatedAt()));
        }
        if(cashItem.getStatus() == 0){
            helper.setText(R.id.tv_rewardMoney,"-" + cashItem.getValue());
            helper.setTextColor(R.id.tv_rewardMoney,getContext().getResources().getColor(R.color.color_red));
        }else{
            helper.setText(R.id.tv_rewardMoney,"+" + cashItem.getValue());
        }
    }
}
