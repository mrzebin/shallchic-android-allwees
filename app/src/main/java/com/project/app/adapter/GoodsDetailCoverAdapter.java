package com.project.app.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.image.ImageLoader;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.project.app.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/**
 * 商品详情的封面
 */
public class GoodsDetailCoverAdapter extends PagerAdapter {
    private final Context mContext;
    private final List<String> mCoverList;
    private final int mScreenWidth;
    private final int mMaxHeight = 375;

    public GoodsDetailCoverAdapter(Context context, List<String> coverList){
        this.mContext = context;
        this.mCoverList = coverList;
        this.mScreenWidth = QMUIDisplayHelper.getScreenWidth(context);
    }

    @Override
    public int getCount() {
        return mCoverList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView iv_cover = new ImageView(mContext);
        iv_cover.setScaleType(ImageView.ScaleType.FIT_CENTER);

        String loadUrl = mCoverList.get(position);
        if(!TextUtils.isEmpty(loadUrl)){
            ImageLoader.getInstance().displayImage(iv_cover,loadUrl + Constant.mGlobalThumbnailStyle,R.mipmap.allwees_ic_default_goods);
        }else{
            iv_cover.setImageDrawable(mContext.getDrawable(R.mipmap.allwees_ic_default_goods));
        }

        iv_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.photoClick(position);
            }
        });
        container.addView(iv_cover);
        return iv_cover;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       container.removeView((View) object);
    }

    public IBrowseListener mListener;

    public IBrowseListener getmListener() {
        return mListener;
    }

    public void setmListener(IBrowseListener mListener) {
        this.mListener = mListener;
    }

    public interface IBrowseListener{
        void photoClick(int position);
    }

}
