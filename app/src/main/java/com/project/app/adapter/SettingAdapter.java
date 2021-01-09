package com.project.app.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hb.basemodel.other.UserUtil;
import com.project.app.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SettingAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SettingAdapter(@Nullable List<String> data) {
        super(R.layout.item_kinds_set, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, String itemTitle) {
        int position = helper.getAdapterPosition();
        helper.setText(R.id.tv_settingFun,itemTitle);
        if(position == 5){
            if(UserUtil.getInstance().isLogin()){
                helper.itemView.setVisibility(View.VISIBLE);
            }else{
                helper.itemView.setVisibility(View.GONE);
            }
        }
    }
}
