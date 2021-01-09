package com.project.app.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.image.ImageLoader;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.bean.CategoryItem;
import com.project.app.fragment.home.classify.CategoryClassifyFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CategoryAdapter extends BaseQuickAdapter<CategoryItem, BaseViewHolder> {

    public CategoryAdapter(List<CategoryItem> data) {
        super(R.layout.item_category_goods, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, CategoryItem categoryItem) {
        if(!categoryItem.getName().equals("P01")){
            String coverImgUrl = categoryItem.getImg();
            if(!TextUtils.isEmpty(coverImgUrl)){
                if(coverImgUrl.endsWith("png") || coverImgUrl.endsWith("jpg")){
                    ImageLoader.getInstance().displayImage(helper.getView(R.id.qm_cGood),coverImgUrl + Constant.mGlobalThumbnailStyle,R.mipmap.allwees_ic_default_goods);
                }else if(coverImgUrl.endsWith("gif")){
                    ImageLoader.getInstance().displayImage(helper.getView(R.id.qm_cGood),coverImgUrl ,R.mipmap.allwees_ic_default_goods);
                }
            }

            if(!TextUtils.isEmpty(categoryItem.getName())){
                helper.setText(R.id.tv_cGoodsName,categoryItem.getName());
            }
        }

        helper.itemView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("type", categoryItem.getNo());
            bundle.putString("name",categoryItem.getName());
            Intent intent = HolderActivity.of(getContext(), CategoryClassifyFragment.class,bundle);
            getContext().startActivity(intent);
        });
    }
}
