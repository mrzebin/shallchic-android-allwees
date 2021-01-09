package com.project.app.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.project.app.R;
import com.project.app.bean.RedeemBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class RedeemAdapter extends BaseQuickAdapter<RedeemBean.RedeemItem, BaseViewHolder> {
    private final List<RedeemBean.RedeemItem> mDatas;

    public RedeemAdapter(@Nullable List<RedeemBean.RedeemItem> data) {
        super(R.layout.item_earn_redeem, data);
        this.mDatas = data;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, RedeemBean.RedeemItem bean) {
        if(bean.getPointExchange()){
            helper.setText(R.id.tv_rateCoupon,bean.getValueText());
        }else{
            helper.setTextColor(R.id.tv_rateCoupon,getContext().getResources().getColor(R.color.color_c7c7));
            helper.setTextColor(R.id.tv_p_s,getContext().getResources().getColor(R.color.color_c7c7));
            helper.setTextColor(R.id.tv_p_t,getContext().getResources().getColor(R.color.color_c7c7));
            helper.setBackgroundColor(R.id.ll_right_p,getContext().getResources().getColor(R.color.color_bbe7));
        }

        if(!TextUtils.isEmpty(bean.getValueText())){
            helper.setText(R.id.tv_rateCoupon,bean.getValueText());
        }

        if(bean.getCount() > 200){
            helper.setText(R.id.tv_entireCount,200 + "pts");
        }else{
            helper.setText(R.id.tv_entireCount, bean.getCount() + "pts");
        }

        if(bean.getPointExchange()){
            helper.itemView.setOnClickListener(view -> {
                if(listener != null){
                    listener.upPointToCoupon(bean.getUuid());
                }
            });
        }
    }

    public ChangeCallbackListener listener;

    public ChangeCallbackListener getListener() {
        return listener;
    }

    public void setListener(ChangeCallbackListener listener) {
        this.listener = listener;
    }

    public interface ChangeCallbackListener{
        void upPointToCoupon(String uuid);
    }

}
