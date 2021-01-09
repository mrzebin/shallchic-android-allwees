package com.project.app.fragment.home;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.basemodel.config.Constant;
import com.hb.basemodel.utils.SPManager;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITab;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.project.app.R;
import com.project.app.adapter.BaseFragmentAdapter;
import com.project.app.base.BaseMvpQmuiFragment;
import com.project.app.bean.OrderTitleBean;
import com.project.app.fragment.order.OrderClassifyFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

public class OrderFragment extends BaseMvpQmuiFragment {
    @BindView(R.id.qmui_topbar)
    QMUITopBarLayout qmui_topbar;
    @BindView(R.id.ts_order)
    QMUITabSegment mTabSegment;
    @BindView(R.id.vp_order)
    ViewPager vp_order;
    @BindView(R.id.tv_topTitle)
    TextView tv_topTitle;
    @BindView(R.id.iv_back)
    ImageView iv_back;

    private boolean isRever = false;
    private int mCurrentItemCount  = 0 ;
    private String mMatchOrderName = "";
    private BaseFragmentAdapter mAdapter;
    private final List<OrderTitleBean> mTitles = new ArrayList<>();
    private FragmentManager mFragmentManager;
    private List<QMUIFragment> mFragments;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_order;
    }

    @Override
    public void initView() {
        initTopbar();
        initVp();
        initTabSegment();
    }

    private void initVp() {
        mFragments = new ArrayList<>();
        mFragmentManager = getChildFragmentManager();
        for(int i=0;i<mTitles.size();i++){
            mFragments.add(OrderClassifyFragment.newInstance(mTitles.get(i).getMatchTitle()));
        }
        mAdapter = new BaseFragmentAdapter(mFragmentManager,mFragments);
        vp_order.setAdapter(mAdapter);
    }

    private void initTopbar() {
        String title = getResources().getString(R.string.order_title);
        tv_topTitle.setText(title);
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
        Bundle bundle = getArguments();
        if(bundle != null){
            mMatchOrderName = bundle.getString("typeName");
        }

        String languageId = SPManager.sGetString(Constant.SP_LOCALE_LANGUAGE);   //判断语言切换
        if(languageId.equals("ar")){
            isRever = true;
        }

        String[] titles = getContext().getResources().getStringArray(R.array.order_tab_segment);
        for(int i=0;i<titles.length;i++){
            if(i == 0){
                mTitles.add(new OrderTitleBean(titles[i],"ALL"));
            }else if(i==1){
                mTitles.add(new OrderTitleBean(titles[i],"WAIT_SHIP"));
            }else if(i==2){
                mTitles.add(new OrderTitleBean(titles[i],"SHIPPED"));
            }else if(i==3){
                mTitles.add(new OrderTitleBean(titles[i],"DELIVERED"));
            }else if(i==4){
                mTitles.add(new OrderTitleBean(titles[i],"REVIEW"));
            }else if(i ==5){
                mTitles.add(new OrderTitleBean(titles[i],"REFUNDED"));
            }
        }

        if(isRever){
            Collections.reverse(mTitles);
        }
    }

    private void initTabSegment() {
        QMUITabBuilder builder =  mTabSegment.tabBuilder();
        builder.setTypeface(null, Typeface.DEFAULT);
        builder.setNormalColor(getContext().getResources().getColor(R.color.color_333))
                .setSelectColor(getContext().getResources().getColor(R.color.allwees_theme_color))
                .setTextSize(QMUIDisplayHelper.dp2px(getContext(),14),QMUIDisplayHelper.dp2px(getContext(),15))
                .setDynamicChangeIconColor(false)
                .skinChangeWithTintColor(false);


        for(OrderTitleBean item:mTitles){
            QMUITab tab = builder.build(getContext());
            tab.setText(item.getTitle());
            mTabSegment.addTab(tab);
        }

        if(!TextUtils.isEmpty(mMatchOrderName)){
            for(int i=0;i<mTitles.size();i++){
                if(mTitles.get(i).getMatchTitle().equals(mMatchOrderName)){
                    mCurrentItemCount = i;
                    break;
                }
            }
        }

        int space = QMUIDisplayHelper.dp2px(getContext(),25);
        mTabSegment.setIndicator(new QMUITabIndicator(QMUIDisplayHelper.dp2px(getContext(),2),false,true));
        mTabSegment.setIndicator(new QMUITabIndicator(ContextCompat.getDrawable(getContext(),R.drawable.bg_home_indicator),false,false));
        mTabSegment.notifyDataChanged();
        mTabSegment.setMode(QMUITabSegment.MODE_SCROLLABLE);
        mTabSegment.setItemSpaceInScrollMode(space);
        mTabSegment.setPadding(space,0,space,0);

        mTabSegment.addOnTabSelectedListener(new QMUIBasicTabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {

            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {

            }

            @Override
            public void onDoubleTap(int index) {

            }
        });

        vp_order.setCurrentItem(mCurrentItemCount);
        vp_order.setOffscreenPageLimit(2);
        mTabSegment.setupWithViewPager(vp_order,false);
    }

    @Override
    protected void lazyFetchData() {

    }

    @OnClick({R.id.iv_back})
    public void onClickViewed(View view){
        if (view.getId() == R.id.iv_back) {
            popBackStack();
        }
    }
}
