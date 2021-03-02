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
import com.project.app.bean.FreeGiftBean;
import com.project.app.config.AppsFlyConfig;
import com.project.app.fragment.GoodsDetailFragment;
import com.project.app.utils.AppsFlyEventUtils;
import com.project.app.utils.LocaleUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FreeGiftAdapter extends BaseQuickAdapter<FreeGiftBean.ClassifyItem, BaseViewHolder> implements LoadMoreModule {
    private String pSymbol;

    public FreeGiftAdapter(List<FreeGiftBean.ClassifyItem> data) {
        super(R.layout.item_new_gift, data);
        pSymbol = LocaleUtil.getInstance().getSymbole();
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, FreeGiftBean.ClassifyItem goodInfo) {
        String free = getContext().getResources().getString(R.string.str_free);

        if(!TextUtils.isEmpty(goodInfo.getMainPhoto())){
            ImageLoader.getInstance().displayImage(helper.getView(R.id.iv_gif_goods),goodInfo.getMainPhoto(),R.mipmap.allwees_ic_default_goods);
        }

        if(!TextUtils.isEmpty(goodInfo.getPriceRetail()+"")){
            if(goodInfo.getPriceRetail() == 0 ){
                helper.setText(R.id.tv_discountPrice,free);
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
            int saleNum = goodInfo.getSalesTotal();
            String saleStr;
            saleStr = saleNum + "";
            helper.setText(R.id.tv_discountDes,saleStr);
        }

        helper.itemView.setOnClickListener(view -> {
            AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_NEW_USER_GIFT_GOODS);
            Bundle bundle = new Bundle();
            bundle.putString("uuid", goodInfo.getUuid());
            bundle.putString("type", "1");
            HolderActivity.startFragment(getContext(),GoodsDetailFragment.class,bundle);
        });
    }
}
