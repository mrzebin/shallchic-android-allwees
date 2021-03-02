package com.project.app.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hb.basemodel.config.Constant;
import com.hb.basemodel.event.RefreshDataEvent;
import com.hb.basemodel.image.ImageLoader;
import com.hb.basemodel.uicomponent.roundimageview.RoundedImageView;
import com.hb.basemodel.uicomponent.roundview.RoundLinearLayout;
import com.hb.basemodel.utils.DataUtil;
import com.project.app.R;
import com.project.app.bean.HomePopularActionBean;
import com.project.app.utils.LocaleUtil;
import com.project.app.utils.MathUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 活动专区
 */
public class HomeActivityAreaAdapter extends BaseQuickAdapter<HomePopularActionBean, BaseViewHolder> {
    private Context mContext;

    public HomeActivityAreaAdapter(Context context,List<HomePopularActionBean> data) {
        super(R.layout.item_home_activityarea, data);
        this.mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, HomePopularActionBean item) {
        if(!TextUtils.isEmpty(item.getIcon())){
            ImageLoader.getInstance().displayImage(helper.getView(R.id.iv_prefixIcon),item.getIcon(),R.mipmap.allwees_ic_default_goods);
        }
        if(!TextUtils.isEmpty(item.getTitle())){
            helper.setText(R.id.tv_activityAreaName,item.getTitle());
        }

        RecyclerView rlv_area = helper.getView(R.id.rlv_area);
        ActivityAreaChildAdapter adapter = new ActivityAreaChildAdapter(new ArrayList<>());
        GridLayoutManager manager = new GridLayoutManager(getContext(),2){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rlv_area.setLayoutManager(manager);
        rlv_area.setAdapter(adapter);

        if(DataUtil.idNotNull(item.getItems())){
            adapter.setNewInstance(item.getItems());
            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    if(!TextUtils.isEmpty(item.getAppLink())){
                        String activityTitle = item.getTitle();
                        String skipDeeplink = item.getAppLink() + "&" + activityTitle;
                        RefreshDataEvent activityAreaEvent = new RefreshDataEvent(Constant.EVENT_INNER_APP_DEEPLINK);
                        activityAreaEvent.setData(skipDeeplink);
                        EventBus.getDefault().post(activityAreaEvent);
                    }
                }
            });
        }
    }

    class ActivityAreaChildAdapter extends BaseQuickAdapter<HomePopularActionBean.ActivityAreaItem,BaseViewHolder>{

        public ActivityAreaChildAdapter(List<HomePopularActionBean.ActivityAreaItem> data) {
            super(R.layout.item_home_activityarea_child, data);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder helper, HomePopularActionBean.ActivityAreaItem item) {
            TextView tv_actionOriginPrice = helper.itemView.findViewById(R.id.tv_actionOrignPrice);
            RoundedImageView iv_gridItem = helper.getView(R.id.riv_actionKindsItem);

            if(!TextUtils.isEmpty(item.getMainPhoto())){
                ImageLoader.getInstance().displayImage(iv_gridItem,item.getMainPhoto(),R.mipmap.allwees_ic_default_goods);
            }

            if(item.getPriceOrigin() >0){
                helper.setVisible(R.id.tv_actionOrignPrice, true);
                tv_actionOriginPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                tv_actionOriginPrice.setText(MathUtil.Companion.formatPrice(item.getPriceOrigin()));
            }

            if(item.getPriceRetail() >0){
                helper.setText(R.id.tv_actionSalePrice,MathUtil.Companion.formatPrice(item.getPriceRetail()));
            }else if(item.getPriceRetail() <= 0){
                helper.setText(R.id.tv_actionSalePrice,getContext().getResources().getString(R.string.str_free));
            }

            if(!TextUtils.isEmpty(item.getDiscountOff())){
                if(item.getDiscountOff().equals("0%") ){
                    helper.setVisible(R.id.rll_discountCanvas,false);
                }else{
                    helper.setVisible(R.id.rll_discountCanvas,true);
                    RoundLinearLayout rll_discountCanvas = helper.itemView.findViewById(R.id.rll_discountCanvas);
                    if(LocaleUtil.getInstance().getLanguage().equals("ar")){
                        rll_discountCanvas.setCornerRadius_TR(QMUIDisplayHelper.dp2px(getContext(),3));
                    }else{
                        rll_discountCanvas.setCornerRadius_TL(QMUIDisplayHelper.dp2px(getContext(),3));
                    }
                    if(!TextUtils.isEmpty(item.getDiscountOff())){
                        helper.setText(R.id.tv_discountOff,item.getDiscountOff());
                    }
                }
            }
        }
    }
}
