package com.project.app.adapter;

import android.graphics.Bitmap;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.project.app.R;
import com.project.app.utils.ImageCropUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 帮助中心
 */
public class CenterHelperAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private final List<String> mDatas;

    public CenterHelperAdapter(@Nullable List<String> data) {
        super(R.layout.item_me_earn, data);
        this.mDatas = data;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, String bean) {
        int position = getItemPosition(bean);
        helper.setText(R.id.tv_earnTitle,bean);

        if(position == 3){
            Bitmap bitmap = ImageCropUtils.getCropCountryFlag();
            helper.setVisible(R.id.iv_iconCountry,true);
            helper.setImageBitmap(R.id.iv_iconCountry,bitmap);
        }
    }
}
