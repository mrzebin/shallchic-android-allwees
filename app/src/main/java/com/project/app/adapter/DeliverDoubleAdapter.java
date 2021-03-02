package com.project.app.adapter;

import android.os.Bundle;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hb.basemodel.image.ImageLoader;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.bean.ActionFreeOneBean;
import com.project.app.fragment.GoodsDetailFragment;
import com.project.app.utils.MathUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DeliverDoubleAdapter extends BaseQuickAdapter<ActionFreeOneBean.ClassifyItem, BaseViewHolder> implements LoadMoreModule {

    public DeliverDoubleAdapter(List<ActionFreeOneBean.ClassifyItem> data) {
        super(R.layout.item_buy_more, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, ActionFreeOneBean.ClassifyItem goodInfo) {
        String free = getContext().getResources().getString(R.string.str_free);
        String mainPhoto = goodInfo.getMainPhoto();

        if(!TextUtils.isEmpty(mainPhoto)){
            ImageLoader.getInstance().displayImage(helper.getView(R.id.tv_superClassify),mainPhoto,R.mipmap.allwees_ic_default_goods);
        }

        if(!TextUtils.isEmpty(goodInfo.getPriceRetail()+"")){
            if(goodInfo.getPriceRetail() == 0 ){
                helper.setText(R.id.tv_discountPrice,free);
            }else{
                helper.setText(R.id.tv_discountPrice,MathUtil.Companion.formatPrice(goodInfo.getPriceRetail()));
            }
        }

        if(!TextUtils.isEmpty(goodInfo.getSalesTotal()+"")){
            String saleNum = MathUtil.Companion.dimGradeDigit(goodInfo.getSalesTotal());
            helper.setText(R.id.tv_discountDes,saleNum);
        }

        helper.itemView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("uuid", goodInfo.getUuid());
            bundle.putString("type","1");
            HolderActivity.startFragment(getContext(),GoodsDetailFragment.class,bundle);
        });
    }
}
