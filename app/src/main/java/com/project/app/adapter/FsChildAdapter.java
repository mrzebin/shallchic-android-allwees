package com.project.app.adapter;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hb.basemodel.image.ImageLoader;
import com.hb.basemodel.uicomponent.roundview.RoundLinearLayout;
import com.project.app.R;
import com.project.app.bean.HomeFLashSaleBean;
import com.project.app.config.AppsFlyConfig;
import com.project.app.utils.AppsFlyEventUtils;
import com.project.app.utils.LocaleUtil;
import com.project.app.utils.MathUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FsChildAdapter extends BaseQuickAdapter<HomeFLashSaleBean.FlashSaleBean, BaseViewHolder> {
    private int fsType;

    public FsChildAdapter(int type,List<HomeFLashSaleBean.FlashSaleBean> data) {
        super(R.layout.item_kinds_flash_sale, data);
        this.fsType = type;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, HomeFLashSaleBean.FlashSaleBean data) {
        QMUIRadiusImageView qiv_cover = helper.getView(R.id.qiv_fsGoodsCover);
        RoundLinearLayout rll_fsDiscountOff = helper.findView(R.id.rll_fsDiscountOff);

        TextView tv_fsOriginP = helper.findView(R.id.tv_fsOriginP);
        tv_fsOriginP.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        if(!TextUtils.isEmpty(data.getMainPhoto())){
            ImageLoader.getInstance().displayImage(qiv_cover,data.getMainPhoto(),R.mipmap.allwees_ic_default_goods);
        }

        if(!TextUtils.isEmpty(data.getName())){
            helper.setText(R.id.tv_fsGoodsTitle,data.getName());
        }

        if(data.getDiscountOff().equals("0%")){
            helper.setVisible(R.id.rll_fsDiscountOff,false);
        }else{
            helper.setVisible(R.id.rll_fsDiscountOff,true);
            helper.setText(R.id.tv_offValue,data.getDiscountOff());
        }

        if(data.getDiscountOff().equals("0%")){
            helper.setVisible(R.id.rll_fsDiscountOff,false);
        }else{
            if(LocaleUtil.getInstance().getLanguage().equals("ar")){
                rll_fsDiscountOff.setCornerRadius_TR(QMUIDisplayHelper.dp2px(getContext(),12));
            }else{
                rll_fsDiscountOff.setCornerRadius_TL(QMUIDisplayHelper.dp2px(getContext(),12));
            }
            helper.setVisible(R.id.rll_fsDiscountOff,true);
            helper.setText(R.id.tv_offValue,data.getDiscountOff());
        }

        if(data.getPriceRetail() == data.getPriceOrigin()){
            helper.setVisible(R.id.tv_fsOriginP,false);
        }

        helper.setText(R.id.tv_fsRetailP, MathUtil.Companion.formatPrice(data.getPriceRetail()));
        helper.setText(R.id.tv_fsOriginP, MathUtil.Companion.formatPrice(data.getPriceOrigin()));

        if(fsType == 1){   //如果是明天
            helper.setVisible(R.id.tv_fsAtc,false);
        }

        helper.getView(R.id.tv_fsAtc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_FLASH_SALE_GOODSITEM);
                if(listener != null){
                    listener.fastBuyTarget(data);
                }
            }
        });
    }

    public IAddToCarListener listener;

    public IAddToCarListener getListener() {
        return listener;
    }

    public void setListener(IAddToCarListener listener) {
        this.listener = listener;
    }

    public interface IAddToCarListener{
        void fastBuyTarget(HomeFLashSaleBean.FlashSaleBean item);
    }
}
