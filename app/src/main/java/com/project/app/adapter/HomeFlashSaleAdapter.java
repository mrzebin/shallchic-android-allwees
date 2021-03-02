package com.project.app.adapter;

import android.graphics.Paint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hb.basemodel.image.ImageLoader;
import com.hb.basemodel.uicomponent.roundview.RoundLinearLayout;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.bean.HomeFLashSaleBean;
import com.project.app.fragment.home.classify.FlashSaleFragment;
import com.project.app.utils.LocaleUtil;
import com.project.app.utils.MathUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomeFlashSaleAdapter extends BaseQuickAdapter<HomeFLashSaleBean.FlashSaleBean, BaseViewHolder> {
    private final DisplayMetrics dm;

    public HomeFlashSaleAdapter(List<HomeFLashSaleBean.FlashSaleBean> data, DisplayMetrics dm) {
        super(R.layout.item_home_flash_sale, data);
        this.dm = dm;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, HomeFLashSaleBean.FlashSaleBean data) {
        QMUIRadiusImageView qiv_cover = helper.getView(R.id.qiv_sfCover);
        RoundLinearLayout rll_discountWrap = helper.getView(R.id.rll_discountWrap);

        TextView tv_originP = helper.getView(R.id.tv_fsOriginP);
        tv_originP.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        if(getData().size() >0){
            ViewGroup.LayoutParams vlParams = helper.itemView.getLayoutParams();
            vlParams.width = dm.widthPixels /3;
            helper.itemView.setLayoutParams(vlParams);
        }else{
            ViewGroup.LayoutParams vlParams = helper.itemView.getLayoutParams();
            vlParams.width = dm.widthPixels - QMUIDisplayHelper.dp2px(getContext(),20);
            helper.itemView.setLayoutParams(vlParams);
        }

        if(data.getDiscountOff().equals("0%")){
            helper.setVisible(R.id.rll_discountWrap,false);
        }else{
            if(LocaleUtil.getInstance().getLanguage().equals("ar")){
                rll_discountWrap.setCornerRadius_TR(QMUIDisplayHelper.dp2px(getContext(),8));
            }else{
                rll_discountWrap.setCornerRadius_TL(QMUIDisplayHelper.dp2px(getContext(),8));
            }
            helper.setVisible(R.id.rll_discountWrap,true);
            helper.setText(R.id.tv_discountOff,data.getDiscountOff());
        }

        if(!TextUtils.isEmpty(data.getMainPhoto())){
            ImageLoader.getInstance().displayImage(qiv_cover,data.getMainPhoto(),R.mipmap.allwees_ic_default_goods);
        }

        if(data.getPriceRetail() == 0){
            helper.setText(R.id.tv_fsOriginP, getContext().getString(R.string.str_free));
        }

        if(data.getPriceOrigin() == data.getPriceRetail()){
            helper.setGone(R.id.tv_fsOriginP,true);
        }

        helper.setText(R.id.tv_fsOriginP,MathUtil.Companion.formatPrice(data.getPriceOrigin()));
        helper.setText(R.id.tv_fsRetailP,MathUtil.Companion.formatPrice(data.getPriceRetail()));

        helper.itemView.setOnClickListener(view -> {
            HolderActivity.startFragment(getContext(),FlashSaleFragment.class);
        });
    }
}
