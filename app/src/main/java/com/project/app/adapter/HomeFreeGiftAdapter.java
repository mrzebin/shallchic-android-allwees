package com.project.app.adapter;

import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.image.ImageLoader;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.bean.HomeFreeGiftBean;
import com.project.app.fragment.home.classify.HomeClassifyGiftsFragment;
import com.project.app.utils.MathUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomeFreeGiftAdapter extends BaseQuickAdapter<HomeFreeGiftBean.GiftItem, BaseViewHolder> {
    private final DisplayMetrics dm;

    public HomeFreeGiftAdapter(List<HomeFreeGiftBean.GiftItem> data,DisplayMetrics dm) {
        super(R.layout.item_home_gift, data);
        this.dm = dm;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, HomeFreeGiftBean.GiftItem data) {
        QMUIRadiusImageView qiv_cover = helper.getView(R.id.qiv_giftCover);
        QMUIRadiusImageView qiv_cCover = helper.getView(R.id.qiv_giftRCover);

        TextView tv_newRetailP = helper.getView(R.id.tv_newRetailP);
        tv_newRetailP.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        if(getData().size() >0){
            ViewGroup.LayoutParams vlParams = helper.itemView.getLayoutParams();
            vlParams.width = dm.widthPixels /3;
            helper.itemView.setLayoutParams(vlParams);
        }else{
            ViewGroup.LayoutParams vlParams = helper.itemView.getLayoutParams();
            vlParams.width = dm.widthPixels - QMUIDisplayHelper.dp2px(getContext(),20);
            helper.itemView.setLayoutParams(vlParams);
        }

        if(!TextUtils.isEmpty(data.getMainPhoto())){
            if(data.getMainPhoto().equals("-1")){
                helper.setVisible(R.id.ll_recommendC,false);
                qiv_cover.setVisibility(View.GONE);
                qiv_cCover.setVisibility(View.VISIBLE);
                qiv_cCover.setImageDrawable(getContext().getDrawable(R.mipmap.ic_freegift_home));
            }else{
                helper.setVisible(R.id.ll_recommendC,true);
                qiv_cCover.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage(qiv_cover,data.getMainPhoto() + Constant.mGlobalThumbnailStyle,R.mipmap.allwees_ic_default_goods);
            }
        }

        if(data.getPriceRetail() == 0){
            helper.setText(R.id.tv_newFreeP, getContext().getString(R.string.str_free));
        }else{
            helper.setText(R.id.tv_newFreeP, MathUtil.Companion.formatPrice(data.getPriceRetail()));
        }

        tv_newRetailP.setText(MathUtil.Companion.formatPrice(data.getPriceOrigin()));

        helper.itemView.setOnClickListener(view -> {
            Intent goFreeG  = HolderActivity.of(getContext(), HomeClassifyGiftsFragment.class);
            getContext().startActivity(goFreeG);
        });
    }
}
