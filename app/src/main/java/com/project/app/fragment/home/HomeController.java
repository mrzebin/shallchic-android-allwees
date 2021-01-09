package com.project.app.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.hb.basemodel.utils.DataUtil;
import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITab;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.project.app.R;
import com.project.app.activity.HolderActivity;
import com.project.app.adapter.BaseFragmentAdapter;
import com.project.app.base.BaseController;
import com.project.app.bean.CategoryBean;
import com.project.app.bean.CategoryItem;
import com.project.app.fragment.home.classify.HomeClassifyFragment;
import com.project.app.fragment.home.classify.HomePClassifyFragment;
import com.project.app.fragment.search.SearchGoodsFragment;
import com.project.app.ui.NoScrollViewPager;
import com.project.app.utils.HomeTitlesUtils;
import com.project.app.utils.LocaleUtil;
import com.project.app.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeController extends BaseController {
    @BindView(R.id.ll_inflateStateBar)
    LinearLayout ll_inflateBar;
    @BindView(R.id.qmts_home)
    QMUITabSegment mTabSegment;
    @BindView(R.id.vp_home)
    NoScrollViewPager vp_home;
    private Context mContext;
    private boolean isRever = false;
    private int mCurrentItemCount = 0;
    private final FragmentManager mFragmentManager;
    private final List<QMUIFragment> mFragments = new ArrayList<>();
    private final Map<ContentPage, View> mPageMap = new HashMap<>();
    private List<CategoryItem> mTitleCategory;
    private HomeTitlesUtils mHomeTitleUtils;
    private BaseFragmentAdapter mAdapter;

    public HomeController(Context context, FragmentManager childFragmentManager) {
        super(context);
        this.mFragmentManager = childFragmentManager;
        LayoutInflater.from(context).inflate(R.layout.home_layout,this);
        ButterKnife.bind(this);
        initTopBar();
        initView();
        initTabSegment();
    }

    private void initTabSegment() {
        QMUITabBuilder builder =  mTabSegment.tabBuilder();
        builder.setTypeface(null, Typeface.DEFAULT);
        builder.setNormalColor(getContext().getResources().getColor(R.color.white))
                .setSelectColor(getContext().getResources().getColor(R.color.white))
                .setTextSize(QMUIDisplayHelper.dp2px(mContext,13),QMUIDisplayHelper.dp2px(mContext,14))
                .setDynamicChangeIconColor(false)
                .skinChangeWithTintColor(false);

        for(CategoryItem item:mTitleCategory){
            QMUITab tab = builder.build(mContext);
            tab.setText(item.getName());
            mTabSegment.addTab(tab);
        }
        int space = QMUIDisplayHelper.dp2px(getContext(),16);
        mTabSegment.setIndicator(new QMUITabIndicator(QMUIDisplayHelper.dp2px(getContext(),2),false,true));
        mTabSegment.setIndicator(new QMUITabIndicator(ContextCompat.getDrawable(getContext(),R.drawable.bg_home_indicator),false,false));
        mTabSegment.notifyDataChanged();
        mTabSegment.setMode(QMUITabSegment.MODE_SCROLLABLE);
        mTabSegment.setItemSpaceInScrollMode(space);
        mTabSegment.setPadding(space,0,space,0);
        mTabSegment.canScrollVertically(getRight());
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

        vp_home.setCurrentItem(mCurrentItemCount);
        vp_home.setOffscreenPageLimit(2);
        vp_home.setNoScroll(false);     //让其可以滚动
        mTabSegment.setupWithViewPager(vp_home,false);
    }

    private void initTopBar() {
        StatusBarUtils.setStatusBarView(getContext(),ll_inflateBar);
        String languageId = LocaleUtil.getInstance().getLanguage();
        if(languageId.equals("ar")){
            isRever = true;
        }
    }

    private void initView() {
        mContext = getContext();
        HostMasterListener listener = fragment -> mHomeControlListener.startFragment(fragment);
        mHomeTitleUtils = HomeTitlesUtils.getInstance();
        CategoryBean bean = mHomeTitleUtils.getAppCategorys();
        mTitleCategory = bean.getCategories();

        if(isRever){
            Collections.reverse(mTitleCategory);
            mCurrentItemCount = mTitleCategory.size();
        }

        if(DataUtil.idNotNull(mTitleCategory)){
            for(CategoryItem item:mTitleCategory){
                if(item.getNo().equals("P01")){
                    mFragments.add(HomePClassifyFragment.newInstance(item.getNo(),listener));
                }else{
                    mFragments.add(HomeClassifyFragment.newInstance(item.getNo(),listener));
                }
            }
        }
        mAdapter = new BaseFragmentAdapter(mFragmentManager,mFragments);
        vp_home.setAdapter(mAdapter);
    }

    @OnClick({R.id.iv_home_search})
    public void onClickViewed(View view){
        switch (view.getId()){
            case R.id.iv_home_search:
                Intent searchIntent = HolderActivity.of(mContext, SearchGoodsFragment.class);
                mContext.startActivity(searchIntent);
                break;
        }
    }



    public enum ContentPage{
        Item0(0),
        Item1(1),
        Item2(2),
        Item3(3),
        Item4(4),
        Item5(5),
        Item6(6),
        Item7(7),
        Item8(8),
        Item9(9),
        Item10(10),
        Item11(11);
        private final int position;

        ContentPage(int pos){
            this.position = pos;
        }

        public static ContentPage getPage(int position){
            switch (position){
                case 1:
                    return Item1;
                case 2:
                    return Item2;
                case 3:
                    return Item3;
                case 4:
                    return Item4;
                case 5:
                    return Item5;
                case 6:
                    return Item6;
                case 7:
                    return Item7;
                case 8:
                    return Item8;
                case 9:
                    return Item9;
                case 10:
                    return Item10;
                case 11:
                    return Item11;
                default:
                    return Item0;
            }
        }
        public int getPosition() {
            return position;
        }
    }

    public void onRefreshView() {

    }

    public interface HostMasterListener{
        void startFragment(QMUIFragment fragment);
    }

}
