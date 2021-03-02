package com.project.app.adapter;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hb.basemodel.image.ImageLoader;
import com.project.app.R;
import com.project.app.bean.OrderBean;
import com.project.app.bean.OrderDetailBean;
import com.project.app.utils.MathUtil;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailKindsAdapter extends BaseQuickAdapter<OrderDetailBean.Items, BaseViewHolder> {
    private final List<OrderBean.OrderItems> orderItems = new ArrayList<>();

    public OrderDetailKindsAdapter(@Nullable List<OrderDetailBean.Items> data) {
        super(R.layout.item_kinds_order_detail_child, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, OrderDetailBean.Items data) {
        String qty = getContext().getString(R.string.str_qty);
        TextView tv_goodsDescribute = helper.getView(R.id.tv_placeOrderDec);

        if(data.getProduct() == null){
            return;
        }
        String loadUrl = data.getProduct().getMainPhoto();
        QMUIRadiusImageView ivGoods = helper.findView(R.id.iv_placeOrderG);
        if(!TextUtils.isEmpty(loadUrl)){
            ImageLoader.getInstance().displayImage(ivGoods,loadUrl,R.mipmap.allwees_ic_default_goods);
        }
        if(!TextUtils.isEmpty(data.getProduct().getName())){
            tv_goodsDescribute.setText(data.getProduct().getName());
        }
        helper.setText(R.id.tv_placeOrderM, MathUtil.Companion.formatPrice(data.getSku().getRetailPrice()));
        helper.setText(R.id.tv_placeOrderC,qty + data.getSku().getQuantity());
        helper.setText(R.id.tv_placeOrderSc,data.getSku().getColor()+","+data.getSku().getSize());

        if(true){
            Drawable freeDrawable = getContext().getResources().getDrawable(R.mipmap.ic_free_tag);
            tv_goodsDescribute.setCompoundDrawables(freeDrawable,null,null,null);
            tv_goodsDescribute.setCompoundDrawablePadding(3);
        }

        if(data.getReviewAble()){
            helper.setVisible(R.id.tv_dReView,true);
        }else{
            helper.setVisible(R.id.tv_dReView,false);
        }
        if(data.getRefundAble()){
            helper.setVisible(R.id.tv_dRefund, true);
        }else{
            helper.setVisible(R.id.tv_dRefund, false);
        }
        if(data.getReceiveAble()){
            helper.setVisible(R.id.tv_dReceive, true);
        }else{
            helper.setVisible(R.id.tv_dReceive, false);
        }
        if(data.getDelivered()){
            helper.setVisible(R.id.tv_lookforLogistic, true);
        }else{
            helper.setVisible(R.id.tv_lookforLogistic, false);
        }
        if(data.getCancelAble()){
            helper.setVisible(R.id.tv_dCancel,true);
        }else{
            helper.setVisible(R.id.tv_dCancel,false);
        }
        helper.getView(R.id.tv_dReView).setOnClickListener(view -> {
            if(listener != null){
                listener.LookForType(data,"review");
            }
        });
        helper.getView(R.id.tv_dRefund).setOnClickListener(view -> {
            if(listener != null){
                listener.LookForType(data,"refund");
            }
        });

        helper.getView(R.id.tv_lookforLogistic).setOnClickListener(view -> listener.LookForType(data,"lookLog"));

        helper.getView(R.id.tv_dReceive).setOnClickListener(view -> listener.LookForType(data,"receive"));

        helper.getView(R.id.tv_dCancel).setOnClickListener(view -> listener.LookForType(data,"cancel"));
    }

    public CallbackListener listener;

    public CallbackListener getListener() {
        return listener;
    }

    public void setListener(CallbackListener listener) {
        this.listener = listener;
    }

    public interface CallbackListener{
        void LookForType(OrderDetailBean.Items item,String taskType);
    }
}
