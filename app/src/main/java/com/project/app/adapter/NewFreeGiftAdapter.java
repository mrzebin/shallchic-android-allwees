package com.project.app.adapter;

import android.graphics.Paint;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hb.basemodel.image.ImageLoader;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.project.app.R;
import com.project.app.bean.HomeFreeGiftBean;
import com.project.app.utils.MathUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NewFreeGiftAdapter extends BaseQuickAdapter<HomeFreeGiftBean.GiftItem, BaseViewHolder> implements LoadMoreModule {

    public NewFreeGiftAdapter(List<HomeFreeGiftBean.GiftItem> data) {
        super(R.layout.item_new_free_gift, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, HomeFreeGiftBean.GiftItem goodInfo) {
        String free = getContext().getResources().getString(R.string.str_free);
        int position = helper.getPosition();
        QMUIRadiusImageView qiv_newGift = helper.getView(R.id.qiv_newGift);
        ImageView iv_unChoiceGift = helper.getView(R.id.iv_unChoiceGift);
        TextView tv_giftRetailP = helper.getView(R.id.tv_giftRetailP);
        tv_giftRetailP.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        if(!TextUtils.isEmpty(goodInfo.getMainPhoto())){
            ImageLoader.getInstance().displayImage(qiv_newGift,goodInfo.getMainPhoto(),R.mipmap.allwees_ic_default_goods);
        }

        if(goodInfo.getPriceRetail() == 0){
            helper.setText(R.id.tv_giftReduce, free);
        }else{
            helper.setText(R.id.tv_giftReduce, MathUtil.Companion.formatPrice(goodInfo.getPriceRetail()));
        }

        tv_giftRetailP.setText(MathUtil.Companion.formatPrice(goodInfo.getPriceOrigin()));

        iv_unChoiceGift.setSelected(goodInfo.isSelected());

        helper.itemView.setOnClickListener(view -> {
            if(listener != null){
                listener.choiceGiftsPosition(goodInfo);
            }
            List<HomeFreeGiftBean.GiftItem> datas = getData();
            for(int i = 0;i<datas.size();i++){
                datas.get(i).setSelected(i == position);
            }
            notifyDataSetChanged();
        });
    }

    ChoiceGiftListener listener;

    public ChoiceGiftListener getListener() {
        return listener;
    }

    public void setListener(ChoiceGiftListener listener) {
        this.listener = listener;
    }

    public interface ChoiceGiftListener{
        void choiceGiftsPosition(HomeFreeGiftBean.GiftItem item);
    }

}
