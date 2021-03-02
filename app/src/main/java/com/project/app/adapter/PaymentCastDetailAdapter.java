package com.project.app.adapter;

import android.graphics.Paint;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.project.app.R;
import com.project.app.bean.PaymentCastBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PaymentCastDetailAdapter extends BaseQuickAdapter<PaymentCastBean, BaseViewHolder> {

    public PaymentCastDetailAdapter(@Nullable List<PaymentCastBean> data) {
        super(R.layout.item_payment_tollprice, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, PaymentCastBean data) {
        helper.setText(R.id.tv_tollPaymentType,data.castType);
        TextView tv_PaymentCostPrice = helper.getView(R.id.tv_tollPaymentPrice);
        tv_PaymentCostPrice.setText(data.castPrice);

        if(data.fontColorType == 0){
            tv_PaymentCostPrice.setTextColor(getContext().getResources().getColor(R.color.color_333));
        }else if(data.fontColorType == 1){
            helper.setTextColor(R.id.tv_tollPaymentType,getContext().getResources().getColor(R.color.color_54CBA3));
            tv_PaymentCostPrice.setTextColor(getContext().getResources().getColor(R.color.color_54CBA3));
        }else if(data.fontColorType == 2){
            tv_PaymentCostPrice.setTextColor(getContext().getResources().getColor(R.color.color_red));
        }

        if(data.fontStyle == 1){
            tv_PaymentCostPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }else if(data.fontStyle == 0){
            tv_PaymentCostPrice.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
        }

        if(!TextUtils.isEmpty(data.attachTag)){
            if(data.attachTag.equals(getContext().getResources().getString(R.string.str_upper_free))){
                helper.setTextColor(R.id.tv_attachPaymentPrice,getContext().getResources().getColor(R.color.color_54CBA3));
            }
            helper.setText(R.id.tv_attachPaymentPrice,data.attachTag);
            helper.setVisible(R.id.tv_attachPaymentPrice,true);
        }else{
            helper.setVisible(R.id.tv_attachPaymentPrice,false);
        }
    }

}
