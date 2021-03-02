package com.project.app.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.project.app.R;
import com.project.app.bean.ChartSizeBean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChartSizeAdapter extends BaseQuickAdapter<ChartSizeBean.ChartSizeItem, BaseViewHolder> {

    public ChartSizeAdapter(List<ChartSizeBean.ChartSizeItem> data) {
        super(R.layout.item_chart_size, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, ChartSizeBean.ChartSizeItem item) {
        int position = helper.getAdapterPosition();
        RecyclerView rlv_chartTable = helper.getView(R.id.rlv_chartTable);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.HORIZONTAL);
        rlv_chartTable.setLayoutManager(manager);
        List<String> tableItems = new ArrayList<>();
        for(int i=0;i<3;i++){
            tableItems.addAll(item.getId());
        }

        rlv_chartTable.setAdapter(new ChartTableAdapter(tableItems));
    }

    class ChartTableAdapter extends BaseQuickAdapter<String, BaseViewHolder>{
        public ChartTableAdapter(List<String> tableDatas) {
            super(R.layout.item_chart_table,tableDatas);
        }
        @Override
        protected void convert(@NotNull BaseViewHolder helper, String content) {
            helper.setText(R.id.tv_tableContent,content);
        }
    }

}
