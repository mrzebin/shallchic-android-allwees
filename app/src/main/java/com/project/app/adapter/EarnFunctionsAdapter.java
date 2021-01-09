package com.project.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.project.app.R;
import com.project.app.bean.EarnItemBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 目录的首页列表
 */
public class EarnFunctionsAdapter extends BaseQuickAdapter<EarnItemBean, BaseViewHolder> {
    private final List<EarnItemBean> mDatas;

    public EarnFunctionsAdapter(@Nullable List<EarnItemBean> data) {
        super(R.layout.item_me_earn, data);
        this.mDatas = data;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, EarnItemBean bean) {
        int position = getItemPosition(bean);
        helper.setText(R.id.tv_earnTitle,bean.getItemName());
        if(position == 1){
            helper.setText(R.id.tv_castMoney,bean.getItemDes());
        }else if(position == 2){
            helper.setText(R.id.tv_castMoney,R.string.reward);
        }
    }
}
