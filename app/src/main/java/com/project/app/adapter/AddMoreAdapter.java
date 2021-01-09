package com.project.app.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.image.ImageLoader;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.bean.AddMoreListBean;
import com.project.app.fragment.GoodsDetailFragment;
import com.project.app.utils.MathUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AddMoreAdapter extends BaseQuickAdapter<AddMoreListBean.ClassifyItem, BaseViewHolder> implements LoadMoreModule {

    public AddMoreAdapter(List<AddMoreListBean.ClassifyItem> data) {
        super(R.layout.item_buy_more, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, AddMoreListBean.ClassifyItem goodInfo) {
        String free = getContext().getResources().getString(R.string.str_free);
        String mainPhoto = goodInfo.getMainPhoto();

        if(!TextUtils.isEmpty(mainPhoto)){
            if(mainPhoto.endsWith("png") || mainPhoto.endsWith("jpg")){
                ImageLoader.getInstance().displayImage(helper.getView(R.id.tv_superClassify),mainPhoto + Constant.mGlobalThumbnailStyle,R.mipmap.allwees_ic_default_goods);
            }else if(mainPhoto.endsWith("gif")){
                ImageLoader.getInstance().displayImage(helper.getView(R.id.tv_superClassify),mainPhoto,R.mipmap.allwees_ic_default_goods);
            }
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
            Intent intent = HolderActivity.of(getContext(), GoodsDetailFragment.class,bundle);
            getContext().startActivity(intent);
        });
    }
}
