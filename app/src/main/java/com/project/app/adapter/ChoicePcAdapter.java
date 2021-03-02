package com.project.app.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.project.app.R;
import com.project.app.bean.ProvinceBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChoicePcAdapter extends BaseQuickAdapter<ProvinceBean, BaseViewHolder> {
    private final List<ProvinceBean> mDatas;


    public ChoicePcAdapter(@Nullable List<ProvinceBean> data) {
        super(R.layout.item_single_country, data);
        this.mDatas = data;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, ProvinceBean item) {
        int position = helper.getAdapterPosition();
        ImageView iv_conutry = helper.getView(R.id.iv_sureCountry);

        if(!TextUtils.isEmpty(item.getName())){
            helper.setText(R.id.tv_nationalName,item.getName());
        }

        if(item.isSelect()){
            iv_conutry.setImageResource(R.mipmap.allwees_ic_cart_selected_small_circle_right_icon);
        }else{
            iv_conutry.setImageResource(R.mipmap.allwees_ic_cart_normal_circle_small_right_icon);
        }

        //注意 这里不要监听checkbox的状态
        helper.itemView.setOnClickListener(view -> {
           if(listener != null){
               listener.choiceIndex(position,item);
           }
        });
    }

    public SelectTargetListener listener;

    public SelectTargetListener getListener() {
        return listener;
    }

    public void setListener(SelectTargetListener listener) {
        this.listener = listener;
    }

    public void repalceAllData(List<ProvinceBean> countries) {
        if(mDatas != null){
            mDatas.clear();
        }
        mDatas.addAll(countries);
        notifyDataSetChanged();
    }

    public interface SelectTargetListener{
        void choiceIndex(int position,ProvinceBean item);
    }

}
