package com.project.app.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITab;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment2;
import com.project.app.R;
import com.project.app.adapter.BaseFragmentAdapter;
import com.project.app.adapter.LoginStateAdapter;
import com.project.app.base.BaseActivity;
import com.project.app.fragment.LoginInfFragment;
import com.project.app.fragment.LoginOutfFragment;
import com.project.app.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * create by zb
 * 登录activity
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.ll_inflateSb)
    View ll_inflateSb;
    @BindView(R.id.qmTab_switchFun)
    QMUITabSegment2 mTabSegment;
    @BindView(R.id.vp_loginContainer)
    ViewPager2 vp_container;
    @BindView(R.id.tv_hint_coupon)
    TextView tv_hint_coupon;

    private List<QMUIFragment> mFragments;
    private String[] titles = new String[]{};
    private BaseFragmentAdapter mAdapter;
    private LoginStateAdapter mAdapter2;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        refreshLocaleEnvironment();
        initTabSegment();
        initViewSprite();
    }

    @OnClick({R.id.iv_loginClose})
    public void onClickViewed(View view){
        if (view.getId() == R.id.iv_loginClose) {
            finish();
        }
    }

    private void initViewSprite() {
        mFragments = new ArrayList<>();
        mFragments.add(new LoginInfFragment());
        mFragments.add(new LoginOutfFragment());

        mAdapter = new BaseFragmentAdapter(getSupportFragmentManager(),mFragments);
        mAdapter2 = new LoginStateAdapter(mFragments,this);
        vp_container.setAdapter(mAdapter2);
    }

    private void initTabSegment() {
        mContext = this;
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        StatusBarUtils.setStatusBarView(this,ll_inflateSb);
        QMUITabBuilder builder =  mTabSegment.tabBuilder();
        titles = getResources().getStringArray(R.array.login_tab_segment);

        builder.setTypeface(null, Typeface.DEFAULT);
        builder.setNormalColor(mContext.getResources().getColor(R.color.color_bfbf))
                .setSelectColor(mContext.getResources().getColor(R.color.allwees_theme_color))
                .setTextSize(QMUIDisplayHelper.dp2px(mContext,14),QMUIDisplayHelper.dp2px(mContext,16))
                .setDynamicChangeIconColor(false)
                .skinChangeWithTintColor(false);

        for(String item:titles){
            QMUITab tab = builder.build(mContext);
            tab.setText(item);
            mTabSegment.addTab(tab);
        }
        int space = QMUIDisplayHelper.dp2px(mContext,16);
        mTabSegment.setIndicator(new QMUITabIndicator(ContextCompat.getDrawable(mContext,R.drawable.bg_indicator),false,true));
        mTabSegment.setMode(QMUITabSegment.MODE_SCROLLABLE);
        mTabSegment.setItemSpaceInScrollMode(space);
        mTabSegment.setPadding(space,0,space,0);

        mTabSegment.addOnTabSelectedListener(new QMUIBasicTabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                if(index == 0){
                    tv_hint_coupon.setVisibility(View.GONE);
                }else{
                    tv_hint_coupon.setVisibility(View.VISIBLE);
                }
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
        mTabSegment.setupWithViewPager(vp_container);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
