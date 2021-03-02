package com.project.app.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hb.basemodel.image.ImageLoader;
import com.hb.basemodel.utils.DataUtil;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.bean.CategoryAllBean;
import com.project.app.fragment.home.classify.CategoryClassifyFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 分类里面的商品
 */
public class CategoryAdapter extends BaseQuickAdapter<CategoryAllBean.CategoryItem, BaseViewHolder> {
    private Context mContext;
    private String languateType;

    public CategoryAdapter(Context context,List<CategoryAllBean.CategoryItem> data,String languateType) {
        super(R.layout.item_category_goods, data);
        this.languateType = languateType;
        this.mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, CategoryAllBean.CategoryItem categoryItem) {
        RecyclerView rlv_goods = helper.getView(R.id.rlv_categoryGoods);
        rlv_goods.setLayoutManager(new LinearLayoutManager(getContext()));
        if(DataUtil.idNotNull(categoryItem.getChildren())){
            rlv_goods.setAdapter(new ItemKindsGoodsAdapter(categoryItem.getChildren()));
        }
    }

    class ItemKindsGoodsAdapter extends BaseQuickAdapter<CategoryAllBean.CategoryChildItem,BaseViewHolder>{
        public ItemKindsGoodsAdapter(List<CategoryAllBean.CategoryChildItem> goods) {
            super(R.layout.item_category_second_level_child,goods);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder helper, CategoryAllBean.CategoryChildItem item) {
            RecyclerView rlv_goods = helper.getView(R.id.rlv_secondChildCategory);
            rlv_goods.setLayoutManager(new GridLayoutManager(getContext(),3));
            if(DataUtil.idNotNull(item.getChildren())){
                rlv_goods.setAdapter(new ThredItemKindsGoodsAdapter(item.getChildren()));
            }
            if(!TextUtils.isEmpty(item.getName())){
                helper.setText(R.id.tv_categoryChildName,item.getName());
            }
        }
    }

    class ThredItemKindsGoodsAdapter extends BaseQuickAdapter<CategoryAllBean.CategoryThireLevelChildItem,BaseViewHolder>{

        public ThredItemKindsGoodsAdapter(List<CategoryAllBean.CategoryThireLevelChildItem> goods) {
            super(R.layout.item_category_thire_level_child,goods);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder helper, CategoryAllBean.CategoryThireLevelChildItem item) {
            if(!TextUtils.isEmpty(item.getImg())){
                ImageLoader.getInstance().displayImage(helper.getView(R.id.riv_childGoodsCover),item.getImg(),R.mipmap.allwees_ic_default_goods);
            }
            if(!TextUtils.isEmpty(item.getName())){
                helper.setText(R.id.tv_childGoodsName,item.getName());
            }
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", item.getNo());
                    bundle.putString("name",item.getName());
                    HolderActivity.startFragment(mContext, CategoryClassifyFragment.class,bundle);
                }
            });
        }
    }
}
