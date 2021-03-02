package com.project.app.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.project.app.R;
import com.project.app.bean.CategoryAllBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CategoryMenuAdapter extends BaseQuickAdapter<CategoryAllBean.CategoryItem, BaseViewHolder> {
    public int mSelectIndex = 0; //默认选择的条数
    private String languateType;

    public CategoryMenuAdapter(List<CategoryAllBean.CategoryItem> data,String languateType) {
        super(R.layout.item_category_menu, data);
        this.languateType = languateType;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, CategoryAllBean.CategoryItem categoryItem) {
        if(mSelectIndex == helper.getAdapterPosition()){
            helper.setVisible(R.id.tv_MenuIndictor,true);
            helper.setTextColor(R.id.tv_MenuItemName,getContext().getResources().getColor(R.color.theme_color));
            helper.getView(R.id.ll_menu).setSelected(true);
        }else{
            helper.setVisible(R.id.tv_MenuIndictor,false);
            helper.getView(R.id.ll_menu).setSelected(false);
            helper.setTextColor(R.id.tv_MenuItemName,getContext().getResources().getColor(R.color.color_7d7d7d));
        }

        if(!TextUtils.isEmpty(categoryItem.getName())){
            helper.setText(R.id.tv_MenuItemName,categoryItem.getName());
        }
    }

    public void setmSelectIndex(int targetIndex) {
        if(mSelectIndex == targetIndex){
            return;
        }
        this.mSelectIndex = targetIndex;
        notifyDataSetChanged();
    }
}
