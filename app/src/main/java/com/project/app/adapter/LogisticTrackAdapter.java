package com.project.app.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.project.app.R;
import com.project.app.bean.LogisticTrackBean;
import com.project.app.utils.StringUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * create by hb
 * 物流信息
 */
public class LogisticTrackAdapter extends BaseQuickAdapter<LogisticTrackBean.LogisticTrackItem, BaseViewHolder> implements LoadMoreModule {
    private List<LogisticTrackBean.LogisticTrackItem> mData;

    public LogisticTrackAdapter(List<LogisticTrackBean.LogisticTrackItem> data) {
        super(R.layout.item_logistic_track, data);
        this.mData = data;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, LogisticTrackBean.LogisticTrackItem logisticTrackItem) {
        StringBuilder detailLog = new StringBuilder();

        if(!TextUtils.isEmpty(logisticTrackItem.getDetails())){
            detailLog.append(logisticTrackItem.getDetails());
        }
        if(!TextUtils.isEmpty(logisticTrackItem.getStatusDescription())){
            detailLog.append("\n");
            detailLog.append(logisticTrackItem.getStatusDescription());
        }

        if(helper.getAdapterPosition() == 0){
            helper.getView(R.id.iv_logTarget).setSelected(true);
            helper.setTextColor(R.id.tv_logTargetA,getContext().getResources().getColor(R.color.theme_color));
        }else{
            helper.getView(R.id.iv_logTarget).setSelected(false);
        }
        helper.setText(R.id.tv_logTargetA,detailLog.toString());
        helper.setText(R.id.tv_logisticM, StringUtils.getEnMDFormat(logisticTrackItem.getCheckTimeStamp()));
        helper.setText(R.id.tv_logisticHS,StringUtils.getHourMS(logisticTrackItem.getCheckTimeStamp()));

        if(helper.getAdapterPosition() == mData.size()){
            helper.setVisible(R.id.v_track,false);
        }

    }
}
