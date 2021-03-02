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
import com.project.app.config.AppsFlyConfig;
import com.project.app.fragment.GoodsDetailFragment;
import com.project.app.fragment.WebExplorerFragment;
import com.project.app.fragment.address.AddressManagerFragment;
import com.project.app.fragment.earn.RewardFragment;
import com.project.app.fragment.earn.SchallCashFragment;
import com.project.app.fragment.home.classify.CategoryClassifyFragment;
import com.project.app.fragment.home.classify.DeliverDoubleFragment;
import com.project.app.fragment.notify.NotifyCenterFragment;
import com.project.app.utils.AppsFlyEventUtils;
import com.youth.banner.adapter.BannerAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class HomeBannerAdapter extends BannerAdapter<HomeBannerBean, HomeBannerAdapter.BannerViewHolder> {
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
        ImageLoader.getInstance().displayImage(holder.imageView,data.getImgUrl(), R.mipmap.allwees_ic_default_goods);
        String forwardUrl = data.getForwardUrl();
//       String forwardUrl = "shallchic://category";
        holder.itemView.setOnClickListener(view -> {
            AppsFlyEventUtils.sendAppInnerEvent(AppsFlyConfig.AF_EVENT_BANNER_HOME);   //记录banner点击事件

            if(!TextUtils.isEmpty(forwardUrl)){
                if(!forwardUrl.startsWith("shallchic://")){
                    return;
                }
                if(forwardUrl.startsWith("shallchic://home")){    //首页

                }else if(forwardUrl.startsWith("shallchic://category")){   //跳转到分类
                    EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_BANNER_SKIP_CATEGORY));
                }else if(forwardUrl.startsWith("shallchic://shoppingcart")){   //跳转到购物车
                    EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_BANNER_SKIP_CART));
                }else if(forwardUrl.startsWith("shallchic://wish")){
                    EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_BANNER_SKIP_WISH));
                }else if(forwardUrl.startsWith("shallchic://mine")){           //跳转到我的
                    EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_BANNER_SKIP_MINE));
                }else if(forwardUrl.startsWith("shallchic://goodslist")){      //跳转到类目
                    if(forwardUrl.contains("=")){
                        int equIndex = forwardUrl.indexOf("=");
                        String kindsNo = forwardUrl.substring(equIndex+1,forwardUrl.length());
                        Bundle bundle = new Bundle();
                        bundle.putString("type", kindsNo);
                        bundle.putString("name","");
                        HolderActivity.startFragment(mContext, CategoryClassifyFragment.class,bundle);
                    }else{
                        EventBus.getDefault().post(new RefreshDataEvent(Constant.EVENT_BANNER_SKIP_CATEGORY));
                    }
                }else if(forwardUrl.startsWith("shallchic://goodsdetail")){     //商品详情
                    int startI = forwardUrl.indexOf("=");
                    String goodsId = forwardUrl.substring(startI+1);
                    Bundle bundle = new Bundle();
                    bundle.putString("uuid", goodsId);
                    bundle.putString("type","1");
                    HolderActivity.startFragment(mContext, GoodsDetailFragment.class,bundle);
                }else if(forwardUrl.startsWith("shallchic://login")){    //调换到登录
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                }else if(forwardUrl.startsWith("shallchic://updateProfile")){  //更新用户信息

                }else if(forwardUrl.startsWith("shallchic://changePassword")){ //修改密码

                }else if(forwardUrl.startsWith("shallchic://address")){        //地址列表
                    if(UserUtil.getInstance().isLogin()){
                        Bundle bundle = new Bundle();
                        bundle.putString("type","1");
                        HolderActivity.startFragment(mContext,AddressManagerFragment.class,bundle);
                    }
                }else if(forwardUrl.startsWith("shallchic://addressdetail")){  //地址添加

                }else if(forwardUrl.startsWith("shallchic://feedback")){

                }else if(forwardUrl.startsWith("shallchic://notification")){
                    HolderActivity.startFragment(mContext,NotifyCenterFragment.class);
                }else if(forwardUrl.startsWith("shallchic://contactus")){

                }else if(forwardUrl.startsWith("shallchic://language")){

                }else if(forwardUrl.startsWith("shallchic://referral")){

                }else if(forwardUrl.startsWith("shallchic://cash")){
                    HolderActivity.startFragment(mContext,SchallCashFragment.class);
                }else if(forwardUrl.startsWith("shallchic://rewards")){
                    HolderActivity.startFragment(mContext,RewardFragment.class);
                }else if(forwardUrl.startsWith("shallchic://dailyloginbouns")){

                }else if(forwardUrl.startsWith("shallchic://orderhistory")){

                }else if(forwardUrl.startsWith("shallchic://orderhistorydetail")){

                }else if(forwardUrl.startsWith("shallchic://webview")){
                    int startI = forwardUrl.indexOf("=");
                    String webUrl = forwardUrl.substring(startI+1);
                    Bundle goWeb = new Bundle();
                    goWeb.putString("type","2");
                    goWeb.putString("webUrl",webUrl);
                    HolderActivity.startFragment(mContext,WebExplorerFragment.class,goWeb);
                }else if(forwardUrl.startsWith("shallchic://buyandfree")){    //跳转都买一送一
                    HolderActivity.startFragment(mContext,DeliverDoubleFragment.class);
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
