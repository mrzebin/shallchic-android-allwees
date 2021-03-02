package com.project.app.adapter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hb.basemodel.image.ImageLoader;
import com.hb.basemodel.utils.DataUtil;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.bean.OrderBean;
import com.project.app.fragment.order.OrderDetailFragment;
import com.project.app.utils.MathUtil;
import com.project.app.utils.StringUtils;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;

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
                cell.setOrderUuid(data.getUuid());  //获取订单的uuid
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
        if(!TextUtils.isEmpty(data.getState())){
            helper.setText(R.id.tv_placeOrderState,data.getState());
        }

        String simpleTime = StringUtils.getEnDateFormat(data.getCreatedAt());
        helper.setText(R.id.tv_placeOrderTime,simpleTime);

        helper.itemView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("orderId",data.getUuid());
            HolderActivity.startFragment(getContext(),OrderDetailFragment.class,bundle);
        });

        helper.getView(R.id.btn_repurchase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.doRepurchase(data.getUuid());
                }
            }
        });
    }

    class OrderInnerAdapter extends BaseQuickAdapter<OrderBean.OrderItems,BaseViewHolder>{

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
            if(!TextUtils.isEmpty(loadUrl)){
                ImageLoader.getInstance().displayImage(ivGoods,loadUrl,R.mipmap.allwees_ic_default_goods);
            }
            if(!TextUtils.isEmpty(orderItems.getProduct().getName())){
                helper.setText(R.id.tv_placeOrderDec,orderItems.getProduct().getName());
            }
            helper.setText(R.id.tv_placeOrderM, MathUtil.Companion.formatPrice(orderItems.getSku().getRetailPrice()));
            helper.setText(R.id.tv_placeOrderC,Qty + orderItems.getSku().getQuantity());
            helper.setText(R.id.tv_placeOrderSc,orderItems.getSku().getColor()+","+orderItems.getSku().getSize());

            if(orderItems.getReviewAble()){
                helper.setVisible(R.id.tv_orderStateReview,true);
            }
            if(orderItems.getRefundAble()){
                helper.setVisible(R.id.tv_orderStateRefund,true);
            }
            if(orderItems.getReceiveAble()){
                helper.setVisible(R.id.tv_orderStateReceive,true);
            }
            if(orderItems.getCancelAble()){
                helper.setVisible(R.id.tv_orderStateCancel,true);
            }

            helper.getView(R.id.tv_orderStateReview).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null){
                        mListener.doReview(orderItems.getProduct().getMainPhoto(),orderItems.getProduct().getName(),orderItems.getOrderUuid(),orderItems.getUuid());
                    }
                }
            });

            helper.getView(R.id.tv_orderStateRefund).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null){
                        mListener.doRefund(orderItems.getOrderUuid(),orderItems.getUuid());
                    }
                }
            });

            helper.getView(R.id.tv_orderStateReceive).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null){
                        mListener.doReceive(orderItems.getUuid());
                    }
                }
            });

            helper.getView(R.id.tv_orderStateCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null){
                        mListener.doCancel(orderItems.getOrderUuid(),orderItems.getUuid());
                    }
                }
            });

            helper.itemView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putString("orderId",orderItems.getNo());
                HolderActivity.startFragment(getContext(),OrderDetailFragment.class,bundle);
            });
        }
    }

   public static IOrderStateListener mListener;

    public IOrderStateListener getListener() {
        return mListener;
    }

    public void setListener(IOrderStateListener listener) {
        this.mListener = listener;
    }

   public interface IOrderStateListener{
       void doReceive(String uuid);
       void doReview(String url,String goodsName,String orderUuid,String uuid);
       void doRefund(String orderUuid,String uuid);
       void doCancel(String orderUuid,String uuid);
       void doRepurchase(String orderUuid);
    }


}
