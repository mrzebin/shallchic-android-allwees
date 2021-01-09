package com.project.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.project.app.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CountSelectAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public CountSelectAdapter(List<String> data){
        super(R.layout.item_scount, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, String s) {
        helper.setText(R.id.tv_digitC,s);
    }
}
