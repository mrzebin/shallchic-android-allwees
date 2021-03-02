package com.project.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.project.app.R;
import com.project.app.bean.OrderDetailCastItem;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OrderDetailCastAdapter extends BaseQuickAdapter<OrderDetailCastItem, BaseViewHolder> {
    private List<OrderDetailCastItem> mDatas;

    public OrderDetailCastAdapter(@Nullable List<OrderDetailCastItem> data) {
        super(R.layout.item_order_detail_tollprice, data);
        this.mDatas = data;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, OrderDetailCastItem data) {
        if(data.colorType == 0){
            helper.setTextColor(R.id.tv_tollPrice,getContext().getResources().getColor(R.color.color_333));
        }else if(data.colorType == 1){
            helper.setTextColor(R.id.tv_tollPrice,getContext().getResources().getColor(R.color.color_54CBA3));
        }else if(data.colorType == 2){
            helper.setTextColor(R.id.tv_tollPrice,getContext().getResources().getColor(R.color.color_red));
        }
        helper.setText(R.id.tv_tollType,data.tollType);
        helper.setText(R.id.tv_tollPrice,data.virtualCash);
        helper.setVisible(R.id.v_tollOrderDetail,data.showLine);
    }

}
