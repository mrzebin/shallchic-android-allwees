package com.project.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.event.RefreshDataEvent;
import com.hb.basemodel.image.ImageLoader;
import com.hb.basemodel.other.UserUtil;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.activity.LoginActivity;
import com.project.app.bean.HomeBannerBean;
import com.project.app.fragment.GoodsDetailFragment;
import com.project.app.fragment.WebExplorerFragment;
import com.project.app.fragment.address.AddressManagerFragment;
import com.project.app.fragment.earn.RewardFragment;
import com.project.app.fragment.earn.SchallCashFragment;
import com.project.app.fragment.notify.NotifyCenterFragment;
import com.youth.banner.adapter.BannerAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class HomeBannerAdapter extends BannerAdapter<HomeBannerBean,HomeBannerAdapter.BannerViewHolder> {
    private final Context mContext;

    public HomeBannerAdapter(Context context,List<HomeBannerBean> datas) {
        super(datas);
        this.mContext = context;
    }

    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new BannerViewHolder(imageView);
    }

    @Override
    public void onBindView(BannerViewHolder holder, HomeBannerBean data, int position, int size) {
        ImageLoader.getInstance().displayImage(holder.imageView,data.getImgUrl() + Constant.mGlobalThumbnailStyle, R.mipmap.allwees_ic_default_goods);
        String forwardUrl = data.getForwardUrl();
//       String forwardUrl = "allwees://category";
        holder.itemView.setOnClickListener(view -> {
            if(!TextUtils.isEmpty(forwardUrl)){
                if(!forwardUrl.startsWith("allwees://")){
                    return;
                }
                if(forwardUrl.startsWith("allwees://home")){    //首页

                }else if(forwardUrl.startsWith("allwees://category")){   //跳转到分类
                    EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_BANNER_SKIP_CATEGORY));
                }else if(forwardUrl.startsWith("allwees://shoppingcart")){   //跳转到购物车
                    EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_BANNER_SKIP_CART));
                }else if(forwardUrl.startsWith("allwees://wish")){
                    EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_BANNER_SKIP_WISH));
                }else if(forwardUrl.startsWith("allwees://mine")){           //跳转到我的
                    EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_BANNER_SKIP_MINE));
                }else if(forwardUrl.startsWith("allwees://goodslist")){      //跳转到类目
                    EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_BANNER_SKIP_CATEGORY));
                }else if(forwardUrl.startsWith("allwees://goodsdetail")){     //商品详情
                    int startI = forwardUrl.indexOf("=");
                    String goodsId = forwardUrl.substring(startI+1);
                    Bundle bundle = new Bundle();
                    bundle.putString("uuid", goodsId);
                    bundle.putString("type","1");
                    Intent intent = HolderActivity.of(mContext, GoodsDetailFragment.class,bundle);
                    mContext.startActivity(intent);
                }else if(forwardUrl.startsWith("allwees://login")){    //调换到登录
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                }else if(forwardUrl.startsWith("allwees://updateProfile")){  //更新用户信息

                }else if(forwardUrl.startsWith("allwees://changePassword")){ //修改密码

                }else if(forwardUrl.startsWith("allwees://address")){        //地址列表
                    if(UserUtil.getInstance().isLogin()){
                        Bundle bundle = new Bundle();
                        bundle.putString("type","1");
                        Intent goAddress = HolderActivity.of(mContext, AddressManagerFragment.class,bundle);
                        mContext.startActivity(goAddress);
                    }
                }else if(forwardUrl.startsWith("allwees://addressdetail")){  //地址添加

                }else if(forwardUrl.startsWith("allwees://feedback")){

                }else if(forwardUrl.startsWith("allwees://notification")){
                    Intent goNotify = HolderActivity.of(mContext, NotifyCenterFragment.class);
                    mContext.startActivity(goNotify);
                }else if(forwardUrl.startsWith("allwees://contactus")){

                }else if(forwardUrl.startsWith("allwees://language")){

                }else if(forwardUrl.startsWith("allwees://referral")){

                }else if(forwardUrl.startsWith("allwees://cash")){
                    Intent goCash = HolderActivity.of(mContext, SchallCashFragment.class);
                    mContext.startActivity(goCash);
                }else if(forwardUrl.startsWith("allwees://rewards")){
                    Intent goReward = HolderActivity.of(mContext, RewardFragment.class);
                    mContext.startActivity(goReward);
                }else if(forwardUrl.startsWith("allwees://dailyloginbouns")){

                }else if(forwardUrl.startsWith("allwees://orderhistory")){

                }else if(forwardUrl.startsWith("allwees://orderhistorydetail")){

                }else if(forwardUrl.startsWith("allwees://webview")){
                    int startI = forwardUrl.indexOf("=");
                    String webUrl = forwardUrl.substring(startI+1);
                    Bundle goWeb = new Bundle();
                    goWeb.putString("type","2");
                    goWeb.putString("webUrl",webUrl);
                    Intent intent = HolderActivity.of(mContext, WebExplorerFragment.class,goWeb);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder{
        final ImageView imageView;

        public BannerViewHolder(ImageView view){
            super(view);
            this.imageView = view;
        }
    }
}
