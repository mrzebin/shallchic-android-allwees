package com.project.app.adapter;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.image.ImageLoader;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.bean.GoodsRelationBean;
import com.project.app.fragment.GoodsDetailFragment;
import com.project.app.utils.LocaleUtil;
import com.project.app.utils.MathUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 商品详情的封面
 */
public class GoodsRelativeAdapter extends BaseQuickAdapter<GoodsRelationBean, BaseViewHolder> {
    private String pSymbol;
    private List<GoodsRelationBean> mDatas;

    public GoodsRelativeAdapter(@Nullable List<GoodsRelationBean> data) {
        super(R.layout.item_home_classify, data);
        this.mDatas = data;
        pSymbol = LocaleUtil.getInstance().getSymbole();
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, GoodsRelationBean goodInfo) {
        String free = getContext().getString(R.string.str_free);

        if(!TextUtils.isEmpty(goodInfo.getMainPhoto())){
            ImageLoader.getInstance().displayImage(helper.getView(R.id.tv_superClassify),goodInfo.getMainPhoto() + Constant.mGlobalThumbnailStyle,R.mipmap.allwees_ic_default_goods);
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
            String saleNum = MathUtil.Companion.dimGradeDigit(goodInfo.getSalesTotal());
            helper.setText(R.id.tv_discountDes,saleNum);
        }

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("uuid", goodInfo.getUuid());
                bundle.putString("type", "1");
                Intent intent = HolderActivity.of(getContext(), GoodsDetailFragment.class,bundle);
                getContext().startActivity(intent);
            }
        });
    }

}
