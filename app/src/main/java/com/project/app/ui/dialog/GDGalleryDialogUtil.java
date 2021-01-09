package com.project.app.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.image.ImageConfig;
import com.hb.basemodel.image.ImageLoader;
import com.hb.basemodel.image.LoadListener;
import com.hb.basemodel.view.bm.PhotoView;
import com.project.app.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

/**
 * create by hb
 * 商品详情放大
 */
public class GDGalleryDialogUtil {
    private final Context mContext;
    private View mView;
    private final Dialog mDialog;
    private ViewPager2 vp_bmGallery;
    private LinearLayout ll_GalleryIndicator;
    private int mCurrentIndex = 0;
    private BmAdapter mAdapter;
    private ArrayList<String> mCoverList = new ArrayList<>();
    private TranslateAnimation animTrans;

    ViewPager2.PageTransformer mAnimator =new ViewPager2.PageTransformer() {
        @Override
        public void transformPage(@NonNull View page, float position) {
            Float absPos = Math.abs(position);
            Float scaleX ;
            Float scaleY;
            if (absPos > 1){
                scaleX=   0F;
                scaleY=   0F;
            }else{
                scaleX= 1 - absPos ;
                scaleY= 1 - absPos ;
            }
            page.setScaleX(scaleX);
            page.setScaleY(scaleY);
        }
    };

    public GDGalleryDialogUtil(Context context, boolean isCancelable, boolean iscancelOutside){
        this.mContext = context;
        mDialog = new Dialog(context, R.style.BoomWindowStyle);
        mDialog.setCancelable(isCancelable);
        mDialog.setCanceledOnTouchOutside(iscancelOutside);
        animTrans = new TranslateAnimation(0,0,0.5f,0.5f);
        animTrans.setDuration(300);
        build();
    }

    public void build(){
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_gallery_bm,null);
        mDialog.setContentView(mView);
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
        initChildView();
    }

    private void initViewPager2() {
        mAdapter = new BmAdapter();
        vp_bmGallery.setAdapter(mAdapter);
        vp_bmGallery.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
//        vp_bmGallery.setPageTransformer(new MarginPageTransformer((int) mContext.getResources().getDimension(R.dimen.item_pspace15)));  //设置间距
        vp_bmGallery.setOffscreenPageLimit(mCoverList.size());
        vp_bmGallery.setCurrentItem(mCurrentIndex);
        vp_bmGallery.setUserInputEnabled(true);   //可以设置可让其滑动
        //切换到指定页，是否展示过渡中间页
//        vp_bmGallery.setCurrentItem(1,true);
        //设置一个缩放动画
//        vp_bmGallery.setPageTransformer(mAnimator);

        vp_bmGallery.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(position != mCurrentIndex){    //注意如果都为0,会出现不显示的bug
                    ll_GalleryIndicator.getChildAt(position).setEnabled(true);
                    ll_GalleryIndicator.getChildAt(mCurrentIndex).setEnabled(false);
                    mCurrentIndex = position;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    private void setIndicator(int indicatorSize){
        View vIndicator;
        for(int i = 0; i<indicatorSize;i++){
            vIndicator = new View(mContext);
            vIndicator.setBackgroundResource(R.drawable.select_gallery_indicator_style);
            vIndicator.setEnabled(false);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(20,20);
            lp.leftMargin = 5;
            lp.rightMargin = 5;
            vIndicator.setLayoutParams(lp);
            ll_GalleryIndicator.addView(vIndicator);
        }
        //选中第一个
        ll_GalleryIndicator.getChildAt(0).setEnabled(true);
        //如果只有一个数据则不显示
        if(indicatorSize ==1){
            ll_GalleryIndicator.setVisibility(View.GONE);
        }else{
            ll_GalleryIndicator.setVisibility(View.VISIBLE);
        }
    }

    public void buildData(ArrayList<String> list){
        this.mCoverList = list;
        initViewPager2();
        setIndicator(list.size());
    }

    private void initChildView() {
        vp_bmGallery = mView.findViewById(R.id.vp_bmGallery);
        ll_GalleryIndicator = mView.findViewById(R.id.ll_GalleryIndicator);
    }

    /**
     * 显示图片
     * @param position
     */
    public void show(int position){
        vp_bmGallery.setCurrentItem(position,false);
        ll_GalleryIndicator.getChildAt(mCurrentIndex).setEnabled(true);
        mDialog.show();
    }

    public void dismiss() {
        if(mDialog != null){
            mDialog.dismiss();
        }
    }

    public class BmAdapter extends RecyclerView.Adapter<BaseViewHolder>{

        @Override
        public void onViewRecycled(@NonNull BaseViewHolder holder) {
            super.onViewRecycled(holder);
            PhotoView view = holder.getView(R.id.pv_zoomCover);
            if(view != null){
                Glide.with(mContext).clear(view);
            }
        }

        @NonNull
        @Override
        public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_gallery_loading,parent,false);
            return new BaseViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
            PhotoView pv_zoom = holder.itemView.findViewById(R.id.pv_zoomCover);
            ProgressBar pb_gdLoad = holder.itemView.findViewById(R.id.pb_gdLoad);
            String loadUrl = mCoverList.get(position);
            ImageConfig.Builder builder = new ImageConfig.Builder();
            builder.placeholder(R.mipmap.allwees_ic_default_goods);
            pv_zoom.enable();

            builder.listener(new LoadListener() {
                @Override
                public void onLoadSuccess(Bitmap bitmap, Drawable drawable) {
                    pb_gdLoad.setVisibility(View.GONE);
                }
                @Override
                public void onLoadFailed(Drawable errorDrawable) {
                    pb_gdLoad.setVisibility(View.GONE);
                }
            });

            if(!TextUtils.isEmpty(loadUrl)){
                ImageLoader.getInstance().displayImage(pv_zoom,loadUrl + Constant.mGlobalThumbnailStyle,new ImageConfig(builder));   //要防止内存溢出
            }else{
                pv_zoom.setImageResource(R.mipmap.allwees_ic_default_goods);
            }

            pv_zoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mCoverList.size();
        }
    }
}
