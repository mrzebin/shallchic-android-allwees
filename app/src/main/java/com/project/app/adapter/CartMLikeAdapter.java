package com.project.app.adapter;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hb.basemodel.image.ImageLoader;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.bean.ClassifyListBean;
import com.project.app.fragment.GoodsDetailFragment;
import com.project.app.utils.LocaleUtil;
import com.project.app.utils.MathUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CartMLikeAdapter extends BaseQuickAdapter<ClassifyListBean.ClassifyItem, BaseViewHolder> implements LoadMoreModule {
    private String pSymbol;

    public CartMLikeAdapter(List<ClassifyListBean.ClassifyItem> data) {
        super(R.layout.item_cart_might_like, data);
        pSymbol = LocaleUtil.getInstance().getSymbole();
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, ClassifyListBean.ClassifyItem goodInfo) {
        String mainPhoto = goodInfo.getMainPhoto();
        if(!TextUtils.isEmpty(mainPhoto)){
            ImageLoader.getInstance().displayImage(helper.getView(R.id.iv_gif_goods),mainPhoto,R.mipmap.allwees_ic_default_goods);
        }

        if(!TextUtils.isEmpty(goodInfo.getPriceRetail()+"")){
            if(goodInfo.getPriceRetail() == 0 ){
                helper.setText(R.id.tv_discountPrice,getContext().getString(R.string.str_free));
            }else{
                helper.setText(R.id.tv_discountPrice,pSymbol + goodInfo.getPriceRetail()+"");
            }
        }

        if(goodInfo.getPriceOrigin() == goodInfo.getPriceRetail()){    //如果价格一样则不显示原价
            helper.setVisible(R.id.tv_orignPrice,false);
        }else{
            TextView tv_orignPrice = helper.getView(R.id.tv_orignPrice);
            tv_orignPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            helper.setText(R.id.tv_orignPrice,pSymbol + goodInfo.getPriceOrigin());
        }

        if(!TextUtils.isEmpty(goodInfo.getSalesTotal()+"")){
            String saleNum = MathUtil.Companion.dimGradeDigit(goodInfo.getSalesTotal());
            helper.setText(R.id.tv_discountDes,saleNum);
        }

        helper.itemView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("uuid", goodInfo.getUuid());
            bundle.putString("type", "0");
            HolderActivity.startFragment(getContext(),GoodsDetailFragment.class,bundle);
        });
    }
}
