package com.project.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.project.app.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LegalAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public LegalAdapter(@Nullable List<String> data) {
        super(R.layout.item_kinds_set, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, String itemTitle) {
        helper.setText(R.id.tv_settingFun,itemTitle);
    }
}
