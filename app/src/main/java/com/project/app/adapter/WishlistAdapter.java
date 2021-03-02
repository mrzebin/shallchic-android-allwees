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
import com.project.app.bean.WishDataBean;
import com.project.app.fragment.GoodsDetailFragment;
import com.project.app.utils.LocaleUtil;
import com.project.app.utils.MathUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WishlistAdapter extends BaseQuickAdapter<WishDataBean.ProductDtoWrapp, BaseViewHolder> implements LoadMoreModule {
    private String pSymbol;

    public WishlistAdapter(List<WishDataBean.ProductDtoWrapp> data) {
        super(R.layout.item_favorite, data);
        pSymbol = LocaleUtil.getInstance().getSymbole();
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, WishDataBean.ProductDtoWrapp goodInfo) {
        String free = getContext().getString(R.string.str_free);
        WishDataBean.ProductDto productDto = goodInfo.getProductDto();
        String mainPhoto = productDto.getMainPhoto();

        if(!TextUtils.isEmpty(mainPhoto)){
            ImageLoader.getInstance().displayImage(helper.getView(R.id.iv_gif_goods),productDto.getMainPhoto(),R.mipmap.allwees_ic_default_goods);
        }

        if(!TextUtils.isEmpty(productDto.getPriceRetail()+"")){
            if(productDto.getPriceRetail() == 0 ){
                helper.setText(R.id.tv_discountPrice,free);
            }else{
                helper.setText(R.id.tv_discountPrice,pSymbol + productDto.getPriceRetail()+"");
            }
        }

        if(productDto.getPriceOrigin() == productDto.getPriceRetail()){    //如果价格一样则不显示原价
            helper.setVisible(R.id.tv_orignPrice,false);
        }else if(productDto.getPriceOrigin() == 0){
            helper.setVisible(R.id.tv_orignPrice,false);
        }else{
            TextView tv_orignPrice = helper.getView(R.id.tv_orignPrice);
            tv_orignPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            helper.setText(R.id.tv_orignPrice,pSymbol + productDto.getPriceOrigin());
        }

        if(!TextUtils.isEmpty(productDto.getSalesTotal()+"")){
            String saleNum = MathUtil.Companion.dimGradeDigit(productDto.getSalesTotal());
            helper.setText(R.id.tv_discountDes,saleNum);
        }

        helper.itemView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("uuid", productDto.getUuid());
            bundle.putString("type", "0");
            HolderActivity.startFragment(getContext(),GoodsDetailFragment.class,bundle);
        });
    }
}
