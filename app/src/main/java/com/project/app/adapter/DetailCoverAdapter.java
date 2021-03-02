package com.project.app.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hb.basemodel.image.ImageLoader;
import com.project.app.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 商品详情的封面
 */
public class DetailCoverAdapter extends RecyclerView.Adapter<com.project.app.adapter.DetailCoverAdapter.GoodDetailViewHolder> {
    private Context mContext;
    private ArrayList<String> mCoverList;

    public DetailCoverAdapter(Context context, ArrayList<String> mCoverList) {
        this.mContext = context;
        this.mCoverList = mCoverList;
    }

    @NonNull
    @Override
    public GoodDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_goodsdetail_cover,parent,false);
        return new GoodDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GoodDetailViewHolder holder, int position) {
        String loadUrl = mCoverList.get(position);
        ImageView pvView = holder.pvView;

        if(!TextUtils.isEmpty(loadUrl)){
            ImageLoader.getInstance().displayImage(pvView,loadUrl, R.mipmap.allwees_ic_default_goods);
        }else{
            holder.pvView.setImageDrawable(mContext.getDrawable(R.mipmap.allwees_ic_default_goods));
        }

        pvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.photoClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCoverList.size();
    }

    class GoodDetailViewHolder extends RecyclerView.ViewHolder{
        ImageView pvView;
        public GoodDetailViewHolder(View itemView) {
            super(itemView);
            this.pvView = itemView.findViewById(R.id.pv_gdCover);
        }
    }

    public IBrowseListener mListener;

    public void setmListener(IBrowseListener mListener) {
        this.mListener = mListener;
    }

    public interface IBrowseListener{
        void photoClick(int position);
    }

}
