package com.project.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.project.app.R;
import com.project.app.bean.OrderFunctionBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MeOrderFunAdapter extends BaseQuickAdapter<OrderFunctionBean, BaseViewHolder> {
    final List<OrderFunctionBean> mDatas;

    public MeOrderFunAdapter(@Nullable List<OrderFunctionBean> data) {
        super(R.layout.item_me_order_funs, data);
        this.mDatas = data;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, OrderFunctionBean orderFunctionBean) {
        helper.setImageResource(R.id.iv_funRes,orderFunctionBean.getResId());
        helper.setText(R.id.tv_funName,orderFunctionBean.getFunName());
    }
}
