package com.project.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.project.app.R;
import com.project.app.bean.CountryCropBean;
import com.project.app.utils.ImageCropUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ChoiceCountryAdapter extends BaseQuickAdapter<CountryCropBean.CountryItem, BaseViewHolder> {
    private Bitmap countryBitm;
    private final RecyclerView mRecyclerView;
    private final List<CountryCropBean.CountryItem> mDatas;

    public ChoiceCountryAdapter(Context context, @Nullable List<CountryCropBean.CountryItem> data, RecyclerView recyclerView) {
        super(R.layout.item_single_country, data);
        this.mDatas = data;
        this.mRecyclerView = recyclerView;
        this.countryBitm = BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_country);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, CountryCropBean.CountryItem countryItem) {
        int position = helper.getAdapterPosition();

        if(!TextUtils.isEmpty(countryItem.getNameEn())){
            helper.setText(R.id.tv_nationalName,countryItem.getNameEn());
        }
        ImageView iv_conutry = helper.getView(R.id.iv_sureCountry);
        ImageView iv_nationalFlag = helper.getView(R.id.iv_nationalFlag);

        if(countryItem.isSelect()){
            iv_conutry.setImageResource(R.mipmap.allwees_ic_cart_selected_circle_right_icon);
        }else{
            iv_conutry.setImageResource(R.mipmap.allwees_ic_cart_normal_circle_right_icon);
        }

        iv_nationalFlag.setImageBitmap(ImageCropUtils.cropBitmap(countryBitm,Integer.valueOf(countryItem.getColNum()), Integer.valueOf(countryItem.getRowNum())));

        //注意 这里不要监听checkbox的状态
        helper.itemView.setOnClickListener(view -> {
           if(listener != null){
               listener.choiceIndex(position,countryItem);
           }
        });
    }

    public SelectTargetListener listener;

    public SelectTargetListener getListener() {
        return listener;
    }

    public void setListener(SelectTargetListener listener) {
        this.listener = listener;
    }

    public void repalceAllData(List<CountryCropBean.CountryItem> countries) {
        if(mDatas != null){
            mDatas.clear();
        }
        mDatas.addAll(countries);
        notifyDataSetChanged();
    }

    public interface SelectTargetListener{
        void choiceIndex(int position,CountryCropBean.CountryItem item);
    }

}
