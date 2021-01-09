package com.project.app.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.project.app.utils.StringUtils;
import com.project.app.R;
import com.project.app.bean.ScCashBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ScHistoryAdapter extends BaseQuickAdapter<ScCashBean.CashItem, BaseViewHolder> {
    private final List<ScCashBean.CashItem> mDatas;

    public ScHistoryAdapter(@Nullable List<ScCashBean.CashItem> data) {
        super(R.layout.item_sc_history, data);
        this.mDatas = data;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, ScCashBean.CashItem cashItem) {
        if(!TextUtils.isEmpty(cashItem.getLogType())){
            helper.setText(R.id.tv_scH_state,cashItem.getLogType());
        }
        if(cashItem.getCreatedAt() >0){
            helper.setText(R.id.tv_scH_date, StringUtils.getEnMDFormat(cashItem.getCreatedAt()));
        }
        if(cashItem.getStatus() == 0){
            helper.setText(R.id.tv_sc_money,"-" + cashItem.getValue());
        }else{
            helper.setText(R.id.tv_sc_money,"+" + cashItem.getValue());
        }
    }
}
