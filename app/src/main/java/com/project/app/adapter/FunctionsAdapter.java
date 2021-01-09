package com.project.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.app.R;
import com.project.app.bean.CatalogFunctions;

import java.util.List;

/**
 * 目录的首页列表
 */
public class FunctionsAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<CatalogFunctions> mDatas;

    public FunctionsAdapter(Context context, List<CatalogFunctions> datas){
        this.mContext = context;
        this.mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View conver, ViewGroup viewGroup) {
        ViewHolder holder;
        if(conver == null){
            conver = LayoutInflater.from(mContext).inflate(R.layout.item_main_functions,viewGroup,false);
            holder = new ViewHolder();
            holder.tv_content = conver.findViewById(R.id.tv_funstionName);
            conver.setTag(holder);
        }else{
            holder = (ViewHolder) conver.getTag();
        }

        CatalogFunctions item = mDatas.get(position);
        holder.tv_content.setText(item.getFunctionName());
        return conver;
    }

    //静态是为了防止多次调用
    static class ViewHolder{
        TextView tv_content;
    }

}
