package com.project.app.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hb.basemodel.image.ImageLoader;
import com.hb.basemodel.utils.DataUtil;
import com.project.app.utils.StringUtils;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.bean.OrderBean;
import com.project.app.fragment.order.OrderDetailFragment;
import com.project.app.utils.MathUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderKindsAdapter extends BaseQuickAdapter<OrderBean.OrderResult, BaseViewHolder> {
    private final List<OrderBean.OrderItems> orderItems = new ArrayList<>();

    public OrderKindsAdapter(@Nullable List<OrderBean.OrderResult> data) {
        super(R.layout.item_kinds_order, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, OrderBean.OrderResult data) {
        OrderInnerAdapter mAdapter = new OrderInnerAdapter(orderItems);
        RecyclerView rlv_spriteItem = helper.findView(R.id.rlv_spriteItem);
        rlv_spriteItem.setLayoutManager(new LinearLayoutManager(getContext()){
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });
        rlv_spriteItem.setAdapter(mAdapter);
        if(DataUtil.idNotNull(data.getItems())){
            List<OrderBean.OrderItems> resultList = data.getItems();
            for(OrderBean.OrderItems cell:resultList){
                cell.setPatyStatus(data.getStateDesc());
                cell.setCreateT(data.getCreatedAt());
                cell.setNo(data.getUuid());
            }
            mAdapter.setNewInstance(resultList);
        }

        String unitItem = getContext().getResources().getString(R.string.str_item);
        helper.setText(R.id.tv_placeOrderTotalC,unitItem + "(" + data.getSum() + ")");
        helper.setText(R.id.tv_orderShippingP,MathUtil.Companion.formatPrice(data.getAmtShipping()));
        helper.setText(R.id.tv_placeTotalPrice,MathUtil.Companion.formatPrice(data.getPlatformAmt()));

        helper.itemView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("orderId",data.getUuid());
            bundle.putString("orderType",data.getStateDesc());
            Intent intent = HolderActivity.of(getContext(), OrderDetailFragment.class,bundle);
            getContext().startActivity(intent);
        });
    }

    static class OrderInnerAdapter extends BaseQuickAdapter<OrderBean.OrderItems,BaseViewHolder>{
        public OrderInnerAdapter(List<OrderBean.OrderItems> data) {
            super(R.layout.item_kinds_order_child, data);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder helper, OrderBean.OrderItems orderItems) {
            String Qty = getContext().getResources().getString(R.string.str_qty);
            if(orderItems.getProduct() == null){
                return;
            }
            String loadUrl = orderItems.getProduct().getMainPhoto();
            QMUIRadiusImageView ivGoods = helper.findView(R.id.iv_placeOrderG);

            long createTime = orderItems.getCreateT();

            if(!TextUtils.isEmpty(loadUrl)){
                ImageLoader.getInstance().displayImage(ivGoods,loadUrl,R.mipmap.allwees_ic_default_goods);
            }

            if(!TextUtils.isEmpty(orderItems.getPatyStatus())){
                helper.setText(R.id.tv_placeOrderS,orderItems.getPatyStatus());
            }

            String simpleTime = StringUtils.getEnDateFormat(createTime);
            helper.setText(R.id.tv_placeOrderT,simpleTime);

            if(!TextUtils.isEmpty(orderItems.getProduct().getName())){
                helper.setText(R.id.tv_placeOrderDec,orderItems.getProduct().getName());
            }
            helper.setText(R.id.tv_placeOrderM, MathUtil.Companion.formatPrice(orderItems.getSku().getRetailPrice()));
            helper.setText(R.id.tv_placeOrderC,Qty + orderItems.getSku().getQuantity());
            helper.setText(R.id.tv_placeOrderSc,orderItems.getSku().getColor()+","+orderItems.getSku().getSize());
            helper.itemView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putString("orderId",orderItems.getNo());
                bundle.putString("orderType",orderItems.getPatyStatus());
                Intent intent = HolderActivity.of(getContext(), OrderDetailFragment.class,bundle);
                getContext().startActivity(intent);
            });
        }
    }
}
